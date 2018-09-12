package com.wt.leanbackutil.model;

public class RadioCategory {
    private String title;

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }
}