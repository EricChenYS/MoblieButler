package com.chhd.mobliebutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.adapter.MyBaseAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.Calllog;
import com.chhd.mobliebutler.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by CWQ on 2016/9/5.
 */
public class CalllogAdapter extends MyBaseAdapter<Calllog> {

    public CalllogAdapter(Context context, List<Calllog> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_calllog, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Calllog calllog = getItem(position);
        String name = calllog.getName();
        if (TextUtils.isEmpty(name) || name == null) {
            viewHolder.tvNumber.setText(calllog.getNumber());
        } else {
            viewHolder.tvNumber.setText(name);
        }
        String typeStr = calllog.getType() == 1 ? "来电" : "去电";
        viewHolder.tvInfo.setText("[" + Tools.formatTime(calllog.getDate()) + "] " + typeStr);
        viewHolder.rvCalllog.setOnClickListener(new InnerOnClickListener(position, viewHolder.checkBox));
        return convertView;
    }

    class ViewHolder {

        @ViewInject(R.id.rv_calllog)
        RippleView rvCalllog;
        @ViewInject(R.id.tv_number)
        TextView tvNumber;
        @ViewInject(R.id.tv_info)
        TextView tvInfo;
        @ViewInject(R.id.check_box)
        CheckBox checkBox;

        public ViewHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {

        private int position;
        private CheckBox checkBox;

        public InnerOnClickListener(int position, CheckBox checkBox) {
            this.position = position;
            this.checkBox = checkBox;
        }

        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());
            getData().get(position).setIsCheck(checkBox.isChecked());
        }
    }
}
