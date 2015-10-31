package com.junruyi.ui;

import com.junruyi.base.BaseV4Fragment;
import com.smallrhino.junruyi.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @description:Wifi列表
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:35:40
 */
public class MainWifiFragment extends BaseV4Fragment {
	public final static String TAG = "MainWifiFragment";
	private View rootView;// 根View

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

}
