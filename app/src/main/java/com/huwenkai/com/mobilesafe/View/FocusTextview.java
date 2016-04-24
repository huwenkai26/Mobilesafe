package com.huwenkai.com.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**一个自定义可以带焦点textview
 * Created by huwenkai on 2016/4/24.
 */

public class FocusTextview extends TextView{
    public FocusTextview(Context context) {
        super(context);
    }

    public FocusTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
