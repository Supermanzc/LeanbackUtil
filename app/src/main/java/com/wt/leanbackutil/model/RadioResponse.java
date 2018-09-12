package com.wt.leanbackutil.model;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 */

public class RadioResponse extends BaseMode {
    private List<RadioInfo> data;
    private List<RadioCategory> categoryList;
    private List<RadioSubcategory> subcategoryList;

    public List<RadioInfo> getData() {
        return data;
    }

    public void setData(List<RadioInfo> data) {
        this.data = data;
    }

    public List<RadioCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<RadioCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public List<RadioSubcategory> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(List<RadioSubcategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }
}
