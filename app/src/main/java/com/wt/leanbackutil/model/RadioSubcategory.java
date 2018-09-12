package com.wt.leanbackutil.model;

public class RadioSubcategory {
    private int radio_id;
    private String radio_name;
    private String radio_pic;
    private int listen_num;

    private int sectionIndex;
    private boolean isRealEntity;

    public int getRadio_id() {
        return radio_id;
    }

    public void setRadio_id(int radio_id) {
        this.radio_id = radio_id;
    }

    public String getRadio_name() {
        return radio_name;
    }

    public void setRadio_name(String radio_name) {
        this.radio_name = radio_name;
    }

    public String getRadio_pic() {
        return radio_pic;
    }

    public void setRadio_pic(String radio_pic) {
        this.radio_pic = radio_pic;
    }

    public int getListen_num() {
        return listen_num;
    }

    public void setListen_num(int listen_num) {
        this.listen_num = listen_num;
    }

    public int getSectionIndex() {
        return sectionIndex;
    }

    public void setSectionIndex(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    public boolean isRealEntity() {
        return isRealEntity;
    }

    public void setRealEntity(boolean realEntity) {
        isRealEntity = realEntity;
    }
}