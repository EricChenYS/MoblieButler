package com.chhd.mobliebutler.entity;

/**
 * Created by CWQ on 2016/9/5.
 */
public class Calllog {

    private String number;
    private long date;
    private int type;
    private String name;
    private boolean isCheck;

    public Calllog() {
    }

    public Calllog(String number, long date, int type, String name) {
        this.number = number;
        this.date = date;
        this.type = type;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public String toString() {
        return "Calllog{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
