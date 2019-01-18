package android.support.v17.leanback.widget;

public interface LoadMoreInterface {

    void onMoreReset(); // 恢复加载更多
    void onMoreOver(); // 没有加载更多.
    void onLoadComplete(); // 加载更多完成
    void setOnLoadMoreListener(OnLoadListener loadListener);
    void setLoadMoreState(MoreState state); // 设置加载更多状态

    public interface OnLoadListener {
        void onLoadMore();
    }

    enum MoreState {
        STATE_IDLE/*空闲状态*/,
        STATE_MORE_OVER/*无更多加载*/,
        STATE_LOADING_MORE/*正在加载更多*/;
    }

}
