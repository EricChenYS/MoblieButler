package com.chhd.mobliebutler.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.DensityUtils;

/**
 * Created by CWQ on 2016/8/27.
 */
public class InputView extends RelativeLayout implements Consts {

    private EditText editText;
    private ImageButton ibClear;

    private View view;

    public InputView(Context context) {
        this(context, null);
    }

    public InputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        view = View.inflate(context, R.layout.view_input, this);

        initView(context, attrs);

        setListener();
    }

    private void initView(Context context, AttributeSet attrs) {
        editText = (EditText) view.findViewById(R.id.edit_text);
        ibClear = (ImageButton) view.findViewById(R.id.ib_clear);

        int resId = attrs.getAttributeResourceValue(NAMESPACE, "drawableLeft", 0);
        if (resId != 0) {
            Drawable drawableLeft = ContextCompat.getDrawable(context, resId);
            drawableLeft.setBounds(0, 0, DensityUtils.dp2px(context, 18), DensityUtils.dp2px(context, 18));
            editText.setCompoundDrawables(drawableLeft, null, null, null);
        }

        String hint = attrs.getAttributeValue(NAMESPACE, "hint");
        if (hint != null) {
            editText.setHint(hint);
        }

        String type = attrs.getAttributeValue(NAMESPACE, "type");
        if ("textPassword".equals(type)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void setListener() {
        editText.addTextChangedListener(new InnerTextWatcher());
        ibClear.setOnClickListener(new InnerOnClickListener());
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    private class InnerTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                ibClear.setVisibility(View.VISIBLE);
            } else {
                ibClear.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class InnerOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            editText.setText("");
        }
    }
}
