package com.chhd.mobliebutler.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by CWQ on 2016/8/7.
 */
public class App implements Serializable {

    private Drawable icon;
    private String name;
    private String packageName;
    private long cacheSize;
    private long memSize;
    private long romSize;
    private boolean isSystem;
    private boolean isViurs;
    private boolean isCheck;

    public App() {
    }

    public App(Drawable icon, String name, String packageName, long cacheSize, boolean isSystem) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.cacheSize = cacheSize;
        this.isSystem = isSystem;
    }

    public App(Drawable icon, String name, String packageName, boolean isViurs) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.isViurs = isViurs;
    }

    public App(Drawable icon, String name, String packageName, long memSize, boolean isSystem, boolean isCheck) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.memSize = memSize;
        this.isSystem = isSystem;
        this.isCheck = isCheck;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public long getRomSize() {
        return romSize;
    }

    public void setRomSize(long romSize) {
        this.romSize = romSize;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean isViurs() {
        return isViurs;
    }

    public void setIsViurs(boolean isViurs) {
        this.isViurs = isViurs;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

}
