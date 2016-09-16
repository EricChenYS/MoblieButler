package com.chhd.mobliebutler.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.AppManagerAdapter;
import com.chhd.mobliebutler.biz.AppManagerBiz;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.fragment.SystemAppFragment;
import com.chhd.mobliebutler.fragment.UserAppFragment;
import com.chhd.mobliebutler.global.Consts;
import com.flyco.tablayout.SlidingTabLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppManagerActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.sliding_tab_layout)
    SlidingTabLayout slidingTabLayout;
    @ViewInject(R.id.view_pager)
    ViewPager viewPager;
    @ViewInject(R.id.loading_indicator_view)
    AVLoadingIndicatorView loadingIndicatorView;

    private String[] titles = new String[]{"用户软件", "系统软件"};
    private List<Fragment> fragments = new ArrayList<>();
    private Handler handler = new InnerHandler();
    private List<App> userApps = new ArrayList<>();
    private List<App> systemApps = new ArrayList<>();
    private UserAppFragment userAppFragment;
    private SystemAppFragment systemAppFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ViewUtils.inject(this);

        initActionBar();

        initData();

        setAdapter();
    }

    private void initActionBar() {
        toolbar.setTitle("软件管家");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        userAppFragment = new UserAppFragment();
        systemAppFragment = new SystemAppFragment();
        fragments.add(userAppFragment);
        fragments.add(systemAppFragment);

        AppManagerBiz appManagerBiz = new AppManagerBiz(this, handler);
        appManagerBiz.getAllApp();
    }

    private void setAdapter() {
        viewPager.setAdapter(new AppManagerAdapter(getSupportFragmentManager(), titles, fragments));
        slidingTabLayout.setViewPager(viewPager);
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_ALL_APPS:
                    Bundle data = msg.getData();
                    userApps.addAll((Collection<? extends App>) data.getSerializable(KEY_USER_APP));
                    systemApps.addAll((Collection<? extends App>) data.getSerializable(KEY_SYSTEM_APP));
                    userAppFragment.updateListView(userApps);
                    systemAppFragment.updateListView(systemApps);
                    loadingIndicatorView.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
