package com.huwenkai.com.mobilesafe.Activity;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;
import com.huwenkai.com.mobilesafe.db.BlacknumberDao.BlackNumberDao;
import com.huwenkai.com.mobilesafe.db.Domian.BlackNumberInfo;

import java.util.List;

public class BlackNumberActivity extends AppCompatActivity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mMyadapter == null) {
                mMyadapter = new Myadapter();
                mLv_blacknumber.setAdapter(mMyadapter);
            }else {
                mMyadapter.notifyDataSetChanged();
            }

        }
    };
    private List<BlackNumberInfo> mBlackNumberInfoList;
    private ListView mLv_blacknumber;
    private int mode;
    private Myadapter mMyadapter;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        initUI();
        initData();


    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getApplicationContext());
//                mBlackNumberInfoList = blackNumberDao.findall();
                //使用分页查询
                mBlackNumberInfoList = blackNumberDao.find(0);
                mHandler.sendEmptyMessage(0);
            }
        }).start();


    }

    private void initUI() {
        Button but_insert_blacknumber = (Button) findViewById(R.id.but_insert_blacknumber);
        mLv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
        mLv_blacknumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                //				OnScrollListener.SCROLL_STATE_FLING	飞速滚动
//				OnScrollListener.SCROLL_STATE_IDLE	 空闲状态
//				OnScrollListener.SCROLL_STATE_TOUCH_SCROLL	拿手触摸着去滚动状态
                boolean isload = false;
                if (mBlackNumberInfoList != null)
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                            mLv_blacknumber.getLastVisiblePosition() == mBlackNumberInfoList.size() - 1 && isload == false) {
                        //加载下面的20条目
                        isload = true;
                        //判断数据库是否还有数据
                        BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getApplicationContext());
                        mCount = blackNumberDao.getCount();
                        if (mCount > mBlackNumberInfoList.size()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getApplicationContext());
                                    //使用分页查询
                                    List<BlackNumberInfo> moreBlacknumber = blackNumberDao.find(mBlackNumberInfoList.size());
                                    mBlackNumberInfoList.addAll(moreBlacknumber);
                                    mHandler.sendEmptyMessage(0);
                                }
                            }).start();
                        }
                        isload = false;
                    }

            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        but_insert_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiglog();
            }
        });
    }

    private void showDiglog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final AlertDialog dialog = alertDialog.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);
        final EditText et_blacknumber = (EditText) view.findViewById(R.id.et_blacknumber);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        Button but_submit = (Button) view.findViewById(R.id.but_submit);
        Button but_cancel = (Button) view.findViewById(R.id.but_cancel);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;

                }
            }
        });


        dialog.setView(view);
        dialog.show();
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_blacknumber.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getApplicationContext());
                    blackNumberDao.insert(phone, mode + "");
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                    blackNumberInfo.mode = mode + "";
                    blackNumberInfo.phone = phone;
                    mBlackNumberInfoList.add(0, blackNumberInfo);
                    if (mMyadapter != null) {
                        mMyadapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                } else {
                    ToastUtils.show(getApplication(), "请输入电话号码");
                }
            }
        });
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private class Myadapter extends BaseAdapter {
        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return mBlackNumberInfoList.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return mBlackNumberInfoList.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.blacknumber_item, null);
                viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                viewHolder.im_delete_blacknumber = (ImageView) convertView.findViewById(R.id.im_delete_blacknumber);
                convertView.setTag(viewHolder);
            }


            final String phone = mBlackNumberInfoList.get(position).phone;
            viewHolder.im_delete_blacknumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getApplicationContext());
                    blackNumberDao.delete(phone);
                    mBlackNumberInfoList.remove(position);
                    mMyadapter.notifyDataSetChanged();

                }
            });
            viewHolder.tv_phone.setText(mBlackNumberInfoList.get(position).phone);
            switch (Integer.parseInt(mBlackNumberInfoList.get(position).mode)) {
                case 1:
                    viewHolder.tv_mode.setText("拦截短信");
                    break;
                case 2:
                    viewHolder.tv_mode.setText("拦截电话");
                    break;
                case 3:
                    viewHolder.tv_mode.setText("拦截所有");
                    break;

            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_phone;
            TextView tv_mode;
            ImageView im_delete_blacknumber;

        }
    }
}
