package com.junruyi.ui;

import com.junruyi.base.BaseFragmentActivity;
import com.smallrhino.junruyi.R;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

/**
 * @description:
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:33:01
 */
public class MainActivity extends BaseFragmentActivity {
	private View[] mTabs;
	private MainEquipmentFragment equipmentFragment;// 设备页面
	private MainWifiFragment wifiFragment;// wifi页面
	private MainLocationFragment locationFragment;// 地址页面
	private MainMarketFragment marketFragment;// 商城页面
	private MainSettingFragment settingFragment;//设置页面

	private int index;
	// 当前fragment的index
	private int currentTabIndex = 0;
	private Fragment[] fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		equipmentFragment = new MainEquipmentFragment();
		wifiFragment = new MainWifiFragment();
		locationFragment = new MainLocationFragment();
		marketFragment = new MainMarketFragment();
		settingFragment = new MainSettingFragment();
		fragments = new Fragment[] { equipmentFragment, wifiFragment, locationFragment, marketFragment, settingFragment };

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mTabs = new View[5];
		mTabs[0] = (View) findViewById(R.id.btn_container_equipment);
		mTabs[1] = (View) findViewById(R.id.btn_container_wifi);
		mTabs[2] = (View) findViewById(R.id.btn_container_location);
		mTabs[3] = (View) findViewById(R.id.btn_container_market);
		mTabs[4] = (View) findViewById(R.id.btn_container_setting);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, equipmentFragment, MainEquipmentFragment.TAG).show(equipmentFragment).commit();
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_container_equipment:
			index = 0;
			break;
		case R.id.btn_container_wifi:
			index = 1;
			break;
		case R.id.btn_container_location:
			index = 2;
			break;
		case R.id.btn_container_market:
			index = 3;
			break;
		case R.id.btn_container_setting:
			index = 4;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {

				if (index == 0) {
					trx.add(R.id.fragment_container, fragments[index], MainEquipmentFragment.TAG);
				} else if (index == 1) {
					trx.add(R.id.fragment_container, fragments[index], MainWifiFragment.TAG);
				} else if (index == 2) {
					trx.add(R.id.fragment_container, fragments[index], MainLocationFragment.TAG);
				} else if (index == 3) {
					trx.add(R.id.fragment_container, fragments[index], MainMarketFragment.TAG);
				} else if (index == 4) {
					trx.add(R.id.fragment_container, fragments[index], MainSettingFragment.TAG);
				}
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
}
