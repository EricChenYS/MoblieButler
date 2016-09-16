package com.chhd.mobliebutler.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.VirusAdapter;
import com.chhd.mobliebutler.biz.AntiVirusBiz;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;
import com.dd.CircularProgressButton;
import com.hanks.htextview.HTextView;
import com.john.waveview.WaveView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AntiVirusActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.wave_view)
    WaveView waveView;
    @ViewInject(R.id.htv_scan_num)
    HTextView htvScanNum;
    @ViewInject(R.id.lv_virus)
    ListView lvVirus;
    @ViewInject(R.id.rv_scan)
    RippleView rvScan;
    @ViewInject(R.id.cpb_scan)
    CircularProgressButton cpbScan;

    private List<App> apps = new ArrayList<>();
    private VirusAdapter adapter;
    private int background;
    private int num;
    private List<App> dangerApps;
    private BroadcastReceiver receiver;
    private List<App> safeApps;
    private Handler handler = new InnerHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);

        ViewUtils.inject(this);

        initActionBar();

        initData();

        setAdapter();

        setListeners();

        setReceiver();
    }

    private void initActionBar() {
        toolbar.setTitle("手机杀毒");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        Tools.loadColorAnimator(this, waveView, R.animator.animator_blue2green);
        background = GREEN;

        htvScanNum.animateText("共扫描 0 项");

        cpbScan.setIndeterminateProgressMode(true);
    }

    private void setAdapter() {
        adapter = new VirusAdapter(this, apps);
        lvVirus.setAdapter(adapter);
    }

    private void setListeners() {
        rvScan.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
    }

    private void setReceiver() {
        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
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
            switch (cpbScan.getProgress()) {
                case CircularProgressButton.IDLE_STATE_PROGRESS:
                    num = 0;
                    cpbScan.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

                    AntiVirusBiz antiVirusBiz = new AntiVirusBiz(AntiVirusActivity.this, handler);
                    antiVirusBiz.initWaveView(waveView.getProgress(), 0);
                    antiVirusBiz.scan();
                    break;
                case CircularProgressButton.ERROR_STATE_PROGRESS:
                    for (App app : dangerApps) {
                        Intent intent = new Intent("android.intent.action.DELETE");
                        intent.setData(Uri.parse("package:" + app.getPackageName()));
                        startActivity(intent);
                    }
                    break;
                case CircularProgressButton.SUCCESS_STATE_PROGRESS:
                    cpbScan.setProgress(CircularProgressButton.IDLE_STATE_PROGRESS);
                    break;
            }
        }
    }


    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<App> deleteApps = new ArrayList<>();
            String dataString = intent.getDataString();
            String packageName = dataString.split(":")[1];
            for (App app : dangerApps) {
                if (app.getPackageName().equals(packageName)) {
                    deleteApps.add(app);
                }
            }
            for (App app : deleteApps) {
                dangerApps.remove(app);
            }
            if (dangerApps.isEmpty()) {
                apps.clear();
                apps.addAll(dangerApps);
                apps.addAll(safeApps);
                adapter.notifyDataSetChanged();
                cpbScan.setProgress(CircularProgressButton.IDLE_STATE_PROGRESS);
                Tools.loadColorAnimator(AntiVirusActivity.this, waveView, R.animator.animator_red2green);
                background = GREEN;
            }
        }
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WAVE_VIEW_SET_PROGRESS:
                    int progress = msg.arg1;
                    waveView.setProgress(progress);
                    break;
                case MSG_WAVE_VIEW_SET_MAX:
                    int max = msg.arg1;
                    waveView.setMax(max);
                    break;
                case MSG_SCANNIG:
                    App app = (App) msg.obj;
                    apps.add(0, app);
                    adapter.notifyDataSetChanged();
                    num++;
                    htvScanNum.animateText("共扫描 " + num + " 项");
                    break;
                case MSG_FIND_VIRUS:
                    if (background != RED) {
                        Tools.loadColorAnimator(AntiVirusActivity.this, waveView, R.animator.animator_green2red);
                        background = RED;
                    }
                    break;
                case MSG_SCAN_FINISH:
                    Bundle data = msg.getData();
                    dangerApps = (List<App>) data.getSerializable(KEY_DANGER_APPS);
                    safeApps = (List<App>) data.getSerializable(KEY_SAFE_APPS);
                    apps.clear();
                    apps.addAll(dangerApps);
                    apps.addAll(safeApps);
                    adapter.notifyDataSetChanged();
                    AntiVirusBiz antiVirusBiz = new AntiVirusBiz(AntiVirusActivity.this, this);
                    antiVirusBiz.initWaveView(waveView.getProgress(), waveView.getProgress() / 2);
                    if (background == RED) {
                        cpbScan.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
                    } else {
                        cpbScan.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
                    }
                    break;
            }
        }
    }
}
