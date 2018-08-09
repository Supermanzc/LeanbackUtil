package com.wt.leanbackutil.model;

/**
 * Created by DELL on 2018/8/9.
 */

public class SongSheetItem {

    private long diss_id;
    private int res_id;
    private String diss_name;
    private String pic_url;
    private int listen_num;

    public long getDiss_id() {
        return diss_id;
    }

    public void setDiss_id(long diss_id) {
        this.diss_id = diss_id;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public String getDiss_name() {
        return diss_name;
    }

    public void setDiss_name(String diss_name) {
        this.diss_name = diss_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getListen_num() {
        return listen_num;
    }

    public void setListen_num(int listen_num) {
        this.listen_num = listen_num;
    }
}
