package com.wt.leanbackutil.model;

import java.util.List;

/**
 * Created by DELL on 2018/8/7.
 */

public class RecommendData {

    private int version;
    private long create_time;
    private List<RecommendInfo> reco_datas;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public List<RecommendInfo> getReco_datas() {
        return reco_datas;
    }

    public void setReco_datas(List<RecommendInfo> reco_datas) {
        this.reco_datas = reco_datas;
    }
}
