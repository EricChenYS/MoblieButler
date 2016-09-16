package com.chhd.mobliebutler.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.dao.BlacklistDao;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;
import com.chhd.mobliebutler.view.InputView;
import com.chhd.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ByhandActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.ll_sms)
    LinearLayout llSms;
    @ViewInject(R.id.ll_tel)
    LinearLayout llTel;
    @ViewInject(R.id.cb_sms)
    CheckBox cbSms;
    @ViewInject(R.id.cb_tel)
    CheckBox cbTel;
    @ViewInject(R.id.iv_number)
    InputView ivNumber;
    @ViewInject(R.id.iv_name)
    InputView ivName;
    @ViewInject(R.id.rv_save)
    RippleView rvSave;
    @ViewInject(R.id.ll_mode)
    LinearLayout llMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_byhand);

        ViewUtils.inject(this);

        initActionBar();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("手工输入号码");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        View.OnClickListener onClickListener = new InnerOnClickListener();
        llSms.setOnClickListener(onClickListener);
        llTel.setOnClickListener(onClickListener);
        rvSave.setOnRippleCompleteListener(new InnerOnRippleCompleteListener());
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_sms:
                    cbSms.setChecked(!cbSms.isChecked());
                    break;
                case R.id.ll_tel:
                    cbTel.setChecked(!cbTel.isChecked());
                    break;
            }
        }
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            String number = ivNumber.getText();
            if (TextUtils.isEmpty(number)) {
                ivNumber.startAnimation(Tools.getShakeAimation(ByhandActivity.this));
                return;
            }
            String name = ivName.getText();
            int mode = MODE_NONE;
            if (cbSms.isChecked() && !cbTel.isChecked()) {
                mode = MODE_SMS;
            } else if (!cbSms.isChecked() && cbTel.isChecked()) {
                mode = MODE_TEL;
            } else if (cbSms.isChecked() && cbTel.isChecked()) {
                mode = MODE_ALL;
            }
            if (mode == MODE_NONE) {
                llMode.startAnimation(Tools.getShakeAimation(ByhandActivity.this));
                return;
            }
            BlacklistDao dao = new BlacklistDao(ByhandActivity.this);
            long id = dao.insert(number, name, mode);
            if (id != -1) {
                ivNumber.setText("");
                ivName.setText("");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.makeText(ByhandActivity.this, "添加失败，号码重复");
                ivNumber.startAnimation(Tools.getShakeAimation(ByhandActivity.this));
            }
        }
    }
}
