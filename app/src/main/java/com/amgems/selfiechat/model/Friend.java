package com.amgems.selfiechat.model;

/**
 * @author Sherman Pay.
 * @version 0.1, 11/30/14.
 */
public class Friend {
    private String name;
    private String imageRes;

    public Friend(String name) {
        this.name = name;
    }

    public Friend(String name, String imageRes) {
        this.name = name;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageRes() {
        return imageRes;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }
}
