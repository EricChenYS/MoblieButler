package com.chhd.mobliebutler.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.chhd.adapter.MyBaseAdapter;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.Contact;
import com.chhd.mobliebutler.global.Consts;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by CWQ on 2016/8/28.
 */
public class ContactAdapter extends MyBaseAdapter<Contact> implements SectionIndexer, Consts {

    public ContactAdapter(Context context, List<Contact> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_contact, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = getItem(position);
        viewHolder.tvHead.setText(contact.getLetter());
        viewHolder.tvName.setText(contact.getName());
        viewHolder.tvNumber.setText(contact.getNumber());
        if (!TextUtils.isEmpty(contact.getName())) {
            viewHolder.tvName.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvName.setVisibility(View.GONE);
        }
        if (position == getPositionForSection(getSectionForPosition(position))) {
            viewHolder.tvHead.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvHead.setVisibility(View.GONE);
        }
        viewHolder.rvContact.setOnRippleCompleteListener(new InnerOnRippleCompleteListener(position));
        return convertView;
    }

    @Override
    public String[] getSections() {
        String[] sections;
        Set<String> set = new TreeSet<>();
        for (int i = 0; i < getData().size(); i++) {
            set.add(getData().get(i).getLetter());
        }
        sections = new String[set.size()];
        int i = 0;
        for (String s : set) {
            sections[i++] = s;
        }
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getData().size(); i++) {
            int currentSectionIndex = getSectionForPosition(i);
            if (currentSectionIndex == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return getData().get(position).getLetter().charAt(0);
    }

    static class ViewHolder {
        @ViewInject(R.id.rv_contact)
        RippleView rvContact;
        @ViewInject(R.id.tv_head)
        TextView tvHead;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_number)
        TextView tvNumber;

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
            Activity activity = ((Activity) getContext());
            Intent intent = new Intent();
            intent.putExtra(KEY_NUMBER, getItem(position).getNumber());
            activity.setResult(0, intent);
            activity.finish();
        }
    }
}
