package com.wt.leanbackutil.leankback.seletor;

import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.Presenter;
import com.open.leanback.widget.PresenterSelector;
import com.wt.leanbackutil.leankback.HeaderPresenter;
import com.wt.leanbackutil.leankback.presenter.ConcertListRowPresenter;
import com.wt.leanbackutil.leankback.presenter.ConcertPlayerPresenter;
import com.wt.leanbackutil.leankback.presenter.ConcertThemePresenter;
import com.wt.leanbackutil.leankback.presenter.ConcertViewPagerPresenter;
import com.wt.leanbackutil.model.RecommendInfo;

/**
 * @author junyan
 *         选择器
 */
public class ConcertPresenterSelector extends PresenterSelector {

    private ConcertListRowPresenter concertListRowPresenter;
    private ConcertViewPagerPresenter concertViewPagerPresenter;
    private ConcertThemePresenter concertThemePresenter;
    private ConcertPlayerPresenter concertPlayerPresenter;

    public ConcertPresenterSelector() {
        concertListRowPresenter = new ConcertListRowPresenter();
        concertViewPagerPresenter = new ConcertViewPagerPresenter();
        concertThemePresenter = new ConcertThemePresenter();
        concertPlayerPresenter = new ConcertPlayerPresenter();
    }

    @Override
    public Presenter[] getPresenters() {
        return new Presenter[]{concertListRowPresenter,
                concertViewPagerPresenter, concertThemePresenter};
    }

    @Override
    public Presenter getPresenter(Object item) {
        ListRow listRow = (ListRow) item;
        if (listRow.getId() == RecommendInfo.TYPE_THREE) {
            concertViewPagerPresenter.setHeaderPresenter(new HeaderPresenter());
            return concertViewPagerPresenter;
        }else if(listRow.getId() == RecommendInfo.TYPE_SEVEN){
            return concertThemePresenter;
        }else if(listRow.getId() == RecommendInfo.TYPE_EIGHT){
            concertPlayerPresenter.setHeaderPresenter(new HeaderPresenter());
            return concertPlayerPresenter;
        }
        concertListRowPresenter.setHeaderPresenter(new HeaderPresenter());
        return concertListRowPresenter;
    }

}