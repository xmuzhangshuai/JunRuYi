package com.junruyi.ui;

import com.junruyi.base.BaseFragmentActivity;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.WifiDbService;
import com.junruyi.entities.Wifi;
import com.junruyi.utils.WifiUtil;
import com.smallrhino.junruyi.R;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
	private WifiDbService wifiDbService;
	private WifiUtil wifiUtil;

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
		showAddWifi();
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
	 * 判断当前wifi是否需要添加到数据库
	 */
	private void showAddWifi() {
		wifiDbService = WifiDbService.getInstance(MainActivity.this);
		wifiUtil = new WifiUtil(this);
		String bssid = wifiUtil.getBSSID();
		String name = wifiUtil.getName();
		if (!bssid.isEmpty()) {
			long count = wifiDbService.countWifi();
			Wifi temp  = wifiDbService.getWifiByBssid(bssid);
			if (count < 6 && temp==null) {
				//添加当前无线到安全wifi
				addWifi(name,bssid);
			}
		}
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
	
	/**
	 * 添加安全wifi的方法
	 */
	public void addWifi(final String wifiName, final String bssid) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否设置当前连接的wifi为安全？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击确认则做删除安全wifi操作
				wifiDbService.addWifi(wifiName, bssid);
				myAlertDialog.dismiss();
			}
		};
		View.OnClickListener cancel = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("确定", comfirm);
		myAlertDialog.setNegativeButton("取消", cancel);
		myAlertDialog.show();
	}
}
