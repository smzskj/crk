package com.smzskj.crk.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @Description:自定义的BaseAdapter，所有adapter的父类。
 */
public abstract class BaseViewAdapter<E> extends BaseAdapter {

	public Context context;
	public List<E> list;//
	// public V view; // 这里不一定是ListView,比如GridView,CustomListView
	public LayoutInflater inflater = null;

	public BaseViewAdapter(Context context, List<E> list) {
		this.context = context;
		this.list = list;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * update
	 * 
	 * @param list
	 */
	public void updateListView(List<E> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		BaseViewHolder holder;
//		if (null == convertView) {
//			holder = new BaseViewHolder();
//			convertView = LayoutInflater.from(context).inflate(itemLayoutRes(), null);
//			convertView.setTag(holder);
//		} else {
//			holder = (BaseViewHolder) convertView.getTag();
//		}
//		return getView(position, convertView, parent, holder);
//	}
//	
//	 public abstract View getView(int position, View convertView, ViewGroup parent, BaseViewHolder holder);
}
