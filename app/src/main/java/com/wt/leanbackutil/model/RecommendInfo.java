package com.wt.leanbackutil.model;

import java.util.List;

/**
 * Created by DELL on 2018/8/7.
 */

public class RecommendInfo {

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    public static final int TYPE_FOUR = 4;

    private String name;
    private int type;
    private List<SingItem> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SingItem> getList() {
        return list;
    }

    public void setList(List<SingItem> list) {
        this.list = list;
    }
}
