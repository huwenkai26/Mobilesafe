package com.huwenkai.com.mobilesafe.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.Engine.ProcessInfoProvider;
import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.LogUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;
import com.huwenkai.com.mobilesafe.db.Domian.ProcessInfo;


import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ProcessInfo> mUserList;
    public ArrayList<ProcessInfo> mSystemList;
    private List<ProcessInfo> mProcessInfolist;
    private Myadpater mMyadpater;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            mMyadpater = new Myadpater();
            mLv_process.setAdapter(mMyadpater);

            mLv_process.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem > mUserList.size()) {
                        mTv_process_title.setText("系统进程(" + mSystemList.size() + ")");
                    } else {

                        mTv_process_title.setText("用户进程(" + mUserList.size() + ")");
                    }

                }
            });
            mLv_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0 || position == mUserList.size() + 1) {
                        return;
                    } else {
                        if (position <= mUserList.size()) {
                            mProcessInfo = mUserList.get(position - 1);

                        } else {
                            mProcessInfo = mSystemList.get(position - mUserList.size() - 2);

                        }
                        if (mProcessInfo != null)
                            if (!(mProcessInfo.packageName.equals(getPackageName()))) {
                                mProcessInfo.isCheck = !mProcessInfo.isCheck;
                                CheckBox cb_checkbox = (CheckBox) view.findViewById(R.id.cb_checkbox);
                                cb_checkbox.setChecked(mProcessInfo.isCheck);
                                LogUtils.e("ProcessActivity", position + mProcessInfo.toString());
                            }


                    }

                }

            });


        }
    };
    private ListView mLv_process;
    private TextView mTv_process_title;
    private ProcessInfo mProcessInfo;
    private TextView mTv_process_count;
    private int KillMemorySize;
    private TextView mTv_memory;
    private String mAvailSize;
    private String mTotalSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        initUI();
        initListData();
    }

    private void initListData() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                mProcessInfolist = ProcessInfoProvider.getProcessInfo(getApplicationContext());
                mUserList = new ArrayList<ProcessInfo>();
                mSystemList = new ArrayList<ProcessInfo>();

                for (ProcessInfo processInfo : mProcessInfolist) {
                    if (processInfo.isSystem) {
                        mSystemList.add(processInfo);
                    } else {
                        mUserList.add(processInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();


    }

    private void initUI() {
        mTv_process_count = (TextView) findViewById(R.id.tv_process_count);
        mTv_memory = (TextView) findViewById(R.id.tv_memory);
        mLv_process = (ListView) findViewById(R.id.lv_process);
        mTv_process_title = (TextView) findViewById(R.id.tv_process_title);
        mTv_process_count.setText("进程总数:" + ProcessInfoProvider.getProcessCount(this));
        mAvailSize = Formatter.formatFileSize(this, ProcessInfoProvider.getAvailSpace(this));
        mTotalSize = Formatter.formatFileSize(this, ProcessInfoProvider.getTotalSpace(this));
        mTv_memory.setText("剩余/总共:" + mAvailSize + "/" + mTotalSize);
        Button bt_select_all = (Button) findViewById(R.id.bt_select_all);
        Button bt_disselec_all = (Button) findViewById(R.id.bt_disselec_all);
        Button bt_clean = (Button) findViewById(R.id.bt_clean);
        Button bt_setting = (Button) findViewById(R.id.bt_setting);
        bt_select_all.setOnClickListener(this);
        bt_disselec_all.setOnClickListener(this);
        bt_clean.setOnClickListener(this);
        bt_setting.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_select_all:
                selectAll();
                break;
            case R.id.bt_disselec_all:
                disselectall();
                break;
            case R.id.bt_clean:
                cleanProcess();
                break;
            case R.id.bt_setting:
                Intent intent = new Intent(this,ProcessSettingActivity.class);
                startActivityForResult(intent,0);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mMyadpater!=null){
            mMyadpater.notifyDataSetChanged();
        }

    }

    private void cleanProcess() {
        ArrayList<ProcessInfo> killProcesList = new ArrayList<>();
        KillMemorySize = 0;
        for(ProcessInfo processInfo : mUserList){
            if(!processInfo.packageName.equals(getPackageName())){
               if(processInfo.isCheck){
                   ProcessInfoProvider.KillProcess(this,processInfo);
                   killProcesList.add(processInfo);
                   KillMemorySize += processInfo.memSize;
               }

            }
        }
        for (ProcessInfo processInfo : mSystemList){
            if(processInfo.isCheck){
                ProcessInfoProvider.KillProcess(this,processInfo);
                killProcesList.add(processInfo);
                KillMemorySize += processInfo.memSize;
            }
        }
        for (ProcessInfo processInfo : killProcesList){
            if(processInfo.isSystem){
                mSystemList.remove(processInfo);
            }else {
                mUserList.remove(processInfo);
            }
        }
        mTv_process_count.setText("进程总数:" +( ProcessInfoProvider.getProcessCount(this)-killProcesList.size()));
        mAvailSize = Formatter.formatFileSize(this, ProcessInfoProvider.getAvailSpace(this)+KillMemorySize);
        mTv_memory.setText("剩余/总共:" + mAvailSize + "/" + mTotalSize);
        if(mMyadpater!=null){
            mMyadpater.notifyDataSetChanged();
        }
        ToastUtils.show(this,String.format("你是杀死了%d个进程，并且释放了%s",killProcesList.size(),
                Formatter.formatFileSize(this,KillMemorySize)));
    }

    private void disselectall() {
        for(ProcessInfo processInfo : mUserList){
            if(!processInfo.packageName.equals(getPackageName())){
                processInfo.isCheck = !processInfo.isCheck ;
            }
        }
        for (ProcessInfo processInfo : mSystemList){
            processInfo.isCheck = !processInfo.isCheck ;
        }
        if(mMyadpater!=null){
            mMyadpater.notifyDataSetChanged();
        }
    }

    private void selectAll() {
        for(ProcessInfo processInfo : mUserList){
            if(!processInfo.packageName.equals(getPackageName())){
                processInfo.isCheck = true ;
            }
        }
        for (ProcessInfo processInfo : mSystemList){
            processInfo.isCheck = true;
        }
        if(mMyadpater!=null){
            mMyadpater.notifyDataSetChanged();
        }
    }

    class Myadpater extends BaseAdapter {


        /**
         * @return 集合的大小加上2个灰色条目
         */
        @Override
        public int getCount() {
            Boolean isShow = SpUtils.getBoolean(ContactsValues.SHOW_SYSTEM, false, getApplicationContext());
            if(isShow){
                return mSystemList.size() + mUserList.size() + 2;
            }else {
                return mUserList.size()+1;
            }

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
        public ProcessInfo getItem(int position) {
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
                    titleViewHolder.text.setText("用户进程(" + mUserList.size() + ")");
                } else {
                    titleViewHolder.text.setText("系统进程(" + mSystemList.size() + ")");
                }

                return convertView;


            } else {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.list_item_process, null);
                    viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
                    viewHolder.tv_process_memory = (TextView) convertView.findViewById(R.id.tv_process_memory);
                    viewHolder.cb_checkbox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.appName.setText(getItem(position).name);
                viewHolder.icon.setBackground(getItem(position).icon);
                if (getItem(position).packageName.equals(getPackageName())) {
                    viewHolder.cb_checkbox.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.cb_checkbox.setVisibility(View.VISIBLE);
                }
                viewHolder.tv_process_memory.setText("内存占用:" + Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize));
                viewHolder.cb_checkbox.setChecked(getItem(position).isCheck);
                return convertView;
            }


        }

    }

    static class ViewHolder {
        public TextView appName;
        public TextView tv_process_memory;
        public ImageView icon;
        public CheckBox cb_checkbox;
    }

    static class TitleViewHolder {
        public TextView text;
    }
}
