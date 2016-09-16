package com.chhd.mobliebutler.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.adapter.MenuAdpater;
import com.chhd.mobliebutler.adapter.ModuleAdapter;
import com.chhd.mobliebutler.entity.Menu;
import com.chhd.mobliebutler.entity.Module;
import com.chhd.mobliebutler.receiver.MyDeviceAdminReceiver;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @ViewInject(R.id.gv_module)
    GridView gvModule;
    @ViewInject(R.id.lv_menu)
    ListView lvMenu;

    private ActionBarDrawerToggle toggle;
    private List<Module> modules = new ArrayList<>();
    private List<Menu> menus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        initActionBar();

        initData();

        addDeviceAdmin();

        setAdapter();

        setListener();
    }

    private void initActionBar() {
        toolbar.setTitle("手机管家");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open, R.string.menu_close);
        toggle.syncState();

        modules.add(new Module(R.mipmap.home_clear, "手机清理"));
        modules.add(new Module(R.mipmap.home_anti_virus, "手机杀毒"));
        modules.add(new Module(R.mipmap.home_guard_against_theft, "手机防盗"));
        modules.add(new Module(R.mipmap.home_harassment_intercept, "骚扰拦截"));
        modules.add(new Module(R.mipmap.home_process_manager, "进程管家"));
        modules.add(new Module(R.mipmap.home_program_manager, "软件管家"));
        modules.add(new Module(R.mipmap.home_backup, "安全备份"));
        modules.add(new Module(R.mipmap.home_app_market, "应用市场"));
        modules.add(new Module(R.mipmap.home_tools, "高级工具"));

        menus.add(new Menu(R.mipmap.ic_drawer_settings, "设置中心"));
        menus.add(new Menu(R.mipmap.ic_drawer_feedback, "意见反馈"));
        menus.add(new Menu(R.mipmap.ic_drawer_phoneinfo, "设备信息"));
        menus.add(new Menu(R.mipmap.ic_drawer_about, "关于软件"));

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.set_item_module);
        LayoutAnimationController controller = new LayoutAnimationController(animation, 0.3f);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        gvModule.setLayoutAnimation(controller);
    }

    private void addDeviceAdmin() {
        ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "手机防盗模块，激活开启锁定手机、清空数据功能");
        startActivity(intent);
    }

    private void setAdapter() {
        gvModule.setAdapter(new ModuleAdapter(this, modules));
        lvMenu.setAdapter(new MenuAdpater(this, menus));
    }

    private void setListener() {
        drawerLayout.setDrawerListener(toggle);
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new InnerOnGlobalLayoutListener());
    }

    private class InnerOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            int height = toolbar.getHeight();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) drawerLayout.getLayoutParams();
            params.topMargin = height;
            drawerLayout.setLayoutParams(params);
        }
    }
}
