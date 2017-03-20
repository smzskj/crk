package com.smzskj.crk.offline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;

import java.util.List;

/**
 * Created by ztt on 2017/3/10.
 */

public class SbhAdapter extends BaseViewAdapter<String> {

	public SbhAdapter(Context context, List<String> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_tv, parent, false);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.item_tv_tv);
		textView.setText(list.get(position));
		return convertView;
	}
}
