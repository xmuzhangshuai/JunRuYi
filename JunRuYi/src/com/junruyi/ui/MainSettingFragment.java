package com.junruyi.ui;

import java.io.File;

import com.junruyi.base.BaseApplication;
import com.junruyi.base.BaseV4Fragment;
import com.junruyi.customewidget.CustomDialog;
import com.junruyi.utils.UserPreference;
import com.smallrhino.junruyi.R;

import android.R.string;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @description:设置页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:37:11
 */
public class MainSettingFragment extends BaseV4Fragment implements
		OnClickListener {
	public final static String TAG = "MainSettingFragment";
	private View rootView;// 根View
	private RelativeLayout rl_switch_warn;// 设置报警免打扰
	private ImageView iv_switch_open_warn;// 打开报警免打扰imageView
	private ImageView iv_switch_close_warn;// 关闭报警免打扰imageview

	private RelativeLayout rl_switch_notification;// 设置新消息通知布局
	private ImageView iv_switch_open_notification;// 打开新消息通知imageView
	private ImageView iv_switch_close_notification;// 关闭新消息通知imageview

	private View settingEquipment;// 自拍
	private View settingPhoto;// 自拍
	private View settingIntro;// 使用介绍
	private View settingRefresh;// 版本检测
	private View settingAbout;// 关于君儒艺

	private UserPreference userPreference;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater
				.inflate(R.layout.fragment_setting, container, false);
		userPreference = BaseApplication.getInstance().getUserPreference();
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		rl_switch_warn = (RelativeLayout) rootView
				.findViewById(R.id.rl_switch_warn);
		iv_switch_open_warn = (ImageView) rootView
				.findViewById(R.id.iv_switch_open_warn);
		iv_switch_close_warn = (ImageView) rootView
				.findViewById(R.id.iv_switch_close_warn);
		rl_switch_notification = (RelativeLayout) rootView
				.findViewById(R.id.rl_switch_notificatoin);
		iv_switch_open_notification = (ImageView) rootView
				.findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) rootView
				.findViewById(R.id.iv_switch_close_notification);
		settingEquipment = rootView.findViewById(R.id.setting_equipment);
		settingPhoto = rootView.findViewById(R.id.setting_photo);
		settingIntro = rootView.findViewById(R.id.setting_intro);
		settingRefresh = rootView.findViewById(R.id.setting_refresh);
		settingAbout = rootView.findViewById(R.id.setting_about);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		rl_switch_warn.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		settingEquipment.setOnClickListener(this);
		settingPhoto.setOnClickListener(this);
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
				// 设置为报警,true为报警，false为报警免打扰
				userPreference.setNoWarn(true);
			} else {
				iv_switch_open_warn.setVisibility(View.VISIBLE);
				iv_switch_close_warn.setVisibility(View.INVISIBLE);
				// 设置为不报警
				userPreference.setNoWarn(false);
			}
			break;
		case R.id.rl_switch_notificatoin:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				// 设置为不接收消息通知
				userPreference.setInfoNotify(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				// 设置为接收消息通知
				userPreference.setInfoNotify(true);
			}
			break;
		case R.id.setting_equipment:
			dialog();
			break;
		case R.id.setting_photo:
			Toast.makeText(getActivity(), "自拍功能...", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getActivity(), RectPhoto.class);
			startActivity(intent);
			break;
		case R.id.setting_intro:
			Toast.makeText(getActivity(), "君儒艺app使用介绍...", Toast.LENGTH_SHORT)
					.show();
			break;

		case R.id.setting_refresh:
			Toast.makeText(getActivity(), "版本检测尚未接入...", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.setting_about:
			Toast.makeText(getActivity(), "君儒艺项目介绍...", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}
	}

	// 弹窗
	private void dialog() {
		final CustomDialog dialog = new CustomDialog(getActivity());
		final EditText editText = (EditText) dialog.getEditText();// 方法在CustomDialog中实现
		dialog.setOnPositiveListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// dosomething youself
				String tel = editText.getText().toString(); 
				Toast.makeText(getActivity(), "已经保存"+tel, Toast.LENGTH_SHORT).show();
				userPreference.setU_tel(tel);
				dialog.dismiss();
			}
		});
		dialog.setOnNegativeListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
