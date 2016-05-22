package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.MessageDigestUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;

/**
 * 程序的主界面
 * Created by huwenkai on 2016/4/23.
 */
public class HomeActivity extends Activity {

    private GridView mGv_home;
    private String[] mTitleStr;
    private int[] mIconIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initData();
    }

    private void initData() {
        mTitleStr = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mIconIDs = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
                        R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings};
        mGv_home.setAdapter(new MyAdapter());
        mGv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showdialog();
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),ProcessActivity.class));
                        break;
                    case 7:

                        startActivity(new Intent(getApplicationContext(),AToolActivity.class));
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showdialog() {
        //读取本地文件查看是否有保存密码
        final String pwd = SpUtils.getString(ContactsValues.MOBILE_SAFE_PWD, "", this);
        //判断是密码是否为空
        if (TextUtils.isEmpty(pwd)){
            //弹出设置密码对话框
            setMobliesafePwd();

        }else {
            //弹出确认密码的对话框
            confirmPwd();
        }

    }

    private void confirmPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(this, R.layout.confrim_pwd, null);
        alertDialog.setView(view);
        alertDialog.show();
        Button bt_yes = (Button) view.findViewById(R.id.bt_yes);
        Button bt_no = (Button) view.findViewById(R.id.bt_no);
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView confirm_pwd = (TextView) view.findViewById(R.id.confirm_mobile_safe_pwd);
                if(!TextUtils.isEmpty(confirm_pwd.getText().toString())){
                    String local_pwd = SpUtils.getString(ContactsValues.MOBILE_SAFE_PWD, "", getApplicationContext());
                    String encryption_pwd = MessageDigestUtils.StringToMD5(confirm_pwd.getText().toString(),"MD5");
                    if(TextUtils.equals(local_pwd,encryption_pwd)){
//                        Intent intent = new Intent(HomeActivity.this,Testactivity.class);
                        Intent intent = new Intent(HomeActivity.this,SetupvoerActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();

                    }else{
                        ToastUtils.show(getApplication(),"密码错误");
                    }

                }else{
                    ToastUtils.show(getApplication(),"密码不能为空");
                }

            }
        });
    }

    private void setMobliesafePwd() {
        //弹出初始化密码的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(this, R.layout.safe_dialog_pwd, null);
        alertDialog.setView(view);
        alertDialog.show();
        Button bt_yes = (Button) view.findViewById(R.id.bt_yes);
        Button bt_no = (Button) view.findViewById(R.id.bt_no);
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mobile_safe_pwd = (TextView) view.findViewById(R.id.mobile_safe_pwd);
                TextView confirm_pwd = (TextView) view.findViewById(R.id.confirm_mobile_safe_pwd);
                if(!TextUtils.isEmpty(mobile_safe_pwd.getText().toString())&&!TextUtils.isEmpty(confirm_pwd.getText().toString())){
                    if(TextUtils.equals(mobile_safe_pwd.getText().toString(),confirm_pwd.getText().toString())){
//                        Intent intent = new Intent(HomeActivity.this,Testactivity.class);
                        Intent intent = new Intent(HomeActivity.this,SetupvoerActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                        String string_pwd = confirm_pwd.getText().toString();

                        SpUtils.putString(ContactsValues.MOBILE_SAFE_PWD, MessageDigestUtils.StringToMD5(string_pwd,"MD5"),getApplicationContext());
                    }else{
                        ToastUtils.show(getApplication(),"密码不唯一");
                    }

                }else{
                    ToastUtils.show(getApplication(),"密码不能为空");
                }

            }
        });
    }

    private void initUI() {
        mGv_home = (GridView) findViewById(R.id.gv_home);

    }

    private class MyAdapter extends BaseAdapter {
        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return mIconIDs.length;
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
            return mTitleStr[position];
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= null;
            view = View.inflate(getApplicationContext(),R.layout.gv_item,null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView  tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_icon.setBackgroundResource(mIconIDs[position]);
            tv_title.setText(mTitleStr[position]);
            return view;
        }

    }
}
