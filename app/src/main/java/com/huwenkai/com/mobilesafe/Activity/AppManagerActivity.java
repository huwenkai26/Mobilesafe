package com.huwenkai.com.mobilesafe.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.Engine.AppInfoProvider;
import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;
import com.huwenkai.com.mobilesafe.db.Domian.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private List<AppInfo> mAppInfos;
    private ListView mLv_app_list;
    private List<AppInfo> mUserList;
    private List<AppInfo> mSystemList;
    private TextView mTv_app_type;
    private AppInfo mAppInfo;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            mMyadpater = new Myadpater();
            mLv_app_list.setAdapter(mMyadpater);
            mLv_app_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem > mUserList.size()) {
                        mTv_app_type.setText("系统应用(" + mSystemList.size() + ")");
                    } else {

                        mTv_app_type.setText("用户应用(" + mUserList.size() + ")");
                    }

                }
            });
            mLv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0 || position == mUserList.size() + 1) {
                        return;
                    } else {
                        if (position <= mUserList.size()) {
                            mAppInfo = mUserList.get(position - 1);
                        } else {
                            mAppInfo = mSystemList.get(position - mUserList.size() - 2);
                        }
                    }
                    showPopupWindow(view);
                }
            });
        }
    };
    private PopupWindow mPopupWindow;
    private Myadpater mMyadpater;

    private void showPopupWindow(View view) {
        View popupView = View.inflate(this, R.layout.popup_layout, null);
        TextView tv_uninstall = (TextView) popupView.findViewById(R.id.tv_uninstall);
        TextView tv_start = (TextView) popupView.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) popupView.findViewById(R.id.tv_share);
        tv_uninstall.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);


        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.showAsDropDown(view, 50, -view.getHeight());
        popupView.startAnimation(animationSet);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_uninstall:
/*<action android:name="android.intent.action.VIEW"/>

<action android:name="android.intent.action.DELETE"/>

<category android:name="android.intent.category.DEFAULT"/>

<data android:scheme="package"/>*/
                if (!mAppInfo.isSystem) {
                    Intent intent = new Intent("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:"+mAppInfo.packageName));
                    startActivity(intent);
                }else {
                    ToastUtils.show(getApplicationContext(),"系统应用不能卸载");
                }
                mUserList.remove(mAppInfo);
                break;
            case R.id.tv_start:
               PackageManager pm = getPackageManager();
                //通过Launch开启制定包名的意图,去开启应用
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.packageName);
                if(launchIntentForPackage!=null){
                    startActivity(launchIntentForPackage);
                }
                break;
            case R.id.tv_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"分享一个应用,应用名称为"+mAppInfo.name);
                intent.setType("text/plain");
                startActivity(intent);
                break;
        }
        if(mPopupWindow!=null)
            mPopupWindow.dismiss();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initTilite();
        initListView();
    }

    private void initListView() {
        mLv_app_list = (ListView) findViewById(R.id.lv_app_list);

        initlistviewData();

    }

    private void initlistviewData() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                mAppInfos = AppInfoProvider.getAppinfo(getApplicationContext());
                mUserList = new ArrayList<AppInfo>();
                mSystemList = new ArrayList<AppInfo>();

                for (AppInfo appInfo : mAppInfos) {
                    if (appInfo.isSystem) {
                        mSystemList.add(appInfo);
                    } else {
                        mUserList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMyadpater != null) {

            mMyadpater.notifyDataSetChanged();
        }
    }

    class Myadpater extends BaseAdapter {


        /**
         * @return 集合的大小加上2个灰色条目
         */
        @Override
        public int getCount() {
            return mAppInfos.size() + 2;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mUserList.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public AppInfo getItem(int position) {
            if (position == 0 || position == mUserList.size() + 1) {
                return null;
            } else {
                if (position <= mUserList.size()) {
                    return mUserList.get(position - 1);
                } else {
                    return mSystemList.get(position - mUserList.size() - 2);
                }
            }

        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int mode = getItemViewType(position);
            if (mode == 0) {
                TitleViewHolder titleViewHolder = null;
                if (convertView == null) {
                    titleViewHolder = new TitleViewHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.list_item_app_title, null);

                    titleViewHolder.text = (TextView) convertView.findViewById(R.id.tv_app_type);
                    convertView.setTag(titleViewHolder);
                } else {
                    titleViewHolder = (TitleViewHolder) convertView.getTag();
                }
                if (position == 0) {
                    titleViewHolder.text.setText("用户应用(" + mUserList.size() + ")");
                } else {
                    titleViewHolder.text.setText("系统应用(" + mSystemList.size() + ")");
                }

                return convertView;


            } else {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.list_item_app, null);
                    viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
                    viewHolder.tv_path = (TextView) convertView.findViewById(R.id.tv_issystem);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.appName.setText(getItem(position).name);
                viewHolder.icon.setBackground(getItem(position).icon);
                if (getItem(position).isSdCard) {
                    viewHolder.tv_path.setText("SD卡应用");
                } else {
                    viewHolder.tv_path.setText("手机应用");
                }
                return convertView;
            }


        }

    }

    static class ViewHolder {
        public TextView appName;
        public TextView tv_path;
        public ImageView icon;
    }

    static class TitleViewHolder {
        public TextView text;
    }

    private void initTilite() {
        TextView tv_disk_memory = (TextView) findViewById(R.id.tv_disk_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);
        mTv_app_type = (TextView) findViewById(R.id.tv_app_type);
        String diskPath = Environment.getDataDirectory().getAbsolutePath();
        String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();


        tv_disk_memory.setText("磁盘可用:" + getMemory(diskPath));
        tv_sd_memory.setText("sd卡可用:" + getMemory(SDPath));

    }

    private String getMemory(String Path) {
        StatFs statFs = new StatFs(Path);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        String fileSize = Formatter.formatFileSize(getApplicationContext(), blockSize * availableBlocks);
        return fileSize;

    }
}
