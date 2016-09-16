package com.chhd.mobliebutler.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/7.
 */
public class Group implements Parcelable {

    private String name;
    private List<App> apps;
    private long userCacheSize;
    private long systemCacheSize;

    public Group() {
    }

    public Group(String name, List<App> apps) {
        this.name = name;
        this.apps = apps;
    }

    public Group(String name, List<App> apps, long userCacheSize, long systemCacheSize) {
        this.name = name;
        this.apps = apps;
        this.userCacheSize = userCacheSize;
        this.systemCacheSize = systemCacheSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public long getUserCacheSize() {
        return userCacheSize;
    }

    public void setUserCacheSize(long userCacheSize) {
        this.userCacheSize = userCacheSize;
    }

    public long getSystemCacheSize() {
        return systemCacheSize;
    }

    public void setSystemCacheSize(long systemCacheSize) {
        this.systemCacheSize = systemCacheSize;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.apps);
        dest.writeLong(this.userCacheSize);
        dest.writeLong(this.systemCacheSize);
    }

    protected Group(Parcel in) {
        this.name = in.readString();
        this.apps = new ArrayList<App>();
        in.readList(this.apps, App.class.getClassLoader());
        this.userCacheSize = in.readLong();
        this.systemCacheSize = in.readLong();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
