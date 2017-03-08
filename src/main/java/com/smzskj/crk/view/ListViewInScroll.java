package com.smzskj.crk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ztt on 2017/2/13.
 * <p>
 * <p>
 * ScrollView 嵌套 ListView
 */

public class ListViewInScroll extends ListView {
	public ListViewInScroll(Context context) {
		super(context);
	}

	public ListViewInScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewInScroll(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
}
