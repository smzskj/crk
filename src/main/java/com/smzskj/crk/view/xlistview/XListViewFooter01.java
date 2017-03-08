/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.smzskj.crk.view.xlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smzskj.crk.R;

public class XListViewFooter01 extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private RelativeLayout mContentView;
	private TextView mHintView;
	private ImageView img;

	public XListViewFooter01(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter01(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	/**
	 * 初始化底部View
	 * @param context
	 */
	@SuppressLint("InflateParams") private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer01, null,false);
		addView(moreView);// 将View添加到本LinearLayout中，并设置位置信息
		moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = (RelativeLayout) moreView.findViewById(R.id.footer_rl);
		img = (ImageView) moreView.findViewById(R.id.footer_img);
		mHintView = (TextView) moreView.findViewById(R.id.footer_txt);
	}

	/**
	 * 设置状态
	 * @param state
	 */
	public void setState(int state) {
		mHintView.setVisibility(View.INVISIBLE);
		img.setVisibility(View.INVISIBLE);
		mHintView.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			img.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	/**
	 * normal status
	 * 正常状态
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		img.setVisibility(View.GONE);
	}
	/**
	 * loading status
	 * 加载状态
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		img.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 * 隐藏页脚时禁止上拉载更多
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}


}
