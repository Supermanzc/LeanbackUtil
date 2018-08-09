package com.wt.leanbackutil.model;

import java.util.List;

/**
 * Created by DELL on 2018/8/9.
 */

public class SongSheetResponse extends BaseMode {

    private List<SongSheetItem> data;

    public List<SongSheetItem> getData() {
        return data;
    }

    public void setData(List<SongSheetItem> data) {
        this.data = data;
    }
}
