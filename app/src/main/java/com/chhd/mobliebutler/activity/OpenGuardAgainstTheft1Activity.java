package com.chhd.mobliebutler.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;
import com.chhd.mobliebutler.view.InputView;
import com.chhd.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class OpenGuardAgainstTheft1Activity extends AppCompatActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.iv_pwd)
    InputView ivPwd;
    @ViewInject(R.id.iv_confirm_pwd)
    InputView ivConfirmPwd;
    @ViewInject(R.id.rv_next)
    RippleView rvNext;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_guard_against_theft1);

        ViewUtils.inject(this);

        initActionBar();

        setListener();

        setReceiver();
    }

    private void initActionBar() {
        toolbar.setTitle("开启防盗");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        rvNext.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
    }

    private void setReceiver() {
        receiver = new InnerBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GUARD_AGAINST_THEFT_FINISH);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            String pwd = ivPwd.getText();
            String confirmPwd = ivConfirmPwd.getText();
            if (!(pwd.length() >= 6 && pwd.length() <= 20)) {
                ivPwd.setText("");
                ivPwd.startAnimation(Tools.getShakeAimation(OpenGuardAgainstTheft1Activity.this));
                ToastUtils.makeText(OpenGuardAgainstTheft1Activity.this, "密码格式不正确, 请输入6—20位字母/数字");
                return;
            }
            if (!confirmPwd.equals(pwd)) {
                ivPwd.startAnimation(Tools.getShakeAimation(OpenGuardAgainstTheft1Activity.this));
                ivConfirmPwd.startAnimation(Tools.getShakeAimation(OpenGuardAgainstTheft1Activity.this));
                ToastUtils.makeText(OpenGuardAgainstTheft1Activity.this, "二次密码不一样");
                return;
            }
            Intent intent = new Intent(OpenGuardAgainstTheft1Activity.this, OpenGuardAgainstTheft2Activity.class);
            intent.putExtra(KEY_GUARD_AGAINST_THEFT_PASSWORD, pwd);
            startActivity(intent);
            overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
        }
    }

    private class InnerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}
