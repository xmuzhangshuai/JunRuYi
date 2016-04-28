package com.junruyi.ui;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.smallrhino.junruyi.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class LocationActivity extends Activity {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	double longitude=0,latitude=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_location);

		longitude = getIntent().getDoubleExtra("longitude", 0);
		latitude = getIntent().getDoubleExtra("latitude", 0);
		System.out.println(longitude+"--"+latitude);
		if(longitude==0&&latitude==0)
		{
			latitude = 24.4442430000;
			longitude = 118.1303220000;
		}
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
		LatLng point = new LatLng(latitude, longitude);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		mBaiduMap.addOverlay(option);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
}
