package com.junruyi.utils;

import java.io.IOException;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


public class LocationTool {
	private LocationManager locationManager;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private String addrString;
	
	private Context context;
	public LocationTool(Context context,LocationManager locationManager){
		this.context = context;
		this.locationManager = locationManager;
		getLocation();
		addrString = getAddr();
	}
	
	public String getAddr() {
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());  
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
		}

		// Provider被disable时触发此函数，比如GPS被关闭
		@Override
		public void onProviderDisabled(String provider) {
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
	
	public String getAddrString(){
		return addrString;
	}
}
