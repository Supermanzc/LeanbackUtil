package com.wt.leanbackutil.model;

/**
 * Created by DELL on 2018/8/7.
 */

public class RecommendResponse extends BaseMode {
    private RecommendData data;

    public RecommendData getData() {
        return data;
    }

    public void setData(RecommendData data) {
        this.data = data;
    }
}
