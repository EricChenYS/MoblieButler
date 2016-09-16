package com.chhd.mobliebutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kyleduo.switchbutton.SwitchButton;

/**
 * Created by CWQ on 2016/8/29.
 */
public class MySwitchButton extends SwitchButton {

    public MySwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MySwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwitchButton(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
