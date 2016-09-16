package com.chhd.mobliebutler.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.SpUtils;
import com.chhd.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class OpenGuardAgainstTheft2Activity extends AppCompatActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rl_detail)
    RelativeLayout rlDetail;
    @ViewInject(R.id.tv_detail)
    TextView tvDetail;
    @ViewInject(R.id.tv_instruction)
    TextView tvInstruction;
    @ViewInject(R.id.rv_contact)
    RippleView rvContact;
    @ViewInject(R.id.tv_number)
    TextView tvNumber;
    @ViewInject(R.id.rv_finish)
    RippleView rvFinish;
    @ViewInject(R.id.check_box)
    CheckBox checkBox;

    private boolean isOpen;
    private int tvInstructionHeight;
    private LinearLayout.LayoutParams params;
    private String number;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_guard_against_theft2);

        ViewUtils.inject(this);

        initActionBar();

        initData();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("开启防盗");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        pwd = getIntent().getStringExtra(KEY_GUARD_AGAINST_THEFT_PASSWORD);
    }

    private void setListener() {
        tvInstruction.getViewTreeObserver().addOnGlobalLayoutListener(new InnerOnGlobalLayoutListener());
        rlDetail.setOnClickListener(new InnerOnClickListener());
        RippleView.OnRippleCompleteListener onRippleCompleteListener = new InnerOnRippleCompleteListener();
        rvContact.setOnRippleCompleteListener(onRippleCompleteListener);
        rvFinish.setOnRippleCompleteListener(onRippleCompleteListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.set_left2right_enter, R.anim.set_left2right_exit);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.set_left2right_enter, R.anim.set_left2right_exit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            number = data.getStringExtra(KEY_NUMBER);
            tvNumber.setText(number);
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ValueAnimator valueAnimator = null;
            if (isOpen) {
                isOpen = false;
                valueAnimator = new ValueAnimator().ofInt(tvInstructionHeight, 0);
                tvDetail.setText("查看详情");
            } else {
                isOpen = true;
                valueAnimator = new ValueAnimator().ofInt(0, tvInstructionHeight);
                tvDetail.setText("收起");
            }
            valueAnimator.addUpdateListener(new InnerAnimatorUpdateListener());
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }
    }

    private class InnerOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            tvInstruction.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            tvInstructionHeight = tvInstruction.getHeight();
            params = (LinearLayout.LayoutParams) tvInstruction.getLayoutParams();
            params.height = 0;
            tvInstruction.setLayoutParams(params);
        }
    }

    private class InnerAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int height = (int) valueAnimator.getAnimatedValue();
            params.height = height;
            tvInstruction.setLayoutParams(params);
        }
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            Intent intent = null;
            switch (rippleView.getId()) {
                case R.id.rv_contact:
                    intent = new Intent(OpenGuardAgainstTheft2Activity.this, ContactActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case R.id.rv_finish:
                    if (TextUtils.isEmpty(number)) {
                        ToastUtils.makeText(OpenGuardAgainstTheft2Activity.this, "请选择亲友号码");
                        return;
                    }

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();

                    SpUtils.putString(OpenGuardAgainstTheft2Activity.this, KEY_SIM_SERIAL_NUMBER, simSerialNumber);
                    SpUtils.putString(OpenGuardAgainstTheft2Activity.this, KEY_GUARD_AGAINST_THEFT_PWD, pwd);
                    SpUtils.putString(OpenGuardAgainstTheft2Activity.this, KEY_NUMBER, number);
                    SpUtils.putBoolean(OpenGuardAgainstTheft2Activity.this, KEY_IS_OPEN_GUARD_AGAINST_THEFT, true);

                    if (checkBox.isChecked()) {
                        String text = "指令包括：\\n\\n报警提醒：#*alarm*#\\n\\n远程定位：#*location*#\\n\\n锁定手机：#*lock*#\\n\\n清空数据：#*clear*#";
                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> messages = smsManager.divideMessage(text);
                        for (String message : messages) {
                            smsManager.sendTextMessage(number, null, message, null, null);
                        }
                    }

                    intent = new Intent(ACTION_GUARD_AGAINST_THEFT_FINISH);
                    sendBroadcast(intent);

                    intent = new Intent(OpenGuardAgainstTheft2Activity.this, GuardAgainstTheftFinishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
                    finish();
                    break;
            }
        }
    }
}
