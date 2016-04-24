package com.huwenkai.com.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.huwenkai.com.mobilesafe.R;

/**
 * 用来设置设置中心得布局条目
 * Created by huwenkai on 2016/4/24.
 */
public class SetingItemView extends RelativeLayout {
    public SetingItemView(Context context) {
        this(context,null);
    }

    public SetingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SetingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.setting_item_view,this);
    }
}
