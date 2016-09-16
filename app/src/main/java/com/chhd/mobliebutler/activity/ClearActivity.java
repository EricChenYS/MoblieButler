package com.chhd.mobliebutler.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.ClearAdapter;
import com.chhd.mobliebutler.biz.ClearBiz;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.entity.Group;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.idunnololz.widgets.AnimatedExpandableListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ClearActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rl_scan_info)
    RelativeLayout rlScanInfo;
    @ViewInject(R.id.progress_bar)
    ProgressBar progressBar;
    @ViewInject(R.id.htv_total_cacheSize)
    HTextView htvTotalCacheSize;
    @ViewInject(R.id.tv_name)
    TextView tvName;
    @ViewInject(R.id.aelv_app)
    AnimatedExpandableListView aelvApp;
    @ViewInject(R.id.rl_scan)
    RelativeLayout rlScan;
    @ViewInject(R.id.rv_scan)
    RippleView rvScan;
    @ViewInject(R.id.rl_clear)
    RelativeLayout rlClear;
    @ViewInject(R.id.rv_clear)
    RippleView rvClear;

    private List<Group> groups = new ArrayList<>();
    private long totalCacheSize;
    private ClearAdapter adpater;
    private Animation btnExitAnim;
    private Animation btnEnterAnim;
    private int progress;
    private int background;
    private Handler handler = new InnerHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        ViewUtils.inject(this);

        initActionBar();

        initData();

        setAdapter();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("手机清理");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        btnEnterAnim = AnimationUtils.loadAnimation(ClearActivity.this, R.anim.set_button_enter);
        btnExitAnim = AnimationUtils.loadAnimation(ClearActivity.this, R.anim.set_button_exit);

        htvTotalCacheSize.animateText(Formatter.formatFileSize(ClearActivity.this, totalCacheSize));

        initGroups();

        Tools.loadColorAnimator(this, rlScanInfo, R.animator.animator_blue2green);
        background = GREEN;

        aelvApp.setGroupIndicator(null);
    }

    private void initGroups() {
        groups.clear();
        groups.add(new Group(KEY_USER_APP, new ArrayList<App>(), 0, 0));
        groups.add(new Group(KEY_SYSTEM_APP, new ArrayList<App>(), 0, 0));
    }

    private void setAdapter() {
        adpater = new ClearAdapter(this, groups);
        aelvApp.setAdapter(adpater);
    }

    private void setListener() {
        aelvApp.setOnGroupClickListener(new InnerOnGroupClickListener());
        RippleView.OnRippleCompleteListener onRippleCompleteListener = new InnerOnRippleCompleteListener();
        rvScan.setOnRippleCompleteListener(onRippleCompleteListener);
        rvClear.setOnRippleCompleteListener(onRippleCompleteListener);
    }

    private void scan() {
        progress = 0;

        rlScan.startAnimation(btnExitAnim);
        rlScan.setVisibility(View.INVISIBLE);

        htvTotalCacheSize.setAnimateType(HTextViewType.EVAPORATE);

        ClearBiz clearBiz = new ClearBiz(ClearActivity.this, handler);
        clearBiz.scan();
        clearBiz.updataTotalDataSiz();
    }

    private void clear() {
        rlClear.startAnimation(btnExitAnim);
        rlClear.setVisibility(View.INVISIBLE);
        rlScan.startAnimation(btnEnterAnim);
        rlScan.setVisibility(View.VISIBLE);

        ClearBiz clearBiz = new ClearBiz(ClearActivity.this, handler);
        clearBiz.clear();

        totalCacheSize = 0;
        htvTotalCacheSize.setAnimateType(HTextViewType.FALL);
        htvTotalCacheSize.animateText(Formatter.formatFileSize(ClearActivity.this, totalCacheSize));

        initGroups();
        adpater.notifyDataSetChanged(groups);
        for (int i = 0; i < groups.size(); i++) {
            aelvApp.collapseGroupWithAnimation(i);
        }

        if (background != GREEN) {
            Tools.loadColorAnimator(this, rlScanInfo, R.animator.animator_red2green);
            background = GREEN;
        }
    }

    private class InnerOnGroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if (aelvApp.isGroupExpanded(groupPosition)) {
                aelvApp.collapseGroupWithAnimation(groupPosition);
            } else {
                aelvApp.expandGroupWithAnimation(groupPosition);
            }
            return true;
        }
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            switch (rippleView.getId()) {
                case R.id.rv_scan:
                    scan();
                    break;
                case R.id.rv_clear:
                    clear();
                    break;
            }
        }
    }
    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PROGRESSBAR_SET_MAX:
                    int max = (int) msg.obj;
                    progressBar.setMax(max);
                    break;
                case MSG_SCANNIG:
                    Bundle data = msg.getData();
                    totalCacheSize = data.getLong(KEY_TOTAL_CACHESIZE);
                    if (totalCacheSize > 0 && background != YELLOW) {
                        Tools.loadColorAnimator(ClearActivity.this, rlScanInfo, R.animator.animator_green2yellow);
                        background = YELLOW;
                    }
                    String name = data.getString(KEY_NAME);
                    tvName.setText("正在扫描: " + name);
                    progress++;
                    progressBar.setProgress(progress);
                    groups = data.getParcelableArrayList(KEY_GROUPS);
                    adpater.notifyDataSetChanged(groups);
                    break;
                case MSG_UPDATE_TOTAL_CACHESIZE:
                    htvTotalCacheSize.animateText(Formatter.formatFileSize(ClearActivity.this, totalCacheSize));
                    break;
                case MSG_SCAN_FINISH:
                    groups = (List<Group>) msg.obj;
                    if (totalCacheSize > 0) {
                        Tools.loadColorAnimator(ClearActivity.this, rlScanInfo, R.animator.animator_yellow2red);
                        background = RED;
                    }
                    htvTotalCacheSize.setAnimateType(HTextViewType.ANVIL);
                    htvTotalCacheSize.animateText("一共发现: " + Formatter.formatFileSize(ClearActivity.this, totalCacheSize));
                    tvName.setText("扫描结束");
                    adpater.notifyDataSetChanged(groups);
                    rlClear.startAnimation(btnEnterAnim);
                    rlClear.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
