package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by huwenkai on 2016/4/26.
 */
public class Testactivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TextView(this));
    }
}
