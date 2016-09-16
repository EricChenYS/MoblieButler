package com.chhd.mobliebutler.global;

import android.app.Application;

import com.chhd.util.SpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by CWQ on 2016/8/25.
 */
public class MyApplication extends Application implements Consts {

    public static String packageName;

    static {
        System.loadLibrary("feedback");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initData();

        loadLocalDatabase();


        if (SpUtils.getBoolean(this, KEY_IS_FIRST_START, true)) {
            cfork();
            SpUtils.getBoolean(this, KEY_IS_FIRST_START, false);
        }
    }

    private void initData() {
        packageName = getPackageName();
    }

    private void loadLocalDatabase() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    File file = new File(getFilesDir(), ANTIVIRUS_DB);
                    if (!file.exists()) {
                        InputStream inputStream = getAssets().open("db/" + ANTIVIRUS_DB);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        int len;
                        byte[] buffer = new byte[1024 * 8];
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        inputStream.close();
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public native void cfork();
}
