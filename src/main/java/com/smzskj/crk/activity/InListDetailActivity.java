package com.smzskj.crk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.InDetailBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/2/9.
 */

public class InListDetailActivity extends BaseActivity implements XListView.IXListViewListener {


	private String djh;
	private XListView listView;
	private List<InDetailBean.RowsBean> datas = new ArrayList<>();
	private DetailAdapter adapter;

	public static void startInListDetailActivity(Context context, String djh) {
		Intent intent = new Intent(context, InListDetailActivity.class);
		intent.putExtra("djh", djh);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_in_list_detail);

		djh = getIntent().getStringExtra("djh");
		addBackListener();
		setTitle("入库详情");
		listView = findView(R.id.in_detail_lv);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);

		listView.setXListViewListener(this);
		adapter = new DetailAdapter(mContext, datas);
		listView.setAdapter(adapter);

		onRefresh();
	}


	/**
	 * 入库信息查询
	 */
	private void rk_get_rkinfo_detail(int page) {
		showLoadDialog();
		String pageStr = "" + page;
		HttpJsonRequest request = new HttpJsonRequest(new InBackListener(),
				Method.SERVICE_NAME_RK, Method.RK_GET_RKINFO_DETAIL
				, djh, UserInfo.RY_NAME, pageStr, pageSize);
		ThreadPoolUtils.execute(request);
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		datas.clear();
		adapter.notifyDataSetChanged();
		rk_get_rkinfo_detail(currentPage);
	}

	@Override
	public void onLoadMore() {
		rk_get_rkinfo_detail(currentPage);
	}


	class InBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			listView.stopRefresh();
			listView.stopLoadMore();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			InDetailBean inBean = JsonUtils.getJsonParseObject(result, InDetailBean.class);
			if (inBean == null || inBean.getRows() == null || inBean.getRows().size() == 0) {
				makeShortToase(R.string.loading_empty);
			} else {
				datas.addAll(inBean.getRows());
				adapter.notifyDataSetChanged();
				if (currentPage < Integer.valueOf(inBean.getTotalPage())) {
					currentPage++;
					listView.setPullLoadEnable(true);
				} else {
					listView.setPullLoadEnable(false);
				}
			}
		}
	}

	class DetailAdapter extends BaseViewAdapter<InDetailBean.RowsBean> {

		public DetailAdapter(Context context, List<InDetailBean.RowsBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_in_detail, parent, false);
			}

			TextView tvPch = BaseViewHolder.get(convertView,R.id.detail_item_tv_pch);
			TextView tvDjh = BaseViewHolder.get(convertView,R.id.detail_item_tv_djhm);
			TextView tvBh = BaseViewHolder.get(convertView,R.id.detail_item_tv_bh);
			TextView tvSpm = BaseViewHolder.get(convertView,R.id.detail_item_tv_spmc);
			TextView tvRks = BaseViewHolder.get(convertView,R.id.detail_item_tv_rks);
			TextView tvRkdd = BaseViewHolder.get(convertView,R.id.detail_item_tv_rkdd);
			TextView tvRq = BaseViewHolder.get(convertView,R.id.detail_item_tv_rq);

			tvPch.setText("批次号:" + list.get(position).get批次号());
			tvDjh.setText("单据号:" + list.get(position).get单据号码());
			tvBh.setText("编号:" + list.get(position).get编号());
			tvSpm.setText("商品名:" + list.get(position).get商品名称());
			tvRks.setText("入库数:" + list.get(position).get入库数());
			tvRkdd.setText("入库地点:" + list.get(position).get入库地点());
			tvRq.setText("日期:" + list.get(position).get日期());

			return convertView;
		}
	}
}
