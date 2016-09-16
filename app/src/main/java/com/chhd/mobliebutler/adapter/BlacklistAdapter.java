package com.chhd.mobliebutler.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.adapter.MyBaseAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.dao.BlacklistDao;
import com.chhd.mobliebutler.entity.Blacklist;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.LogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/9/4.
 */
public class BlacklistAdapter extends MyBaseAdapter<Blacklist> implements Consts {

    private Animation deleteAnim;
    private BlacklistDao dao;

    public BlacklistAdapter(Context context, List<Blacklist> data) {
        super(context, data);
        deleteAnim = AnimationUtils.loadAnimation(context, R.anim.set_item_blacklist_delete);
        dao = new BlacklistDao(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_blacklist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Blacklist blacklist = getItem(position);
        String name = blacklist.getName();
        if (TextUtils.isEmpty(name) || name == null) {
            viewHolder.tvNumber.setText(blacklist.getNumber());
        } else {
            viewHolder.tvNumber.setText(name);
        }
        switch (blacklist.getMode()) {
            case MODE_TEL:
                viewHolder.tvMode.setText("只拦截电话");
                break;
            case MODE_SMS:
                viewHolder.tvMode.setText("只拦截短信");
                break;
            case MODE_ALL:
                viewHolder.tvMode.setText("拦截电话和短信");
                break;
        }
        viewHolder.ibDelete.setOnClickListener(new InnerOnClickListener(position, convertView));
        viewHolder.rvBlacklist.setOnRippleCompleteListener(new InnerOnRippleCompleteListener(position));
        return convertView;
    }

    class ViewHolder {

        @ViewInject(R.id.rv_blacklist)
        RippleView rvBlacklist;
        @ViewInject(R.id.tv_number)
        TextView tvNumber;
        @ViewInject(R.id.tv_mode)
        TextView tvMode;
        @ViewInject(R.id.ib_delete)
        ImageButton ibDelete;

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
            LogUtils.i("onComplete");
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {

        private int position;
        private View view;

        public InnerOnClickListener(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("确定要删除吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    view.startAnimation(deleteAnim);
                    deleteAnim.setAnimationListener(new InnerAnimationListener(position));
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private class InnerAnimationListener implements Animation.AnimationListener {

        private int position;

        public InnerAnimationListener(int position) {
            this.position = position;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dao.delete(getItem(position).getNumber());
            getData().remove(getItem(position));
            notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
