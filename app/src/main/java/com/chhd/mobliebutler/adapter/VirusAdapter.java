package com.chhd.mobliebutler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.adapter.MyBaseAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.App;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/8/14.
 */
public class VirusAdapter extends MyBaseAdapter<App> {

    public VirusAdapter(Context context, List<App> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_virus, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        App app = getItem(position);
        viewHolder.ivIcon.setImageDrawable(app.getIcon());
        viewHolder.tvName.setText(app.getName());
        boolean virus = app.isViurs();
        if (virus) {
            viewHolder.tvScanInfo.setTextColor(Color.parseColor("#E93E61"));
            viewHolder.tvScanInfo.setText("发现病毒");
        } else {
            viewHolder.tvScanInfo.setTextColor(Color.parseColor("#1BA73A"));
            viewHolder.tvScanInfo.setText("扫描安全");
        }
        return convertView;
    }

    static class ViewHolder {

        @ViewInject(R.id.iv_icon)
        ImageView ivIcon;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_scan_info)
        TextView tvScanInfo;

        public ViewHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }
}
