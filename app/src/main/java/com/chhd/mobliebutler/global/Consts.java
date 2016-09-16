package com.chhd.mobliebutler.global;

/**
 * Created by CWQ on 2016/8/7.
 */
public interface Consts {
    // MyApplication
    String ANTIVIRUS_DB = "antivirus.db";
    String KEY_IS_FIRST_START="is_first_start";
    // ClearActivity
    int MSG_PROGRESSBAR_SET_MAX = 0;
    int MSG_UPDATE_TOTAL_CACHESIZE = 1;
    int MSG_SCANNIG = 2;
    int MSG_SCAN_FINISH = 3;
    int YELLOW = 0;
    int GREEN = 1;
    int RED = 2;
    // ClearBiz
    String KEY_TOTAL_CACHESIZE = "tatal_dataSize";
    String KEY_NAME = "name";
    String KEY_GROUPS = "groups";
    String KEY_USER_APP = "用户应用";
    String KEY_SYSTEM_APP = "系统应用";
    // AntiVirusBiz
    int MSG_WAVE_VIEW_SET_MAX = 0;
    int MSG_WAVE_VIEW_SET_PROGRESS = 1;
    int MSG_FIND_VIRUS = 4;
    String KEY_SAFE_APPS = "safe_apps";
    String KEY_DANGER_APPS = "danger_apps";
    // ProcessBiz
    String KEY_USER_APPS_MEM = "user_apps_mem";
    String KEY_IS_SHOW_SYSTEM_PROCESS = "is_show_system";
    int MSG_GET_RUN_APP_PROCESS_INFOS = 0;
    // ProcessAdapter
    int ITEM_HEAD = 0;
    int ITEM_PROCESS = 1;
    // AppManagerBiz
    int MSG_GET_ALL_APPS = 0;
    // InputView
    String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    // ContactBiz
    int MSG_GET_CONTACTS = 0;
    // OpenGuardAgainstTheft1Activity
    String KEY_GUARD_AGAINST_THEFT_PASSWORD = "guard_against_theft_password";
    // ContactActivity
    String KEY_NUMBER = "number";
    // OpenGuardAgainstTheft2Activity
    String KEY_GUARD_AGAINST_THEFT_PWD = "guard_against_theft_pwd";
    String KEY_SIM_SERIAL_NUMBER = "sim_serial_number";
    String KEY_IS_OPEN_GUARD_AGAINST_THEFT = "is_open_guard_against_theft";
    String ACTION_GUARD_AGAINST_THEFT_FINISH = "guard_against_theft_finish";
    // BlacklistDao
    String TABLE_BLACKLIST = "blacklist";
    String KEY_MODE = "mode";
    // ByhandActivity
    int MODE_NONE = 0;
    int MODE_TEL = 1;
    int MODE_SMS = 2;
    int MODE_ALL = 3;
    // BlanklistActivity
    int MSG_GET_BLACKLISTS = 0;
    // CalllogActivity
    int MSG_GET_CALLLOGS = 0;
}
