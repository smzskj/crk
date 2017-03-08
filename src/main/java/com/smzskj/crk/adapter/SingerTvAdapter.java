package com.smzskj.crk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;

import java.util.List;

/**
 * Created by ztt on 2017/2/24.
 */

public class SingerTvAdapter extends BaseViewAdapter<String> {

	public SingerTvAdapter(Context context, List<String> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_tv, parent, false);
		}
		TextView textView = BaseViewHolder.get(convertView, R.id.item_tv_tv);
		textView.setText(list.get(position));
		return convertView;
	}
}