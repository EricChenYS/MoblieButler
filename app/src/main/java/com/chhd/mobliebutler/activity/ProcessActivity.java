package com.chhd.mobliebutler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.ProcessAdapter;
import com.chhd.mobliebutler.biz.ProcessBiz;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.global.Consts;
import com.dd.CircularProgressButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.tv_dirty_mem)
    TextView tvDirtyMem;
    @ViewInject(R.id.tv_avail_mem)
    TextView tvAvailMem;
    @ViewInject(R.id.lv_process)
    ListView lvProcess;
    @ViewInject(R.id.loading_indicator_view)
    AVLoadingIndicatorView loadingIndicatorView;
    @ViewInject(R.id.rv_clear)
    RippleView rvClear;
    @ViewInject(R.id.cpb_clear)
    CircularProgressButton cpbClear;

    private Handler handler = new InnerHandler();
    private List<App> userApps = new ArrayList<>();
    private List<App> systemApps = new ArrayList<>();
    private ProcessAdapter adapter;
    private long dirtyMem;
    private long availMem;
    private String totalMemStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ViewUtils.inject(this);

        initActionBar();

        initData();

        setAdapter();

        setListeners();
    }

    private void initActionBar() {
        toolbar.setTitle("进程管家");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        ProcessBiz processBiz = new ProcessBiz(this, handler);
        availMem = processBiz.getAvailMem();
        String availMemStr = Formatter.formatFileSize(this, availMem);
        long totalMem = processBiz.getTotalMem();
        totalMemStr = Formatter.formatFileSize(this, totalMem);
        tvAvailMem.setText("剩余内存: " + availMemStr + " / " + totalMemStr);
        processBiz.getRunAppProcessInfos();
    }

    private void setAdapter() {
        adapter = new ProcessAdapter(ProcessActivity.this, userApps, systemApps);
        lvProcess.setAdapter(adapter);
    }

    private void setListeners() {
        rvClear.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.process, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(ProcessActivity.this, ProcessSettingsActivity.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_RUN_APP_PROCESS_INFOS:
                    Bundle data = msg.getData();
                    dirtyMem = data.getLong(KEY_USER_APPS_MEM);
                    tvDirtyMem.setText(Formatter.formatFileSize(ProcessActivity.this, dirtyMem) + " 可清理");
                    userApps.clear();
                    systemApps.clear();
                    userApps.addAll((List<App>) data.getSerializable(KEY_USER_APP));
                    systemApps.addAll((List<App>) data.getSerializable(KEY_SYSTEM_APP));
                    adapter.notifyDataSetChanged();
                    loadingIndicatorView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            if (cpbClear.getProgress() == CircularProgressButton.IDLE_STATE_PROGRESS) {
                List<App> killApps = new ArrayList<>();
                long mem = 0;
                for (App app : userApps) {
                    if (app.isCheck()) {
                        killApps.add(app);
                    }
                }
                for (App app : systemApps) {
                    if (app.isCheck()) {
                        killApps.add(app);
                    }
                }
                for (App app : killApps) {

                    if (userApps.contains(app)) {
                        userApps.remove(app);
                        mem += app.getMemSize();
                    } else if (systemApps.contains(app)) {
                        systemApps.remove(app);
                    }

                    ProcessBiz processBiz = new ProcessBiz(ProcessActivity.this, handler);
                    processBiz.killBackgroundProcess(app.getPackageName());
                }

                tvDirtyMem.setText(Formatter.formatFileSize(ProcessActivity.this, dirtyMem - mem) + " 可清理");
                tvAvailMem.setText("剩余内存: " + Formatter.formatFileSize(ProcessActivity.this, availMem + mem) + " / " + totalMemStr);

                adapter.notifyDataSetChanged();
                cpbClear.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
            } else if (cpbClear.getProgress() == CircularProgressButton.SUCCESS_STATE_PROGRESS) {
                cpbClear.setProgress(CircularProgressButton.IDLE_STATE_PROGRESS);
            }
        }
    }
}
