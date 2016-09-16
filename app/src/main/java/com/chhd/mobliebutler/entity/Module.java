package com.chhd.mobliebutler.entity;

/**
 * Created by CWQ on 2016/8/25.
 */
public class Module {

    private int iconRes;
    private String title;

    public Module() {
    }

    public Module(int iconRes, String title) {
        this.iconRes = iconRes;
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
