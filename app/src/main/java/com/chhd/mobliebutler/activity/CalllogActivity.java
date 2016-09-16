package com.chhd.mobliebutler.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.CalllogAdapter;
import com.chhd.mobliebutler.biz.CalllogBiz;
import com.chhd.mobliebutler.dao.BlacklistDao;
import com.chhd.mobliebutler.entity.Calllog;
import com.chhd.mobliebutler.global.Consts;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CalllogActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.lv_calllog)
    ListView lvCalllog;
    @ViewInject(R.id.rv_confirm)
    RippleView rvConfirm;

    private Handler handler = new InnerHandler();
    private CalllogBiz biz;
    private List<Calllog> calllogs = new ArrayList<>();
    private CalllogAdapter adapter;
    private BlacklistDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog);

        ViewUtils.inject(this);

        initActionBar();

        initData();

        setAdapter();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("从通话记录添加");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        biz = new CalllogBiz(this, handler);
        biz.getCalllogs();
        dao = new BlacklistDao(this);
    }

    private void setAdapter() {
        adapter = new CalllogAdapter(this, calllogs);
        lvCalllog.setAdapter(adapter);
    }

    private void setListener() {
        rvConfirm.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            for (Calllog calllog : calllogs) {
                if (calllog.isCheck()) {
                    dao.insert(calllog.getName(), calllog.getNumber(), MODE_ALL);
                }
            }
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.set_aty_exit_aty_enter, R.anim.set_aty_exit_aty_exit);
        }
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_CALLLOGS:
                    calllogs.clear();
                    calllogs.addAll((Collection<? extends Calllog>) msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}
