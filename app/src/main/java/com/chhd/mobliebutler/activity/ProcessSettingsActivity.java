package com.chhd.mobliebutler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.service.LockScreenService;
import com.chhd.mobliebutler.util.ServiceUitls;
import com.chhd.mobliebutler.view.MySwitchButton;
import com.chhd.util.SpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ProcessSettingsActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_process)
    RippleView rvProcess;
    @ViewInject(R.id.rv_clear)
    RippleView rvClear;
    @ViewInject(R.id.sb_process)
    MySwitchButton sbProcess;
    @ViewInject(R.id.sb_clear)
    MySwitchButton sbClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_settings);

        ViewUtils.inject(this);

        initView();

        initActionBar();

        setListener();
    }

    private void initView() {
        sbProcess.setChecked(SpUtils.getBoolean(this, KEY_IS_SHOW_SYSTEM_PROCESS, false));

        sbClear.setChecked(ServiceUitls.isRunning(this, "com.chhd.mobliebutler.service.LockScreenService"));
    }

    private void initActionBar() {
        toolbar.setTitle("进程管家设置");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        View.OnClickListener onClickListener = new InnerOnClickListener();
        rvProcess.setOnClickListener(onClickListener);
        rvClear.setOnClickListener(onClickListener);
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rv_process:
                    sbProcess.setChecked(!sbProcess.isChecked());
                    SpUtils.putBoolean(ProcessSettingsActivity.this, KEY_IS_SHOW_SYSTEM_PROCESS, sbProcess.isChecked());
                    break;
                case R.id.rv_clear:
                    sbClear.setChecked(!sbClear.isChecked());
                    Intent intent = new Intent(ProcessSettingsActivity.this, LockScreenService.class);
                    if (sbClear.isChecked()) {
                        startService(intent);
                    } else {
                        stopService(intent);
                    }
                    break;
            }
        }
    }

}
