package com.chhd.mobliebutler.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.adapter.MyBaseAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.activity.FeedbackActivity;
import com.chhd.mobliebutler.entity.Menu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/8/25.
 */
public class MenuAdpater extends MyBaseAdapter<Menu> {

    public MenuAdpater(Context context, List<Menu> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_menu, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Menu menu = getItem(position);
        viewHolder.ivIcon.setImageResource(menu.getIconRes());
        viewHolder.tvTitle.setText(menu.getTitle());
        viewHolder.rvMenu.setOnRippleCompleteListener(new InnerRippleCompleteListener(position));
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.rv_menu)
        RippleView rvMenu;
        @ViewInject(R.id.iv_icon)
        ImageView ivIcon;
        @ViewInject(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }

    private class InnerRippleCompleteListener implements RippleView.OnRippleCompleteListener {

        private int position;

        public InnerRippleCompleteListener(int position) {
            this.position = position;
        }

        @Override
        public void onComplete(RippleView rippleView) {
            Intent intent = null;
            switch (position) {
                case 0:

                    break;
                case 1:
                    intent = new Intent(getContext(), FeedbackActivity.class);
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        }
    }
}
