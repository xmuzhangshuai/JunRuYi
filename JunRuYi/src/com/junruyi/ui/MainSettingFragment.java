package com.junruyi.ui;

import com.junruyi.base.BaseV4Fragment;
import com.smallrhino.junruyi.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @description:设置页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:37:11
 */
public class MainSettingFragment extends BaseV4Fragment implements OnClickListener {
	public final static String TAG = "MainSettingFragment";
	private View rootView;// 根View
	private RelativeLayout rl_switch_warn;// 设置报警免打扰
	private ImageView iv_switch_open_warn;// 打开报警免打扰imageView
	private ImageView iv_switch_close_warn;// 关闭报警免打扰imageview

	private RelativeLayout rl_switch_notification;// 设置新消息通知布局
	private ImageView iv_switch_open_notification;// 打开新消息通知imageView
	private ImageView iv_switch_close_notification;// 关闭新消息通知imageview

	private View settingIntro;// 使用介绍
	private View settingRefresh;// 版本检测
	private View settingAbout;// 关于君儒艺

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_setting, container, false);
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		rl_switch_warn = (RelativeLayout) rootView.findViewById(R.id.rl_switch_warn);
		iv_switch_open_warn = (ImageView) rootView.findViewById(R.id.iv_switch_open_warn);
		iv_switch_close_warn = (ImageView) rootView.findViewById(R.id.iv_switch_close_warn);
		rl_switch_notification = (RelativeLayout) rootView.findViewById(R.id.rl_switch_notificatoin);
		iv_switch_open_notification = (ImageView) rootView.findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) rootView.findViewById(R.id.iv_switch_close_notification);
		settingIntro = rootView.findViewById(R.id.setting_intro);
		settingRefresh = rootView.findViewById(R.id.setting_refresh);
		settingAbout = rootView.findViewById(R.id.setting_about);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		rl_switch_warn.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		settingIntro.setOnClickListener(this);
		settingRefresh.setOnClickListener(this);
		settingAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_switch_warn:
			if (iv_switch_open_warn.getVisibility() == View.VISIBLE) {
				iv_switch_open_warn.setVisibility(View.INVISIBLE);
				iv_switch_close_warn.setVisibility(View.VISIBLE);
			} else {
				iv_switch_open_warn.setVisibility(View.VISIBLE);
				iv_switch_close_warn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.rl_switch_notificatoin:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.setting_intro:

			break;
		case R.id.setting_refresh:

			break;
		case R.id.setting_about:

			break;

		default:
			break;
		}
	}

}
