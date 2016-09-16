package com.chhd.mobliebutler.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.global.MyApplication;
import com.chhd.util.SpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/8/18.
 */
public class ProcessAdapter extends BaseAdapter implements Consts {

    private Context context;
    private List<App> userApps;
    private List<App> systemApps;

    public ProcessAdapter(Context context, List<App> userApps, List<App> systemApps) {
        this.context = context;
        this.userApps = userApps;
        this.systemApps = systemApps;
    }


    @Override
    public int getCount() {
        if (SpUtils.getBoolean(context, KEY_IS_SHOW_SYSTEM_PROCESS, false)) {
            return userApps.size() + systemApps.size();
        } else {
            return userApps.size();
        }
    }

    @Override
    public App getItem(int position) {
        if (position < userApps.size()) {
            return userApps.get(position);
        } else {
            return systemApps.get(position - userApps.size());
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_process, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        App app = getItem(position);
        viewHolder.ivIcon.setImageDrawable(app.getIcon());
        viewHolder.tvName.setText(app.getName());
        viewHolder.tvMemSize.setText(Formatter.formatFileSize(context, app.getMemSize()));
        if (MyApplication.packageName.equals(app.getPackageName())) {
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            if (!app.isSystem()) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }
        }

        viewHolder.rvProcess.setOnClickListener(new InnerOnClickListener(position, viewHolder.checkBox));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == userApps.size() + 1) {
            return ITEM_HEAD;
        } else {
            return ITEM_PROCESS;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class ViewHolder {
        @ViewInject(R.id.rv_process)
        RippleView rvProcess;
        @ViewInject(R.id.iv_icon)
        ImageView ivIcon;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_memSize)
        TextView tvMemSize;
        @ViewInject(R.id.check_box)
        CheckBox checkBox;
    }

    private class InnerOnClickListener implements View.OnClickListener {

        private int position;
        private CheckBox cb_box;

        public InnerOnClickListener(int position, CheckBox cb_box) {
            this.position = position;
            this.cb_box = cb_box;
        }

        @Override
        public void onClick(View v) {
            if (!MyApplication.packageName.equals(getItem(position).getPackageName())) {
                cb_box.setChecked(!cb_box.isChecked());
                getItem(position).setIsCheck(cb_box.isChecked());
            }
        }
    }

}
