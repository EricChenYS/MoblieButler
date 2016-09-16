package com.chhd.mobliebutler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.ContactAdapter;
import com.chhd.mobliebutler.biz.ContactBiz;
import com.chhd.mobliebutler.entity.Contact;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;
import com.chhd.mobliebutler.view.SideBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactActivity extends BaseActivity implements Consts {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.rv_input)
    RippleView rvInput;
    @ViewInject(R.id.tv_number)
    TextView tvNumber;
    @ViewInject(R.id.rv_cancel)
    RippleView rvCancel;
    @ViewInject(R.id.rv_confirm)
    RippleView rvConfirm;
    @ViewInject(R.id.et_number)
    EditText etNumber;
    @ViewInject(R.id.lv_contact)
    ListView lvContact;
    @ViewInject(R.id.side_bar)
    SideBar sideBar;
    @ViewInject(R.id.tv_dialog)
    TextView tvDialog;
    @ViewInject(R.id.tv_empty)
    TextView tvEmpty;

    private AlertDialog dialog;
    private Handler hanlder = new InnerHandler();
    private List<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ViewUtils.inject(this);

        initActionBar();

        initView();

        initData();

        initDialog();

        setListener();

        setAdapter();

    }

    private void initActionBar() {
        toolbar.setTitle("从联系人添加");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00379C"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        sideBar.setTextView(tvDialog);
    }

    private void initData() {
        ContactBiz contactBiz = new ContactBiz(this, hanlder);
        contactBiz.getContacts();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_input_number, null);
        ViewUtils.inject(this, view);
        builder.setView(view);
        dialog = builder.create();
    }

    private void setListener() {
        RippleView.OnRippleCompleteListener onRippleCompleteListener = new InnerOnRippleCompleteListener();
        rvInput.setOnRippleCompleteListener(onRippleCompleteListener);
        rvCancel.setOnRippleCompleteListener(onRippleCompleteListener);
        rvConfirm.setOnRippleCompleteListener(onRippleCompleteListener);
        sideBar.setOnTouchingLetterChangedListener(new InnerOnTouchingLetterChangedListener());
    }

    private void setAdapter() {
        adapter = new ContactAdapter(this, contacts);
        lvContact.setAdapter(adapter);
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {
        @Override
        public void onComplete(RippleView rippleView) {
            switch (rippleView.getId()) {
                case R.id.rv_input:
                    dialog.show();
                    break;
                case R.id.rv_cancel:
                    dialog.dismiss();
                    break;
                case R.id.rv_confirm:
                    String number = etNumber.getText().toString();
                    if (TextUtils.isEmpty(number)) {
                        etNumber.startAnimation(Tools.getShakeAimation(ContactActivity.this));
                        return;
                    }
                    dialog.dismiss();
                    Intent data = new Intent();
                    data.putExtra(KEY_NUMBER, number);
                    setResult(0, data);
                    finish();
                    break;
            }
        }
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_CONTACTS:
                    contacts.addAll((Collection<? extends Contact>) msg.obj);
                    if (contacts.size() == 0) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private class InnerOnTouchingLetterChangedListener implements SideBar.OnTouchingLetterChangedListener {
        @Override
        public void onTouchingLetterChanged(String s) {
            int position = adapter.getPositionForSection(s.charAt(0));
            lvContact.setSelection(position);
        }
    }
}
