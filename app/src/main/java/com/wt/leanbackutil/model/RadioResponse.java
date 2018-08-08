package com.wt.leanbackutil.model;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 */

public class RadioResponse extends BaseMode {
    private List<RadioInfo> data;

    public List<RadioInfo> getData() {
        return data;
    }

    public void setData(List<RadioInfo> data) {
        this.data = data;
    }
}
