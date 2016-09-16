package com.chhd.mobliebutler.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by CWQ on 2016/8/26.
 */
public class UserAppFragment extends Fragment {

    @ViewInject(R.id.list_view)
    SwipeMenuListView listView;

    private List<App> userApps = new ArrayList<>();
    private AppAdapter adapter;
    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_app_manager, null);

        ViewUtils.inject(this, view);

        initListView();

        setReceiver();

        return view;
    }

    private void initListView() {
        adapter = new AppAdapter(getActivity(), userApps);
        listView.setAdapter(adapter);
        listView.setMenuCreator(new InnerSwipeMenuCreator());
        listView.setOnItemClickListener(new InnerOnItemClickListener());
        listView.setOnMenuItemClickListener(new InnerOnMenuItemClickListener());
    }

    private void setReceiver() {
        receiver = new InnerBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getActivity().registerReceiver(receiver, filter);
    }

    public void updateListView(List<App> apps) {
        userApps.addAll(apps);
        adapter.notifyDataSetChanged();
    }

    private void settings(int position) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + userApps.get(position).getPackageName()));
        startActivity(intent);
    }

    private void share(int position) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setText("我是正在使用: " + userApps.get(position).getName());
        oks.show(getActivity());
    }


    private void uninstall(int position) {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + userApps.get(position).getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    private class InnerSwipeMenuCreator implements SwipeMenuCreator {
        @Override
        public void create(SwipeMenu menu) {

            SwipeMenuItem aboutItem = new SwipeMenuItem(getActivity());
            aboutItem.setWidth(DensityUtils.dp2px(getActivity(), 80));
            aboutItem.setBackground(new ColorDrawable(Color.parseColor("#379DE4")));
            aboutItem.setIcon(R.mipmap.ic_action_about);

            SwipeMenuItem shareItem = new SwipeMenuItem(getActivity());
            shareItem.setWidth(DensityUtils.dp2px(getActivity(), 80));
            shareItem.setBackground(new ColorDrawable(Color.parseColor("#F6CE4A")));
            shareItem.setIcon(R.mipmap.ic_action_share);

            SwipeMenuItem discardItem = new SwipeMenuItem(getActivity());
            discardItem.setWidth(DensityUtils.dp2px(getActivity(), 80));
            discardItem.setBackground(new ColorDrawable(Color.parseColor("#E74D6F")));
            discardItem.setIcon(R.mipmap.ic_action_discard);

            menu.addMenuItem(aboutItem);
            menu.addMenuItem(shareItem);
            menu.addMenuItem(discardItem);
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
                case 1:
                    share(position);
                    break;
                case 2:
                    uninstall(position);
                    break;
            }
            return false;
        }
    }

    private class InnerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<App> uninstallApps = new ArrayList<>();
            String packageName = intent.getDataString().split(":")[1];
            for (App app : userApps) {
                if (packageName.equals(app.getPackageName())) {
                    uninstallApps.add(app);
                }
            }
            for (App app : uninstallApps) {
                userApps.remove(app);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
