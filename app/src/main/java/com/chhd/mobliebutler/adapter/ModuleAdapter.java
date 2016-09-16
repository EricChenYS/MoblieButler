package com.chhd.mobliebutler.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.adapter.MyBaseAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.activity.AntiVirusActivity;
import com.chhd.mobliebutler.activity.AppManagerActivity;
import com.chhd.mobliebutler.activity.BlanklistActivity;
import com.chhd.mobliebutler.activity.ClearActivity;
import com.chhd.mobliebutler.activity.GuardAgainstTheftActivity;
import com.chhd.mobliebutler.activity.GuardAgainstTheftFinishActivity;
import com.chhd.mobliebutler.activity.ProcessActivity;
import com.chhd.mobliebutler.entity.Module;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;
import com.chhd.util.SpUtils;
import com.chhd.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/8/25.
 */
public class ModuleAdapter extends MyBaseAdapter<Module> implements Consts {

    @ViewInject(R.id.rv_cancel)
    RippleView rvCancel;
    @ViewInject(R.id.rv_confirm)
    RippleView rvConfirm;
    @ViewInject(R.id.et_pwd)
    EditText etPwd;

    private AlertDialog dialog;

    public ModuleAdapter(Context context, List<Module> data) {
        super(context, data);

        initDialog();
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(getContext()).create();
        View view = View.inflate(getContext(), R.layout.dialog_guard_against_theft_pwd, null);
        ViewUtils.inject(this, view);
        dialog.setView(view);
        RippleView.OnRippleCompleteListener onRippleCompleteListener = new InnerOnRippleCompleteListener(-1);
        rvCancel.setOnRippleCompleteListener(onRippleCompleteListener);
        rvConfirm.setOnRippleCompleteListener(onRippleCompleteListener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.grid_item_module, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Module module = getItem(position);
        viewHolder.ivIcon.setImageResource(module.getIconRes());
        viewHolder.tvTitle.setText(module.getTitle());
        viewHolder.rvModule.setOnRippleCompleteListener(new InnerOnRippleCompleteListener(position));
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.rv_module)
        RippleView rvModule;
        @ViewInject(R.id.iv_icon)
        ImageView ivIcon;
        @ViewInject(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }

    private class InnerOnRippleCompleteListener implements RippleView.OnRippleCompleteListener {

        private int position;

        public InnerOnRippleCompleteListener(int position) {
            this.position = position;
        }

        @Override
        public void onComplete(RippleView rippleView) {
            Intent intent = null;
            switch (position) {
                case 0:
                    intent = new Intent(getContext(), ClearActivity.class);
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case 1:
                    intent = new Intent(getContext(), AntiVirusActivity.class);
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case 2:
                    String pwd = SpUtils.getString(getContext(), KEY_GUARD_AGAINST_THEFT_PWD, null);
                    if (pwd != null) {
                        dialog.show();
                    } else {
                        intent = new Intent(getContext(), GuardAgainstTheftActivity.class);
                        getContext().startActivity(intent);
                        ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    }
                    break;
                case 3:
                    intent = new Intent(getContext(), BlanklistActivity.class);
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case 4:
                    intent = new Intent(getContext(), ProcessActivity.class);
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case 5:
                    intent = new Intent(getContext(), AppManagerActivity.class);
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
            }

            switch (rippleView.getId()) {
                case R.id.rv_cancel:
                    dialog.dismiss();
                    break;
                case R.id.rv_confirm:
                    String pwd = etPwd.getText().toString();
                    if (TextUtils.isEmpty(pwd)) {
                        etPwd.startAnimation(Tools.getShakeAimation(getContext()));
                        return;
                    }
                    if (pwd.equals(SpUtils.getString(getContext(), KEY_GUARD_AGAINST_THEFT_PWD, null))) {
                        intent = new Intent(getContext(), GuardAgainstTheftFinishActivity.class);
                        getContext().startActivity(intent);
                        ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                        dialog.dismiss();
                    } else {
                        etPwd.setText("");
                        ToastUtils.makeText(getContext(), "密码不正确");
                    }
                    break;
            }
        }
    }
}
