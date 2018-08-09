package com.wt.leanbackutil.model;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 */

public class RadioInfo {

    private String id;
    private String radio_group_name;
    /**
     * 设置类型处理

     */
    private int type;
    private List<RadioItem> radios;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRadio_group_name() {
        return radio_group_name;
    }

    public void setRadio_group_name(String radio_group_name) {
        this.radio_group_name = radio_group_name;
    }

    public List<RadioItem> getRadios() {
        return radios;
    }

    public void setRadios(List<RadioItem> radios) {
        this.radios = radios;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
