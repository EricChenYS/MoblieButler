package com.chhd.mobliebutler.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.entity.Group;
import com.chhd.mobliebutler.global.Consts;
import com.idunnololz.widgets.AnimatedExpandableListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/8/9.
 */
public class ClearAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter implements Consts {

    private Context context;
    private List<Group> groups;

    public ClearAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_child, null);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        App app = getChild(groupPosition, childPosition);
        childHolder.ivIcon.setImageDrawable(app.getIcon());
        childHolder.tvName.setText(app.getName());
        childHolder.tvCacheSize.setText(Formatter.formatFileSize(context, app.getCacheSize()));
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int childPosition) {
        return groups.get(childPosition).getApps().size();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public App getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getApps().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_group, null);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Group group = getGroup(groupPosition);
        String name = group.getName();
        groupHolder.tvHead.setText(name + " (" + group.getApps().size() + ")");
        if (KEY_USER_APP.equals(name)) {
            groupHolder.tvTotalCacheSize.setText(Formatter.formatFileSize(context, groups.get(groupPosition).getUserCacheSize()));
        } else {
            groupHolder.tvTotalCacheSize.setText(Formatter.formatFileSize(context, groups.get(groupPosition).getSystemCacheSize()));
        }

        if (isExpanded) {
            groupHolder.ivArrow.setImageResource(R.mipmap.ic_arrow_expanded);
        } else {
            groupHolder.ivArrow.setImageResource(R.mipmap.ic_arrow_default);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void notifyDataSetChanged(List<Group> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        notifyDataSetChanged();
    }

    static class ChildHolder {

        @ViewInject(R.id.iv_icon)
        ImageView ivIcon;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_cacheSize)
        TextView tvCacheSize;

        public ChildHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }

    static class GroupHolder {

        @ViewInject(R.id.iv_arrow)
        ImageView ivArrow;
        @ViewInject(R.id.tv_head)
        TextView tvHead;
        @ViewInject(R.id.tv_total_cacheSize)
        TextView tvTotalCacheSize;

        public GroupHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }
}
