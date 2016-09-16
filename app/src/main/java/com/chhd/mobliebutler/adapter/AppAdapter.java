package com.chhd.mobliebutler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.App;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Random;

/**
 * Created by CWQ on 2016/8/26.
 */
public class AppAdapter extends BaseSwipListAdapter {

    private Context context;
    private List<App> userApps;
    private Random random;

    public AppAdapter(Context context, List<App> userApps) {
        this.context = context;
        this.userApps = userApps;
        random = new Random();
    }

    @Override
    public int getCount() {
        return userApps.size();
    }

    @Override
    public App getItem(int position) {
        return userApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_app_manager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        App app = getItem(position);
        viewHolder.ivIcon.setImageDrawable(app.getIcon());
        viewHolder.tvName.setText(app.getName());
        viewHolder.tvRomSize.setText(Formatter.formatFileSize(context, app.getRomSize()));

        int red = 30 + random.nextInt(201);
        int green = 30 + random.nextInt(201);
        int blue = 30 + random.nextInt(201);
        viewHolder.view.setBackgroundColor(Color.rgb(red, green, blue));

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.iv_icon)
        ImageView ivIcon;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_romSize)
        TextView tvRomSize;
        @ViewInject(R.id.view)
        View view;

        public ViewHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }
}
