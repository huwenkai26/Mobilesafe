package com.huwenkai.com.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;

/**
 * 用来显示来电归属地的风格的
 * Created by huwenkai on 2016/4/24.
 */
public class AddressStyleView extends RelativeLayout {

    private TextView tv_dec;

    private TextView tv_title;


    public AddressStyleView(Context context) {
        this(context, null);
    }

    public AddressStyleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddressStyleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.address_style_view, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_dec = (TextView) findViewById(R.id.tv_dec);



    }
    public  void setTitle(String titleName){
        tv_title.setText(titleName);
    }
    public  void setDec(String dec){
        tv_dec.setText(dec);
    }
}