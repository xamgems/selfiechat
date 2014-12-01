package com.amgems.selfiechat.model;

/**
 * @author Sherman Pay.
 * @version 0.1, 11/30/14.
 */
public class Friend {
    private String name;
    private int imageResId;

    public Friend(String name) {
        this.name = name;
    }

    public Friend(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
