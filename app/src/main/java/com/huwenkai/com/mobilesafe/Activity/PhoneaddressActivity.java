package com.huwenkai.com.mobilesafe.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.Engine.AddressDao;
import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;

public class PhoneaddressActivity extends AppCompatActivity {

    private Button bt_query_address;
    private EditText ed_number_address;
    private TextView tv_query_result;
    private String mAddress;
private Handler mHandler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        tv_query_result.setText(mAddress);
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneaddress);
        initUI();
    }

    private void initUI() {
        bt_query_address = (Button) findViewById(R.id.bt_query_address);
        ed_number_address = (EditText) findViewById(R.id.ed_number_address);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);
        bt_query_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = ed_number_address.getText().toString().trim();
                if(phone.length()>7){
                    query(phone);

                }else{
                    ToastUtils.show(getApplicationContext(),"输入号码");
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    ed_number_address.startAnimation(shake);
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{1000,1000,2000,1000},-1);
                }

            }

            private void query(final String phone) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mAddress = AddressDao.getAddress(phone);
                        Message message = Message.obtain();
                        mHandler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
    }


}
