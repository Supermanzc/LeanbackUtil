package android.support.v17.leanback.app;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentManager.BackStackEntry;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v17.leanback.R;
import android.support.v17.leanback.transition.TransitionListener;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BrowseFrameLayout;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.TitleView;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

@SuppressLint("NewApi")
public class WMenuFragment extends BaseFragment {

	public static interface OnMenuItemSelectedListener {
		public void onMenuItemSelected(Row row, int position);
	}

    // BUNDLE attribute for saving header show/hide status when backstack is used:
    static final String HEADER_STACK_INDEX = "headerStackIndex";
    // BUNDLE attribute for saving header show/hide status when backstack is not used:
    static final String HEADER_SHOW = "headerShow";


    final class BackStackListener implements FragmentManager.OnBackStackChangedListener {
        int mLastEntryCount;
        int mIndexOfHeadersBackStack;

        BackStackListener() {
            mLastEntryCount = getFragmentManager().getBackStackEntryCount();
            mIndexOfHeadersBackStack = -1;
        }

        void load(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                mIndexOfHeadersBackStack = savedInstanceState.getInt(HEADER_STACK_INDEX, -1);
                mShowingHeaders = mIndexOfHeadersBackStack == -1;
            } else {
                if (!mShowingHeaders) {
                    getFragmentManager().beginTransaction()
                            .addToBackStack(mWithHeadersBackStackName).commit();
                }
            }
        }

        void save(Bundle outState) {
            outState.putInt(HEADER_STACK_INDEX, mIndexOfHeadersBackStack);
        }


        @Override
        public void onBackStackChanged() {
            if (getFragmentManager() == null) {
                Log.w(TAG, "getFragmentManager() is null, stack:", new Exception());
                return;
            }
            int count = getFragmentManager().getBackStackEntryCount();
            // if backstack is growing and last pushed entry is "headers" backstack,
            // remember the index of the entry.
            if (count > mLastEntryCount) {
                BackStackEntry entry = getFragmentManager().getBackStackEntryAt(count - 1);
                if (mWithHeadersBackStackName.equals(entry.getName())) {
                    mIndexOfHeadersBackStack = count - 1;
                }
            } else if (count < mLastEntryCount) {
                // if popped "headers" backstack, initiate the show header transition if needed
                if (mIndexOfHeadersBackStack >= count) {
                    mIndexOfHeadersBackStack = -1;
                    if (!mShowingHeaders) {
                        startHeadersTransitionInternal(true);
                    }
                }
            }
            mLastEntryCount = count;
        }
    }

    /**
     * Listener for transitions between browse headers and rows.
     */
    public static class BrowseTransitionListener {
        /**
         * Callback when headers transition starts.
         *
         * @param withHeaders True if the transition will result in headers
         *        being shown, false otherwise.
         */
        public void onHeadersTransitionStart(boolean withHeaders) {
        }
        /**
         * Callback when headers transition stops.
         *
         * @param withHeaders True if the transition will result in headers
         *        being shown, false otherwise.
         */
        public void onHeadersTransitionStop(boolean withHeaders) {
        }
    }

    private class SetSelectionRunnable implements Runnable {
        static final int TYPE_INVALID = -1;
        static final int TYPE_INTERNAL_SYNC = 0;
        static final int TYPE_USER_REQUEST = 1;

        private int mPosition;
        private int mType;
        private boolean mSmooth;

        SetSelectionRunnable() {
            reset();
        }

        void post(int position, int type, boolean smooth) {
            // Posting the set selection, rather than calling it immediately, prevents an issue
            // with adapter changes.  Example: a row is added before the current selected row;
            // first the fast lane view updates its selection, then the rows fragment has that
            // new selection propagated immediately; THEN the rows view processes the same adapter
            // change and moves the selection again.
            if (type >= mType) {
                mPosition = position;
                mType = type;
                mSmooth = smooth;
                mBrowseFrame.removeCallbacks(this);
                mBrowseFrame.post(this);
            }
        }

        @Override
        public void run() {
            setSelection(mPosition, mSmooth);
            reset();
        }

        private void reset() {
            mPosition = -1;
            mType = TYPE_INVALID;
            mSmooth = false;
        }
    }

    private static final String TAG = "ANDROIDTV";

    private static final String LB_HEADERS_BACKSTACK = "lbHeadersBackStack_";

    private static boolean DEBUG = false;

    /** The headers fragment is enabled and shown by default. */
    public static final int HEADERS_ENABLED = 1;

    /** The headers fragment is enabled and hidden by default. */
    public static final int HEADERS_HIDDEN = 2;

    /** The headers fragment is disabled and will never be shown. */
    public static final int HEADERS_DISABLED = 3;

    private RowsFragment mRowsFragment;
    private HeadersFragment mMenuFragment;

    private ObjectAdapter mMenuAdapter;
    private ObjectAdapter mRowsAdapter;

    private int mHeadersState = HEADERS_ENABLED;
    private int mBrandColor = Color.TRANSPARENT;
    private boolean mBrandColorSet;

    private BrowseFrameLayout mBrowseFrame;
    private boolean mHeadersBackStackEnabled = true;
    private String mWithHeadersBackStackName;
    private boolean mShowingHeaders = true;
    private boolean mCanShowHeaders = true;
    private int mContainerListMarginStart;
    private int mContainerListAlignTop;
    private int mContainerListFirstItemAlignTop;
    private boolean mRowScaleEnabled = true;
    private OnItemViewSelectedListener mExternalOnItemViewSelectedListener;
    private OnMenuItemSelectedListener mExternalOnMenuItemSelectedListener;
    private OnItemViewClickedListener mOnItemViewClickedListener;
    private int mSelectedRowPosition = NO_POSITION;
    private int mSelectedItemPosition = NO_POSITION;

    private PresenterSelector mHeaderPresenterSelector;
    private final SetSelectionRunnable mSetSelectionRunnable = new SetSelectionRunnable();

    // transition related:
    private Object mSceneWithHeaders;
    private Object mSceneWithoutHeaders;
    private Object mSceneAfterEntranceTransition;
    private Object mHeadersTransition;
    private BackStackListener mBackStackChangedListener;
    private BrowseTransitionListener mBrowseTransitionListener;

    private static final String ARG_TITLE = BrowseFragment.class.getCanonicalName() + ".title";
    private static final String ARG_BADGE_URI = BrowseFragment.class.getCanonicalName() + ".badge";
    private static final String ARG_HEADERS_STATE =
        BrowseFragment.class.getCanonicalName() + ".headersState";

    /**
     * Creates arguments for a browse fragment.
     *
     * @param args The Bundle to place arguments into, or null if the method
     *        should return a new Bundle.
     * @param title The title of the BrowseFragment.
     * @param headersState The initial state of the headers of the
     *        BrowseFragment. Must be one of {@link #HEADERS_ENABLED}, {@link
     *        #HEADERS_HIDDEN}, or {@link #HEADERS_DISABLED}.
     * @return A Bundle with the given arguments for creating a BrowseFragment.
     */
    public static Bundle createArgs(Bundle args, String title, int headersState) {
        if (args == null) {
            args = new Bundle();
        }
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_HEADERS_STATE, headersState);
        return args;
    }

    /**
     * Sets the brand color for the browse fragment. The brand color is used as
     * the primary color for UI elements in the browse fragment. For example,
     * the background color of the headers fragment uses the brand color.
     *
     * @param color The color to use as the brand color of the fragment.
     */
    public void setBrandColor(int color) {
        mBrandColor = color;
        mBrandColorSet = true;

        if (mMenuFragment != null) {
            mMenuFragment.setBackgroundColor(mBrandColor);
        }
    }

    /**
     * Returns the brand color for the browse fragment.
     * The default is transparent.
     */
    public int getBrandColor() {
        return mBrandColor;
    }

    /**
     * Sets the adapter containing the rows for the fragment.
     *
     * <p>The items referenced by the adapter must be be derived from
     * {@link Row}. These rows will be used by the rows fragment and the headers
     * fragment (if not disabled) to render the browse rows.
     *
     * @param adapter An ObjectAdapter for the browse rows. All items must
     *        derive from {@link Row}.
     */
    public void setRowsAdapter(ObjectAdapter adapter) {
    	setRowsAdapter(adapter, mContainerListAlignTop);
    }
    
    public void setRowsAdapter(ObjectAdapter adapter, int firstRowMarginTop) {
    	mContainerListFirstItemAlignTop = firstRowMarginTop;
    	mRowsAdapter = adapter;
    	if (mRowsFragment != null) {
    		mRowsFragment.setAdapter(adapter);
    		setSelection(0, false);
    	}
    }
    
    public void setMenuAdapter(ObjectAdapter adapter) {
    	mMenuAdapter = adapter;
    	if (mMenuFragment != null) {
    		mMenuFragment.setAdapter(adapter);
    	}
    }

    /**
     * Returns the adapter containing the rows for the fragment.
     */
    public ObjectAdapter getMenuAdapter() {
        return mMenuAdapter;
    }

    public ObjectAdapter getRowsAdapter() {
        return mRowsAdapter;
    }

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(OnItemViewSelectedListener listener) {
        mExternalOnItemViewSelectedListener = listener;
    }

    /**
     * Returns an item selection listener.
     */
    public OnItemViewSelectedListener getOnItemViewSelectedListener() {
        return mExternalOnItemViewSelectedListener;
    }

    /**
     * Sets an item clicked listener on the fragment.
     * OnItemViewClickedListener will override {@link View.OnClickListener} that
     * item presenter sets during {@link Presenter#onCreateViewHolder(ViewGroup)}.
     * So in general,  developer should choose one of the listeners but not both.
     */
    public void setOnItemViewClickedListener(OnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
        if (mRowsFragment != null) {
            mRowsFragment.setOnItemViewClickedListener(listener);
        }
    }
    
    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener listener) {
    	mExternalOnMenuItemSelectedListener = listener;
    }

    /**
     * Returns the item Clicked listener.
     */
    public OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    /**
     * Starts a headers transition.
     *
     * <p>This method will begin a transition to either show or hide the
     * headers, depending on the value of withHeaders. If headers are disabled
     * for this browse fragment, this method will throw an exception.
     *
     * @param withHeaders True if the headers should transition to being shown,
     *        false if the transition should result in headers being hidden.
     */
    public void startHeadersTransition(boolean withHeaders) {
        if (!mCanShowHeaders) {
            throw new IllegalStateException("Cannot start headers transition");
        }
        if (isInHeadersTransition() || mShowingHeaders == withHeaders) {
            return;
        }
        startHeadersTransitionInternal(withHeaders);
    }

    /**
     * Returns true if the headers transition is currently running.
     */
    public boolean isInHeadersTransition() {
        return mHeadersTransition != null;
    }

    /**
     * Returns true if headers are shown.
     */
    public boolean isShowingHeaders() {
        return mShowingHeaders;
    }

    /**
     * Sets a listener for browse fragment transitions.
     *
     * @param listener The listener to call when a browse headers transition
     *        begins or ends.
     */
    public void setBrowseTransitionListener(BrowseTransitionListener listener) {
        mBrowseTransitionListener = listener;
    }

    /**
     * Enables scaling of rows when headers are present.
     * By default enabled to increase density.
     *
     * @param enable true to enable row scaling
     */
    public void enableRowScaling(boolean enable) {
        mRowScaleEnabled = enable;
        if (mRowsFragment != null) {
            mRowsFragment.enableRowScaling(mRowScaleEnabled);
        }
    }

    private void startHeadersTransitionInternal(final boolean withHeaders) {
        if (getFragmentManager().isDestroyed()) {
            return;
        }
        mShowingHeaders = withHeaders;
        mRowsFragment.onExpandTransitionStart(!withHeaders, new Runnable() {
            @Override
            public void run() {
                mMenuFragment.onTransitionStart();
                createHeadersTransition();
                if (mBrowseTransitionListener != null) {
                    mBrowseTransitionListener.onHeadersTransitionStart(withHeaders);
                }
                sTransitionHelper.runTransition(withHeaders ? mSceneWithHeaders : mSceneWithoutHeaders,
                        mHeadersTransition);
                if (mHeadersBackStackEnabled) {
                    if (!withHeaders) {
                        getFragmentManager().beginTransaction()
                                .addToBackStack(mWithHeadersBackStackName).commit();
                    } else {
                        int index = mBackStackChangedListener.mIndexOfHeadersBackStack;
                        if (index >= 0) {
                            BackStackEntry entry = getFragmentManager().getBackStackEntryAt(index);
                            getFragmentManager().popBackStackImmediate(entry.getId(),
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }
                }
            }
        });
    }

    private boolean isVerticalScrolling() {
        // don't run transition
        return mMenuFragment.getVerticalGridView().getScrollState()
                != HorizontalGridView.SCROLL_STATE_IDLE
                || mRowsFragment.getVerticalGridView().getScrollState()
                != HorizontalGridView.SCROLL_STATE_IDLE;
    }


    private final BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener =
            new BrowseFrameLayout.OnFocusSearchListener() {
        @Override
        public View onFocusSearch(View focused, int direction) {
            // if headers is running transition,  focus stays
            if (mCanShowHeaders && isInHeadersTransition()) {
                return focused;
            }
            if (DEBUG) Log.v(TAG, "onFocusSearch focused " + focused + " + direction " + direction);

            if (getTitleView() != null && focused != getTitleView() &&
                    direction == View.FOCUS_UP) {
                return getTitleView().focusSearch(focused, direction);
            }
            if (getTitleView() != null && getTitleView().hasFocus() &&
                    direction == View.FOCUS_DOWN) {
                return mCanShowHeaders && mShowingHeaders ?
                        mMenuFragment.getVerticalGridView() :
                        mRowsFragment.getVerticalGridView();
            }

            boolean isRtl = ViewCompat.getLayoutDirection(focused) == View.LAYOUT_DIRECTION_RTL;
            int towardStart = isRtl ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
            int towardEnd = isRtl ? View.FOCUS_LEFT : View.FOCUS_RIGHT;
            if (mCanShowHeaders && direction == towardStart) {
                if (isVerticalScrolling() || mShowingHeaders) {
                    return focused;
                }
                return mMenuFragment.getVerticalGridView();
            } else if (direction == towardEnd) {
                if (isVerticalScrolling()) {
                    return focused;
                }
                return mRowsFragment.getVerticalGridView();
            } else {
                return null;
            }
        }
    };

    private final BrowseFrameLayout.OnChildFocusListener mOnChildFocusListener =
            new BrowseFrameLayout.OnChildFocusListener() {

        @Override
        public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
            if (getChildFragmentManager().isDestroyed()) {
                return true;
            }
            // Make sure not changing focus when requestFocus() is called.
            if (mCanShowHeaders && mShowingHeaders) {
                if (mMenuFragment != null && mMenuFragment.getView() != null &&
                        mMenuFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
            if (mRowsFragment != null && mRowsFragment.getView() != null &&
                    mRowsFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
            if (getTitleView() != null &&
                    getTitleView().requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
            return false;
        };

        @Override
        public void onRequestChildFocus(View child, View focused) {
            if (getChildFragmentManager().isDestroyed()) {
                return;
            }
            if (!mCanShowHeaders || isInHeadersTransition()) return;
            int childId = child.getId();
            if (childId == R.id.browse_container_dock && mShowingHeaders) {
                startHeadersTransitionInternal(false);
            } else if (childId == R.id.browse_headers_dock && !mShowingHeaders) {
                startHeadersTransitionInternal(true);
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBackStackChangedListener != null) {
            mBackStackChangedListener.save(outState);
        } else {
            outState.putBoolean(HEADER_SHOW, mShowingHeaders);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypedArray ta = getActivity().obtainStyledAttributes(R.styleable.LeanbackTheme);
        mContainerListMarginStart = (int) ta.getDimension(
                R.styleable.LeanbackTheme_browseRowsMarginStart, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.lb_browse_rows_margin_start));
        mContainerListAlignTop = (int) ta.getDimension(
                R.styleable.LeanbackTheme_browseRowsMarginTop, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.lb_browse_rows_margin_top));
        ta.recycle();

        readArguments(getArguments());

        if (mCanShowHeaders) {
            if (mHeadersBackStackEnabled) {
                mWithHeadersBackStackName = LB_HEADERS_BACKSTACK + this;
                mBackStackChangedListener = new BackStackListener();
                getFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
                mBackStackChangedListener.load(savedInstanceState);
            } else {
                if (savedInstanceState != null) {
                    mShowingHeaders = savedInstanceState.getBoolean(HEADER_SHOW);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mBackStackChangedListener != null) {
            getFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (getChildFragmentManager().findFragmentById(R.id.browse_container_dock) == null) {
            mRowsFragment = new RowsFragment();
            mMenuFragment = new HeadersFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.browse_headers_dock, mMenuFragment)
                    .replace(R.id.browse_container_dock, mRowsFragment).commit();
        } else {
            mMenuFragment = (HeadersFragment) getChildFragmentManager()
                    .findFragmentById(R.id.browse_headers_dock);
            mRowsFragment = (RowsFragment) getChildFragmentManager()
                    .findFragmentById(R.id.browse_container_dock);
        }

        mMenuFragment.setHeadersGone(!mCanShowHeaders);

        mRowsFragment.setAdapter(mRowsAdapter);
        if (mHeaderPresenterSelector != null) {
            mMenuFragment.setPresenterSelector(mHeaderPresenterSelector);
        }
        mMenuFragment.setAdapter(mMenuAdapter);

        mRowsFragment.enableRowScaling(mRowScaleEnabled);
        mRowsFragment.setOnItemViewSelectedListener(mRowViewSelectedListener);
        mMenuFragment.setOnHeaderViewSelectedListener(mHeaderViewSelectedListener);
        mMenuFragment.setOnHeaderClickedListener(mHeaderClickedListener);
        mRowsFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);

        View root = inflater.inflate(R.layout.lb_browse_fragment, container, false);

        setTitleView((TitleView) root.findViewById(R.id.browse_title_group));

        mBrowseFrame = (BrowseFrameLayout) root.findViewById(R.id.browse_frame);
        mBrowseFrame.setOnChildFocusListener(mOnChildFocusListener);
        mBrowseFrame.setOnFocusSearchListener(mOnFocusSearchListener);

        if (mBrandColorSet) {
            mMenuFragment.setBackgroundColor(mBrandColor);
        }

        mSceneWithHeaders = sTransitionHelper.createScene(mBrowseFrame, new Runnable() {
            @Override
            public void run() {
                showHeaders(true);
            }
        });
        mSceneWithoutHeaders =  sTransitionHelper.createScene(mBrowseFrame, new Runnable() {
            @Override
            public void run() {
                showHeaders(false);
            }
        });
        mSceneAfterEntranceTransition = sTransitionHelper.createScene(mBrowseFrame, new Runnable() {
            @Override
            public void run() {
                setEntranceTransitionEndState();
            }
        });
        return root;
    }

    private void createHeadersTransition() {
        mHeadersTransition = sTransitionHelper.loadTransition(getActivity(),
                mShowingHeaders ?
                R.transition.lb_browse_headers_in : R.transition.lb_browse_headers_out);

        sTransitionHelper.setTransitionListener(mHeadersTransition, new TransitionListener() {
            @Override
            public void onTransitionStart(Object transition) {
            }
            @Override
            public void onTransitionEnd(Object transition) {
                mHeadersTransition = null;
                mRowsFragment.onTransitionEnd();
                mMenuFragment.onTransitionEnd();
                if (mShowingHeaders) {
                    VerticalGridView headerGridView = mMenuFragment.getVerticalGridView();
                    if (headerGridView != null && !headerGridView.hasFocus()) {
                        headerGridView.requestFocus();
                    }
                } else {
                    VerticalGridView rowsGridView = mRowsFragment.getVerticalGridView();
                    if (rowsGridView != null && !rowsGridView.hasFocus()) {
                        rowsGridView.requestFocus();
                    }
                }
                if (mBrowseTransitionListener != null) {
                    mBrowseTransitionListener.onHeadersTransitionStop(mShowingHeaders);
                }
            }
        });
    }

    /**
     * Sets the {@link PresenterSelector} used to render the row headers.
     *
     * @param headerPresenterSelector The PresenterSelector that will determine
     *        the Presenter for each row header.
     */
    public void setHeaderPresenterSelector(PresenterSelector headerPresenterSelector) {
        mHeaderPresenterSelector = headerPresenterSelector;
        if (mMenuFragment != null) {
            mMenuFragment.setPresenterSelector(mHeaderPresenterSelector);
        }
    }

    private void setRowsAlignedLeft(boolean alignLeft) {
        MarginLayoutParams lp;
        View containerList;
        containerList = mRowsFragment.getView();
        lp = (MarginLayoutParams) containerList.getLayoutParams();
        lp.setMarginStart(alignLeft ? 0 : mContainerListMarginStart);
        containerList.setLayoutParams(lp);
    }

    private void setHeadersOnScreen(boolean onScreen) {
        MarginLayoutParams lp;
        View containerList;
        containerList = mMenuFragment.getView();
        lp = (MarginLayoutParams) containerList.getLayoutParams();
        lp.setMarginStart(onScreen ? 0 : -mContainerListMarginStart);
        containerList.setLayoutParams(lp);
    }

    private void showHeaders(boolean show) {
        if (DEBUG) Log.v(TAG, "showHeaders " + show);
        mMenuFragment.setHeadersEnabled(show);
        setHeadersOnScreen(show);
        setRowsAlignedLeft(!show);
        mRowsFragment.setExpand(!show);
    }

    private HeadersFragment.OnHeaderClickedListener mHeaderClickedListener =
        new HeadersFragment.OnHeaderClickedListener() {
            @Override
            public void onHeaderClicked() {
                if (!mCanShowHeaders || !mShowingHeaders || isInHeadersTransition()) {
                    return;
                }
                startHeadersTransitionInternal(false);
                mRowsFragment.getVerticalGridView().requestFocus();
            }
        };

    private OnItemViewSelectedListener mRowViewSelectedListener = new OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                RowPresenter.ViewHolder rowViewHolder, Row row) {

            int position = mRowsFragment.getVerticalGridView().getSelectedPosition();
			if (position == 0) {
				mRowsFragment.setWindowAlignmentFromTop(mContainerListFirstItemAlignTop);
				mRowsFragment.setItemAlignment();
			} else {
				mRowsFragment.setWindowAlignmentFromTop(mContainerListAlignTop);
				mRowsFragment.setItemAlignment();
			}
            onRowSelected(position);
			if (row instanceof ListRow) {
				ListRow lr = (ListRow) row;
				if (lr.getAdapter() instanceof ArrayObjectAdapter) {
					ArrayObjectAdapter adapter = (ArrayObjectAdapter) lr.getAdapter();
					mSelectedItemPosition = adapter.indexOf(item);
				}
			}
            if (mExternalOnItemViewSelectedListener != null) {
                mExternalOnItemViewSelectedListener.onItemSelected(itemViewHolder, item,
                        rowViewHolder, row);
            }
        }
    };

    private HeadersFragment.OnHeaderViewSelectedListener mHeaderViewSelectedListener =
            new HeadersFragment.OnHeaderViewSelectedListener() {
        @Override
        public void onHeaderSelected(RowHeaderPresenter.ViewHolder viewHolder, Row row) {
            int position = mMenuFragment.getVerticalGridView().getSelectedPosition();
            if (DEBUG) Log.v(TAG, "header selected position " + position);
            setRowsAdapter(null);
            if (mExternalOnMenuItemSelectedListener != null) {
            	mExternalOnMenuItemSelectedListener.onMenuItemSelected(row, position);
            }
        }
    };

    private void onRowSelected(int position) {
        if (position != mSelectedRowPosition) {
            mSetSelectionRunnable.post(
                    position, SetSelectionRunnable.TYPE_INTERNAL_SYNC, true);

        }
        if (getRowsAdapter() == null || getRowsAdapter().size() == 0 || position == 0) {
            showTitle(true);
        } else {
            showTitle(false);
        }
    }
    
    private void setSelection(int position, boolean smooth) {
        if (position != NO_POSITION) {
            mRowsFragment.setSelectedPosition(position, smooth);
        }
        mSelectedRowPosition = position;
    }

    /**
     * Sets the selected row position with smooth animation.
     */
    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    /**
     * Sets the selected row position.
     */
    public void setSelectedPosition(int position, boolean smooth) {
        mSetSelectionRunnable.post(
                position, SetSelectionRunnable.TYPE_USER_REQUEST, smooth);
    }

    public void setMenuSelectedPosition(int position) {
        if (position != NO_POSITION) {
            mMenuFragment.setSelectedPosition(position);
        }
    }
    
    public int getSelectedRowPosition() {
    	return mRowsFragment.getVerticalGridView().getSelectedPosition();
    }
//    
//    public int getSelectedItemPosition() {
//    	return mSelectedRowPosition;
//    }
    
    public int getSelectedItemPosition() {
    	return mSelectedItemPosition;
    }
    
    public int getSelectedRowItemCount() {
    	int selectedRow = getSelectedRowPosition();
    	try {
	    	ListRow row = (ListRow) mRowsAdapter.get(selectedRow);
	    	return row.getAdapter().size();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return 0;
    }
    
    public Object getFirstItem() {
    	int selectedRow = getSelectedRowPosition();
    	try {
	    	ListRow row = (ListRow) mRowsAdapter.get(selectedRow);
	    	return row.getAdapter().get(0);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMenuFragment.setWindowAlignmentFromTop(mContainerListAlignTop);
        mMenuFragment.setItemAlignment();
        mRowsFragment.setScalePivots(0, mContainerListAlignTop);

        if (mCanShowHeaders && mShowingHeaders && mMenuFragment.getView() != null) {
            mMenuFragment.getView().requestFocus();
        } else if ((!mCanShowHeaders || !mShowingHeaders)
                && mRowsFragment.getView() != null) {
            mRowsFragment.getView().requestFocus();
        }
        if (mCanShowHeaders) {
            showHeaders(mShowingHeaders);
        }
        if (isEntranceTransitionEnabled()) {
            setEntranceTransitionStartState();
        }
    }

    /**
     * Enables/disables headers transition on back key support. This is enabled by
     * default. The BrowseFragment will add a back stack entry when headers are
     * showing. Running a headers transition when the back key is pressed only
     * works when the headers state is {@link #HEADERS_ENABLED} or
     * {@link #HEADERS_HIDDEN}.
     * <p>
     * NOTE: If an Activity has its own onBackPressed() handling, you must
     * disable this feature. You may use {@link #startHeadersTransition(boolean)}
     * and {@link BrowseTransitionListener} in your own back stack handling.
     */
    public final void setHeadersTransitionOnBackEnabled(boolean headersBackStackEnabled) {
        mHeadersBackStackEnabled = headersBackStackEnabled;
    }

    /**
     * Returns true if headers transition on back key support is enabled.
     */
    public final boolean isHeadersTransitionOnBackEnabled() {
        return mHeadersBackStackEnabled;
    }

    private void readArguments(Bundle args) {
        if (args == null) {
            return;
        }
        if (args.containsKey(ARG_TITLE)) {
            setTitle(args.getString(ARG_TITLE));
        }
        if (args.containsKey(ARG_HEADERS_STATE)) {
            setHeadersState(args.getInt(ARG_HEADERS_STATE));
        }
    }

    /**
     * Sets the state for the headers column in the browse fragment. Must be one
     * of {@link #HEADERS_ENABLED}, {@link #HEADERS_HIDDEN}, or
     * {@link #HEADERS_DISABLED}.
     *
     * @param headersState The state of the headers for the browse fragment.
     */
    public void setHeadersState(int headersState) {
        if (headersState < HEADERS_ENABLED || headersState > HEADERS_DISABLED) {
            throw new IllegalArgumentException("Invalid headers state: " + headersState);
        }
        if (DEBUG) Log.v(TAG, "setHeadersState " + headersState);

        if (headersState != mHeadersState) {
            mHeadersState = headersState;
            switch (headersState) {
                case HEADERS_ENABLED:
                    mCanShowHeaders = true;
                    mShowingHeaders = true;
                    break;
                case HEADERS_HIDDEN:
                    mCanShowHeaders = true;
                    mShowingHeaders = false;
                    break;
                case HEADERS_DISABLED:
                    mCanShowHeaders = false;
                    mShowingHeaders = false;
                    break;
                default:
                    Log.w(TAG, "Unknown headers state: " + headersState);
                    break;
            }
            if (mMenuFragment != null) {
                mMenuFragment.setHeadersGone(!mCanShowHeaders);
            }
        }
    }

    /**
     * Returns the state of the headers column in the browse fragment.
     */
    public int getHeadersState() {
        return mHeadersState;
    }

    @Override
    protected Object createEntranceTransition() {
        return sTransitionHelper.loadTransition(getActivity(),
                R.transition.lb_browse_entrance_transition);
    }

    @Override
    protected void runEntranceTransition(Object entranceTransition) {
        sTransitionHelper.runTransition(mSceneAfterEntranceTransition,
                entranceTransition);
    }

    @Override
    protected void onEntranceTransitionStart() {
        mMenuFragment.onTransitionStart();
        mRowsFragment.onTransitionStart();
    }

    @Override
    protected void onEntranceTransitionEnd() {
        mRowsFragment.onTransitionEnd();
        mMenuFragment.onTransitionEnd();
    }

    void setSearchOrbViewOnScreen(boolean onScreen) {
        View searchOrbView = getTitleView().getSearchAffordanceView();
        MarginLayoutParams lp = (MarginLayoutParams) searchOrbView.getLayoutParams();
        lp.setMarginStart(onScreen ? 0 : -mContainerListMarginStart);
        searchOrbView.setLayoutParams(lp);
    }

    void setEntranceTransitionStartState() {
        setHeadersOnScreen(false);
        setSearchOrbViewOnScreen(false);
        mRowsFragment.setEntranceTransitionState(false);
    }

    void setEntranceTransitionEndState() {
        setHeadersOnScreen(mShowingHeaders);
        setSearchOrbViewOnScreen(true);
        mRowsFragment.setEntranceTransitionState(true);
    }
    
    public View getRowsGridView() {
    	return mRowsFragment.getVerticalGridView();
    }
    
    public TitleView getTitleView() {
    	return super.getTitleView();
    }

}
