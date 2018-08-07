package com.wt.leanbackutil.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardRow {
    
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_SECTION_HEADER = 1;
    public static final int TYPE_DIVIDER = 2;

    @SerializedName("type")
    private int mType = TYPE_DEFAULT;
    @SerializedName("shadow")
    private boolean mShadow = true;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("cards")
    private List<Card> mCards;

    public int getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean useShadow() {
        return mShadow;
    }

    public List<Card> getCards() {
        return mCards;
    }

}