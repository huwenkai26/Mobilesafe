package com.huwenkai.com.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;

import org.w3c.dom.Text;

/**
 * 用来设置设置中心得布局条目
 * Created by huwenkai on 2016/4/24.
 */
public class SetingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.huwenkai.com.mobilesafe";
    private CheckBox cb_box;
    private TextView tv_dec;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SetingItemView(Context context) {
        this(context, null);
    }

    public SetingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.setting_item_view, this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_dec = (TextView) findViewById(R.id.tv_dec);
        cb_box = (CheckBox) findViewById(R.id.cb_updata);
        ititattrs(attrs);

        tv_title.setText(mDestitle);

    }

    private void ititattrs(AttributeSet attrs) {

        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");


    }

    public boolean ischeck(){
        return cb_box.isChecked();
    }
    public void setcheak(boolean ischeck){
        if(ischeck){
            cb_box.setChecked(ischeck);
            tv_dec.setText(mDesoff);
        }else{
            cb_box.setChecked(ischeck);
            tv_dec.setText(mDeson);
        }
    }
}
