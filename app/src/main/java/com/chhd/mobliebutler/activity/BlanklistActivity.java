package com.chhd.mobliebutler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.BlacklistAdapter;
import com.chhd.mobliebutler.dao.BlacklistDao;
import com.chhd.mobliebutler.entity.Blacklist;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class BlanklistActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_add)
    RippleView rvAdd;
    @ViewInject(R.id.rv_sms)
    RippleView rvSms;
    @ViewInject(R.id.rv_calllog)
    RippleView rvCalllog;
    @ViewInject(R.id.rv_contact)
    RippleView rvContact;
    @ViewInject(R.id.rv_byhand)
    RippleView rvByhand;
    @ViewInject(R.id.lv_blacklist)
    ListView lvBlacklist;
    @ViewInject(R.id.ll_empty)
    LinearLayout llEmpty;

    private AlertDialog dialog;
    private List<Blacklist> blacklists = new ArrayList<>();
    private Handler handler = new InnerHandler();
    private BlacklistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blanklist);

        ViewUtils.inject(this);

        initActionBar();

        initData();

        setAdapter();

        initDialog();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("骚扰拦截");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BlacklistDao dao = new BlacklistDao(BlanklistActivity.this);
                blacklists.clear();
                blacklists.addAll(dao.query());
                handler.sendEmptyMessage(MSG_GET_BLACKLISTS);
            }
        }.start();
    }

    private void setAdapter() {
        adapter = new BlacklistAdapter(this, blacklists);
        lvBlacklist.setAdapter(adapter);
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_add_blacklist, null);
        ViewUtils.inject(this, view);
        dialog.setView(view);
    }


    private void setListener() {
        RippleView.OnRippleCompleteListener onRippleCompleteListener = new InnerOnRippleCompleteListener();
        rvAdd.setOnRippleCompleteListener(onRippleCompleteListener);
        rvSms.setOnRippleCompleteListener(onRippleCompleteListener);
        rvCalllog.setOnRippleCompleteListener(onRippleCompleteListener);
        rvContact.setOnRippleCompleteListener(onRippleCompleteListener);
        rvByhand.setOnRippleCompleteListener(onRippleCompleteListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blacklist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            Intent intent = null;
            switch (rippleView.getId()) {
                case R.id.rv_add:
                    dialog.show();
                    break;
                case R.id.rv_sms:
                    ToastUtils.makeText(BlanklistActivity.this, "抱歉，暂不开放此功能");
                    dialog.dismiss();
                    break;
                case R.id.rv_calllog:
                    intent = new Intent(BlanklistActivity.this, CalllogActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    dialog.dismiss();
                    break;
                case R.id.rv_contact:
                    ToastUtils.makeText(BlanklistActivity.this, "抱歉，暂不开放此功能");
                    dialog.dismiss();
                    break;
                case R.id.rv_byhand:
                    intent = new Intent(BlanklistActivity.this, ByhandActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    dialog.dismiss();
                    break;
            }
        }
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_BLACKLISTS:
                    if (blacklists.size() != 0) {
                        adapter.notifyDataSetChanged();
                        llEmpty.setVisibility(View.GONE);
                    } else {
                        llEmpty.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}
