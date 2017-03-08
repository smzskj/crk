/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.smzskj.crk.view.xlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smzskj.crk.R;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	/** 旋转到向上 */
	private Animation mRotateUpAnim;
	/** 旋转到向下 */
	private Animation mRotateDownAnim;
	private final int ROTATE_ANIM_DURATION = 180;

	/** 顶部View空闲 */
	public final static int STATE_NORMAL = 0;
	/** 顶部View准备好 */
	public final static int STATE_READY = 1;
	/** 顶部View正在刷新 */
	public final static int STATE_REFRESHING = 2;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 *	将XMLView添加到当前LinearLayout中
	 *	初始化箭头的动画
	 */
	@SuppressLint("InflateParams") private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null ,false);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

		/*
		 *从0旋转到-180
		 **/
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		/*
		 *从-180旋转到0
		 **/
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	/**
	 * 设置状态
	 */
	public void setState(int state) {
		if (state == mState) // 状态相同不做任何处理
			return;
		if (state == STATE_REFRESHING) { // 状态切换到刷新状态 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else { //准备状态   显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		switch (state) {
		case STATE_NORMAL: // 当前状态为空闲状态，原来为其他状态转为空闲
			if (mState == STATE_READY) { // 原状态为准备状态
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) { // 原状态为准备
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY: // 转为准备状态
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING: // 转为刷新状态
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			break;
		default:
		}
		mState = state;
	}
	/**
	 *  设置显示的高度
	 * */
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}
	/**
	 * 得到当前高度
	 * */
	public int getVisiableHeight() {
		return mContainer.getLayoutParams().height;
	}
}
