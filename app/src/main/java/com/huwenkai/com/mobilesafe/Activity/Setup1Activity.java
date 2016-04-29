package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.huwenkai.com.mobilesafe.R;

/**
 * 设置向导界面
 */
public class Setup1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setup1);
        findViewById(R.id.bt_nextpage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
                startActivity(intent);
             overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
                finish();
            }
        });
    }
}
