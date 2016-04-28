package com.junruyi.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.junruyi.base.BaseFragmentActivity;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.EquipmentDbService;
import com.junruyi.db.LocationDbService;
import com.junruyi.db.WifiDbService;
import com.junruyi.entities.EquipMent;
import com.junruyi.entities.Location;
import com.junruyi.entities.Wifi;
import com.junruyi.utils.FastJsonTool;
import com.junruyi.utils.WifiUtil;
import com.smallrhino.junruyi.R;
import com.umeng.update.UmengUpdateAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
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
	private MainSettingFragment settingFragment;// 设置页面

	private int index;
	// 当前fragment的index
	private int currentTabIndex = 0;
	private Fragment[] fragments;
	
	private LocationDbService locationDbService;

	private DisconnectReceiver disconnectReceiver;
	private EquipmentDbService equipmentDbService;

	private LocationManager locationManager;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private String address = "";

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
		fragments = new Fragment[] { equipmentFragment, wifiFragment,
				locationFragment, marketFragment, settingFragment };
		findViewById();
		initView();
		// 丢失定位广播
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		equipmentDbService = EquipmentDbService.getInstance(this);
		locationDbService = LocationDbService.getInstance(this);

		disconnectReceiver = new DisconnectReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.xxn.location");
		registerReceiver(disconnectReceiver, intentFilter);

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
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, equipmentFragment,
						MainEquipmentFragment.TAG).show(equipmentFragment)
				.commit();
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
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {

				if (index == 0) {
					trx.add(R.id.fragment_container, fragments[index],
							MainEquipmentFragment.TAG);
				} else if (index == 1) {
					trx.add(R.id.fragment_container, fragments[index],
							MainWifiFragment.TAG);
				} else if (index == 2) {
					trx.add(R.id.fragment_container, fragments[index],
							MainLocationFragment.TAG);
				} else if (index == 3) {
					trx.add(R.id.fragment_container, fragments[index],
							MainMarketFragment.TAG);
				} else if (index == 4) {
					trx.add(R.id.fragment_container, fragments[index],
							MainSettingFragment.TAG);
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
	 * 
	 * @ClassName: MsgReceiver
	 * @Description: TODO 蓝牙丢失定位
	 * @author kunsen-lee
	 * @date 2016年4月8日 上午9:43:03
	 * 
	 */
	public class DisconnectReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拿到数据
			String addr = intent.getStringExtra("addr");
			System.out.println("断开的设备addr:" + addr);
			if (!addr.isEmpty()) {
				EquipMent e = equipmentDbService.getEquipMentByAddr(addr);
				String name = e.getEquipMentName();
				System.out.println("addr:" + addr + "name:" + name);
				Toast.makeText(MainActivity.this, name + "断开了",
						Toast.LENGTH_SHORT).show();
				// 调用获取地理位置
				getLocation();
				String address = getAddr();
				long i = locationDbService.countLocation() + 1;
				Location location = new Location(i, name, latitude + "",
						longitude + "", address, new Date());
				locationDbService.addLocation(location);
				Intent speed = new Intent();
				speed.setAction("com.xxn.speed");
				speed.putExtra("danger", 2.0d);
				MainActivity.this.sendBroadcast(speed);
			}
		}
	}

	public String getAddr() {
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());  
		Address addresses = null;
		StringBuilder sb = new StringBuilder();  
		try {
			 addresses = geocoder.getFromLocation(latitude, longitude, 1).get(0);
//			 for (int i = 0; i < addresses.getMaxAddressLineIndex(); i++) {  
                 sb.append(addresses.getAddressLine(0)).append("\n");  
//             }  
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private void getLocation() {
		android.location.Location loca = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loca != null) {
			latitude = loca.getLatitude();
			longitude = loca.getLongitude();
		} else {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
		}
	}

	public LocationListener locationListener = new LocationListener() {
		// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// Provider被enable时触发此函数，比如GPS被打开
		@Override
		public void onProviderEnabled(String provider) {
			Log.e(TAG, provider);
		}

		// Provider被disable时触发此函数，比如GPS被关闭
		@Override
		public void onProviderDisabled(String provider) {
			Log.e(TAG, provider);
		}

		// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发

		@Override
		public void onLocationChanged(android.location.Location location) {
			if (location != null) {
				latitude = location.getLatitude(); // 经度
				longitude = location.getLongitude(); // 纬度
			}
		}
		
	};

}
