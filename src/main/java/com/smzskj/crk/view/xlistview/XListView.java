package com.smzskj.crk.view.xlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.smzskj.crk.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class XListView extends ListView implements OnScrollListener {

	/** 记录Y */
	private float mLastY = -1;
	/** 用户滚动，用于回滚 */
	private Scroller mScroller;
	/** 用户滚动监听 */
	private OnScrollListener mScrollListener;
	/** 触发刷新加载接口 */
	private IXListViewListener mListViewListener;
	/** 头部View */
	private XListViewHeader mHeaderView;
	/** 头部View内容，用它来计算标题的高度。并把它藏在禁用拉刷新。 */
	private RelativeLayout mHeaderViewContent;
	/** 顶部View文字 */
	private TextView mHeaderTimeView;
	/** 头部View高度  */
	private int mHeaderViewHeight;
	/** 是否可以下拉刷新 */
	private boolean mEnablePullRefresh = true;
	/** 是否正在刷新 */
	private boolean mPullRefreshing = false;
	/** 底部View */
	private XListViewFooter mFooterView;
	/** 是否可以上拉加载 */
	private boolean mEnablePullLoad;
	/** 是否正在加载 */
	private boolean mPullLoading;
	/**  */
	private boolean mIsFooterReady = false;
	/** 总列表项，用于检测是否在底部 */
	private int mTotalItemCount;
	/** 对mScroller滚动到顶部或底部 */
	private int mScrollBack;
	/** 滚动到顶部 */
	private final static int SCROLLBACK_HEADER = 0;
	/** 滚动到底部 */
	private final static int SCROLLBACK_FOOTER = 1;
	/** 回滚时间 */
	private final static int SCROLL_DURATION = 400; // scroll back duration
	/** 当上拉 >50 px 触发加载*/
	private final static int PULL_LOAD_MORE_DELTA = 50;
	/** 手指下滑对应往下滚动比例 */
	private final static float OFFSET_RADIO = 0.7f;

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}
	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}
	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}
	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// 设置监听滚动时间
		super.setOnScrollListener(this);
		// 初始化头部View
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);
		// 初始化底部View
		mFooterView = new XListViewFooter(context);
		// 初始化头部View高度
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// 确保底部View只添加一次
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 启用或禁用下拉刷新
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content 禁用隐藏
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 启动或禁用上拉加载
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			//设置可以或者不可以为页脚视图画上分隔符。
			setFooterDividersEnabled(false);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			//设置可以或者不可以为页脚视图画上分隔符。
			setFooterDividersEnabled(true);
			// 点击也触发加载更多
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 * 停止刷新重置头部View
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 * 停止加载更多，重置底部视图
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 设置最后一次刷新时间
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	public void setRefreshTime() {
		Date nowTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		mHeaderTimeView.setText(format.format(nowTime));
	}
	/** 设置滚动监听 */
	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}
	/** 更新头部View高度 */
	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // 滚动到顶部 scroll to top each time
	}

	/**
	 * reset header view's height.
	 *	重置顶部View高度
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.不可见
			return;
		// refreshing and header isn't shown fully. do nothing.
		// 正在刷新或者没有显示，不做任何处理
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		// 默认回滚到顶部
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		// 如果正在刷新，回滚到显示头部View的高度
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		// trigger computeScroll 重绘
		invalidate();
	}

	/** 更新底部View高度 */
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { //高度满足加载更多50px  height enough to invoke load more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	/** 重新设置底部View高度 */
	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}
	/** 启动加载更多 */
	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY(); //触摸点相对于屏幕的坐标
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY; // Y变化
			mLastY = ev.getRawY();
			// 屏幕中显示的第一条数据的Position == 0 并且头部View正在显示 或者正在往下滑动(屏幕左上角为00)
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down. 第一项是显示，头显示或下拉。
				updateHeaderHeight(deltaY * OFFSET_RADIO);
				invokeOnScrolling(); // 设置滚动监听
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up. 显示最后一项或者已经拉起来或正在往上拉
				updateFooterHeight(-deltaY * OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1; // reset 重置
			if (getFirstVisiblePosition() == 0) { // 判断显示的第一项是否是List的第一项
				// 可以下拉刷新，并且 头部视图显示的高度大于视图内容高度
				if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) { // 监听器不等于空
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight(); // 重新设置高度，显示整个头部视图
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {// 判断最底部显示的是否是List的最后一项

				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
						&& !mPullLoading) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener 发送到用户监听器
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
		}
	}

	/** 设置监听 */
	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one.
	 * it will invoke onXScrolling when header/footer scroll back.
	 * 可以监听ListView.OnScrollListener或者这个接口，他会调用
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 *  实现这个接口 完成刷新加载事件
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}
}
