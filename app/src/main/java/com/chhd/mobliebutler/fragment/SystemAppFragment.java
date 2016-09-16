package com.chhd.mobliebutler.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.AppAdapter;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.util.DensityUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/26.
 */
public class SystemAppFragment extends Fragment {

    @ViewInject(R.id.list_view)
    SwipeMenuListView listView;

    private List<App> systemApps = new ArrayList<>();
    private AppAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_app_manager, null);

        ViewUtils.inject(this, view);

        initListView();

        return view;
    }

    private void initListView() {
        adapter = new AppAdapter(getActivity(), systemApps);
        listView.setAdapter(adapter);
        listView.setMenuCreator(new InnerSwipeMenuCreator());
        listView.setOnItemClickListener(new InnerOnItemClickListener());
        listView.setOnMenuItemClickListener(new InnerOnMenuItemClickListener());
    }

    public void updateListView(List<App> apps) {
        systemApps.addAll(apps);
        adapter.notifyDataSetChanged();
    }

    private void settings(int position) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + systemApps.get(position).getPackageName()));
        startActivity(intent);
    }

    private class InnerSwipeMenuCreator implements SwipeMenuCreator {

        @Override
        public void create(SwipeMenu menu) {

            SwipeMenuItem aboutItem = new SwipeMenuItem(getActivity());
            aboutItem.setWidth(DensityUtils.dp2px(getActivity(), 80));
            aboutItem.setBackground(new ColorDrawable(Color.parseColor("#379DE4")));
            aboutItem.setIcon(R.mipmap.ic_action_about);

            menu.addMenuItem(aboutItem);
        }
    }

    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listView.smoothOpenMenu(position);
        }
    }

    private class InnerOnMenuItemClickListener implements SwipeMenuListView.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            switch (index) {
                case 0:
                    settings(position);
                    break;
            }
            return false;
        }
    }
}
