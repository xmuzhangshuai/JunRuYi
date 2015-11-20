package com.junruyi.db;

import java.util.List;

import com.junruyi.base.BaseApplication;
import com.junruyi.dao.DaoSession;
import com.junruyi.dao.WifiDao;
import com.junruyi.dao.WifiDao.Properties;
import com.junruyi.entities.Wifi;

import android.content.Context;

public class WifiDbService {
	private static final String TAG = WifiDbService.class.getSimpleName();
	private static WifiDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public WifiDao wifiDao;

	public WifiDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static WifiDbService getInstance(Context context) {
		if (instance == null) {
			instance = new WifiDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.wifiDao = instance.mDaoSession.getWifiDao();
		}
		return instance;
	}

	private void addWifi(Wifi wifi) {
		wifiDao.insertOrReplace(wifi);
	}

	public void addWifi(String wifiName, String bssid) {
		Wifi wifi = wifiDao.queryBuilder().where(Properties.Bssid.eq(bssid)).unique();
		if (wifi == null) {
			addWifi(new Wifi(null, wifiName, bssid));
		} else {
			wifi.setWifiName(wifiName);
			wifiDao.update(wifi);
		}
	}

	public List<Wifi> getWifiList() {
		return wifiDao.loadAll();
	}

	public void deleteWifi(Wifi entity) {
		wifiDao.delete(entity);
	}

	public void updateWifiName(Wifi entity) {
		wifiDao.update(entity);
	}

}
