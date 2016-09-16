package com.chhd.mobliebutler.entity;

/**
 * Created by CWQ on 2016/9/3.
 */
public class Blacklist {

    private String number;
    private String name;
    private int mode;

    public Blacklist() {
    }

    public Blacklist(String number, String name, int mode) {
        this.number = number;
        this.name = name;
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Blacklist{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", mode=" + mode +
                '}';
    }
}
