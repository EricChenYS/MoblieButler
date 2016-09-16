package com.chhd.mobliebutler.entity;

/**
 * Created by CWQ on 2016/8/28.
 */
public class Contact {

    private String name;
    private String letter;
    private String number;

    public Contact() {
    }

    public Contact(String name, String letter, String number) {
        this.name = name;
        this.letter = letter;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", letter='" + letter + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
