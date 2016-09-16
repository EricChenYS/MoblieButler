package com.chhd.mobliebutler.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class GuardAgainstTheftActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_open)
    RippleView rvOpen;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_against_theft);

        ViewUtils.inject(this);

        initActionBar();

        setListener();

        setReceiver();
    }

    private void initActionBar() {
        toolbar.setTitle("手机防盗");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        rvOpen.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
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

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            Intent intent = new Intent(GuardAgainstTheftActivity.this, OpenGuardAgainstTheft1Activity.class);
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
