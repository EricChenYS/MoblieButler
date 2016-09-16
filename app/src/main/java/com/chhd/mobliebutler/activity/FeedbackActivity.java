package com.chhd.mobliebutler.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.util.Tools;
import com.chhd.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class FeedbackActivity extends BaseActivity {

    static {
        System.loadLibrary("feedback");
    }

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_submit)
    RippleView rvSubmit;
    @ViewInject(R.id.et_content)
    EditText etContent;
    @ViewInject(R.id.et_contact)
    EditText etContact;
    @ViewInject(R.id.ib_content_clear)
    ImageButton ibContentClear;
    @ViewInject(R.id.ib_contact_clear)
    ImageButton ibContactClear;
    @ViewInject(R.id.rl_content)
    RelativeLayout rlContent;
    private String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ViewUtils.inject(this);

        initActionBar();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("意见反馈");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        rvSubmit.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
        View.OnClickListener onClickListener = new InnerOnClickListener();
        ibContentClear.setOnClickListener(onClickListener);
        ibContactClear.setOnClickListener(onClickListener);
        TextWatcher textWatcher = new InnerTextWatcher();
        etContent.addTextChangedListener(textWatcher);
        etContact.addTextChangedListener(textWatcher);
    }

    public String getAppName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            return appName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public native String getMyNumber();

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            final String content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                rlContent.startAnimation(Tools.getShakeAimation(FeedbackActivity.this));
                return;
            }
            contact = etContact.getText().toString();
            if (!TextUtils.isEmpty(contact)) {
                String emailRegex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
                String mobliePhoneRegex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
                if (contact.matches(emailRegex) || contact.matches(mobliePhoneRegex)) {

                } else {
                    ToastUtils.makeText(FeedbackActivity.this, "手机号码 或 邮箱格式不正确");
                    etContact.startAnimation(Tools.getShakeAimation(FeedbackActivity.this));
                    return;
                }
            } else {
                contact = "none";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
            builder.setTitle("温馨提醒");
            builder.setMessage("由此产生的短信费用按运营商正常收取喔");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SmsManager smsManager = SmsManager.getDefault();
                    String text = "app: " + getAppName() + "\n\n" + "content: " + content + "\n\n" + "contact: " + contact;
                    ArrayList<String> messages = smsManager.divideMessage(text);
                    for (String message : messages) {
                        smsManager.sendTextMessage(getMyNumber(), null, message, null, null);
                    }
                    ToastUtils.makeText(FeedbackActivity.this, "发送成功");
                    ibContactClear.setVisibility(View.INVISIBLE);
                    etContent.setText("");
                    etContact.setText("");
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_content_clear:
                    etContent.setText("");
                    break;
                case R.id.ib_contact_clear:
                    etContact.setText("");
                    break;
            }
        }
    }

    private class InnerTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etContent.hasFocus()) {
                if (!TextUtils.isEmpty(s)) {
                    ibContentClear.setVisibility(View.VISIBLE);
                } else {
                    ibContentClear.setVisibility(View.INVISIBLE);
                }
            } else if (etContact.hasFocus()) {
                if (!TextUtils.isEmpty(s)) {
                    ibContactClear.setVisibility(View.VISIBLE);
                } else {
                    ibContactClear.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
