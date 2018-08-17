package com.wt.leanbackutil.leankback.seletor;

import android.content.Context;

import com.open.leanback.widget.Presenter;
import com.open.leanback.widget.PresenterSelector;
import com.wt.leanbackutil.leankback.presenter.RadioCardPresenter;
import com.wt.leanbackutil.model.Card;
import com.wt.leanbackutil.model.RadioItem;

/**
 * Created by DELL on 2018/8/17.
 * item内容选择器
 * 返回每一个卡片内容
 */

public class CardPresenterSelector extends PresenterSelector {

    private Context mContext;

    public CardPresenterSelector(Context context) {
        mContext = context;
    }

    @Override
    public Presenter getPresenter(Object item) {
        if (!(item instanceof RadioItem)) {
            throw new RuntimeException(
                    String.format("The PresenterSelector only supports data items of type '%s'",
                            Card.class.getName()));
        }
//        RadioItem radioItem = (RadioItem) item;
        return new RadioCardPresenter(mContext);
    }
}
