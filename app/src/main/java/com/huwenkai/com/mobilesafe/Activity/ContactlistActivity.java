package com.huwenkai.com.mobilesafe.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用来选择联系人的activity
 */
public class ContactlistActivity extends AppCompatActivity {

    private ListView lv_contact;
    private List<HashMap<String,String>> list= new ArrayList<HashMap<String, String>>();
    private android.os.Handler mHandle = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (HashMap<String,String> hashmap: list) {
                LogUtils.e("dsdsa",hashmap.get("name"));
                LogUtils.e("dsdsa",hashmap.get("phone"));
            }
            Myadapter myadapter = new Myadapter();
            lv_contact.setAdapter(myadapter);


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);
        initUI();
        initData();
    }

    /**
     * 为显示联系人的listview适配数据
     */
    private void initData() {
        //因为数据库数据可能很多所有可能造成耗时操作所以在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = getContentResolver();
                Uri contact_id_uri = Uri.parse("content://com.android.contacts/raw_contacts");
                Cursor cursor = contentResolver.query(contact_id_uri, new String[]{"contact_id"}, null, null, null);
                list.clear();
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    Uri data_uri = Uri.parse("content://com.android.contacts/data");
                    Cursor cursor1 = contentResolver.query(data_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                    HashMap<String,String> hashMap = new HashMap<String, String>();
                        while(cursor1.moveToNext()){
                            String data = cursor1.getString(0);
                            String type = cursor1.getString(1);
                            if(type.equals("vnd.android.cursor.item/phone_v2")){
                                hashMap.put("phone",data);
                            }else if(type.equals("vnd.android.cursor.item/name")){
                                hashMap.put("name",data);
                            }


                        }
                    cursor1.close();
                    list.add(hashMap);

                }
                cursor.close();
                mHandle.sendEmptyMessage(0);
            }
        }).start();


    }

    /**
     * 找到显示联系人的listview
     */
    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = list.get(position);
                String phone_munber = hashMap.get("phone");
                phone_munber = phone_munber.replaceAll("[^0-9]","");
                Intent intent =new Intent();
                intent.putExtra("phone_number",phone_munber);

                setResult(1,intent);
                finish();

            }
        });
    }

    /**
     * 用来适配联系人的adpater
     */
    private class Myadapter extends BaseAdapter {
        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return list.size();
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
            return list.get(position);
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
            View view = null;
            if(convertView==null){
                 view = View.inflate(getApplicationContext(), R.layout.contact_item, null);

            }else {
                view = convertView;
            }
            TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            TextView tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
            HashMap<String, String> hashMap = list.get(position);
            tv_contact_name.setText(hashMap.get("name"));
            tv_contact_phone.setText(hashMap.get("phone"));
            return view;
        }
    }
}
