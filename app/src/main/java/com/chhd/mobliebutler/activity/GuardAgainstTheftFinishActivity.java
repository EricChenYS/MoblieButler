package com.chhd.mobliebutler.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.view.MySwitchButton;
import com.chhd.util.SpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class GuardAgainstTheftFinishActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_number)
    RippleView rvNumber;
    @ViewInject(R.id.rv_open)
    RippleView rvOpen;
    @ViewInject(R.id.rv_reset)
    RippleView rvReset;
    @ViewInject(R.id.rv_send)
    RippleView rvSend;
    @ViewInject(R.id.switch_button)
    MySwitchButton switchButton;
    @ViewInject(R.id.tv_number)
    TextView tvNumber;

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_against_theft_finish);

        ViewUtils.inject(this);

        initData();

        initActionBar();

        setListener();
    }

    private void initData() {
        number = SpUtils.getString(this, KEY_NUMBER, null);
        tvNumber.setText(number);
        switchButton.setChecked(SpUtils.getBoolean(this, KEY_IS_OPEN_GUARD_AGAINST_THEFT, false));
    }

    private void initActionBar() {
        toolbar.setTitle("防盗中心");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        RippleView.OnRippleCompleteListener onRippleCompleteListener = new InnerOnRippleCompleteListener();
        rvNumber.setOnRippleCompleteListener(onRippleCompleteListener);
        rvReset.setOnRippleCompleteListener(onRippleCompleteListener);
        rvSend.setOnRippleCompleteListener(onRippleCompleteListener);
        rvOpen.setOnClickListener(new InnerOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            number = data.getStringExtra(KEY_NUMBER);
            tvNumber.setText(number);
            SpUtils.putString(this, KEY_NUMBER, number);
        }
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            AlertDialog.Builder builder = null;
            switch (rippleView.getId()) {
                case R.id.rv_number:
                    Intent intent = new Intent(GuardAgainstTheftFinishActivity.this, ContactActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case R.id.rv_reset:
                    builder = new AlertDialog.Builder(GuardAgainstTheftFinishActivity.this);
                    builder.setTitle("重新设置");
                    builder.setMessage("是否重新设置手机防盗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SpUtils.putString(GuardAgainstTheftFinishActivity.this, KEY_SIM_SERIAL_NUMBER, null);
                            SpUtils.putString(GuardAgainstTheftFinishActivity.this, KEY_GUARD_AGAINST_THEFT_PWD, null);
                            SpUtils.putString(GuardAgainstTheftFinishActivity.this, KEY_NUMBER, null);
                            SpUtils.putBoolean(GuardAgainstTheftFinishActivity.this, KEY_IS_OPEN_GUARD_AGAINST_THEFT, false);
                            number = null;
                            tvNumber.setText("");
                            switchButton.setChecked(false);
                            Intent intent = new Intent(GuardAgainstTheftFinishActivity.this, GuardAgainstTheftActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                    break;
                case R.id.rv_send:
                    builder = new AlertDialog.Builder(GuardAgainstTheftFinishActivity.this);
                    builder.setTitle("备忘防盗指令");
                    builder.setMessage("是否把防盗指令通过短信发给亲友号码？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String text = "指令包括：\\n\\n报警提醒：#*alarm*#\\n\\n远程定位：#*location*#\\n\\n锁定手机：#*lock*#\\n\\n清空数据：#*clear*#";
                            SmsManager smsManager = SmsManager.getDefault();
                            ArrayList<String> messages = smsManager.divideMessage(text);
                            for (String message : messages) {
                                smsManager.sendTextMessage(number, null, message, null, null);
                            }
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                    break;
            }
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switchButton.setChecked(!switchButton.isChecked());
            SpUtils.putBoolean(GuardAgainstTheftFinishActivity.this, KEY_IS_OPEN_GUARD_AGAINST_THEFT, switchButton.isChecked());
        }
    }
}
