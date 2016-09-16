package com.chhd.mobliebutler.entity;

/**
 * Created by CWQ on 2016/8/25.
 */
public class Menu {

    private int iconRes;
    private String title;

    public Menu() {
    }

    public Menu(int iconRes, String title) {
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
