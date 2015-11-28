package com.junruyi.ui;

import com.junruyi.base.BaseV4Fragment;
import com.smallrhino.junruyi.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * @description:商城页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:36:47
 */
public class MainMarketFragment extends BaseV4Fragment implements OnClickListener {
	public final static String TAG = "MainMarketFragment";
	private View rootView;// 根View

	private View linear0, linear1, linear2, linear3;
	private View wxstore, tbstore;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_market, container, false);
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		linear0 = rootView.findViewById(R.id.l0);
		linear1 = rootView.findViewById(R.id.l1);
		linear2 = rootView.findViewById(R.id.l2);
		linear3 = rootView.findViewById(R.id.l3);
		wxstore = rootView.findViewById(R.id.wxstore);
		tbstore = rootView.findViewById(R.id.tbstore);
	}

	@Override
	protected void initView() {
		linear0.setOnClickListener(this);
		linear1.setOnClickListener(this);
		linear2.setOnClickListener(this);
		linear3.setOnClickListener(this);
		wxstore.setOnClickListener(this);
		tbstore.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.l0:
			gotoWeb();
			break;
		case R.id.l1:
			gotoWeb();
			break;
		case R.id.l2:
			gotoWeb();
			break;
		case R.id.l3:
			gotoWeb();
			break;
		case R.id.tbstore:
			gotoWeb();
			break;
		case R.id.wxstore:
			gotoWeb();
			break;
		}
	}

	/**
	 * 跳转到商城网站
	 */
	public void gotoWeb() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse("http://www.smallrhino.net");
		intent.setData(content_url);
		startActivity(intent);
	}

}
