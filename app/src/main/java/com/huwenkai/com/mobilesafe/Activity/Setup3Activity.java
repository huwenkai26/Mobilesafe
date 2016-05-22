package com.huwenkai.com.mobilesafe.Activity;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import com.huwenkai.com.mobilesafe.R;
        import com.huwenkai.com.mobilesafe.Util.ContactsValues;
        import com.huwenkai.com.mobilesafe.Util.SpUtils;
        import com.huwenkai.com.mobilesafe.Util.ToastUtils;

public class Setup3Activity extends SetupBaseActivity {

    private EditText ed_contact_number;
    private Button bt_select_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();

    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String contact_number = ed_contact_number.getText().toString().trim();

        if(!TextUtils.isEmpty(contact_number)){
            SpUtils.putString(ContactsValues.PHONE_MUNBER,contact_number,getApplicationContext());
            Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else {
            ToastUtils.show(getApplicationContext(),"请输入联系人");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&data!=null){
            String phone_number = data.getStringExtra("phone_number");
            ed_contact_number.setText(phone_number);
        }

    }

    private void initUI() {
        ed_contact_number = (EditText) findViewById(R.id.ed_contact);
        bt_select_contacts = (Button) findViewById(R.id.bt_select_contacts);
        String phone_number = SpUtils.getString(ContactsValues.PHONE_MUNBER, "", getApplicationContext());
        ed_contact_number.setText(phone_number);
        bt_select_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactlistActivity.class);
                startActivityForResult(intent,0);
            }

        });

        //设置下一页的点击事件
        findViewById(R.id.bt_nextpage3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact_number = ed_contact_number.getText().toString().trim();

                if(!TextUtils.isEmpty(contact_number)){
                    SpUtils.putString(ContactsValues.PHONE_MUNBER,contact_number,getApplicationContext());
                Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
                startActivity(intent);
                finish();
                    overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
                }else {
                    ToastUtils.show(getApplicationContext(),"请输入联系人");
                }
            }
        });
        //设置上一页的点击事件
        findViewById(R.id.bt_prepage_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
            }
        });
    }

}
