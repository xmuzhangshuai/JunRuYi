package com.junruyi.db;

import java.util.List;

import android.content.Context;

import com.junruyi.base.BaseApplication;
import com.junruyi.dao.DaoSession;
import com.junruyi.dao.LocationDao;
import com.junruyi.dao.LocationDao.Properties;
import com.junruyi.entities.Location;

public class LocationDbService {
	private static final String TAG = LocationDbService.class.getSimpleName();
	private static LocationDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public LocationDao LocationDao;

	public LocationDbService() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 得到实例
	 * 
	 * @param context
	 * @return
	 */
	public static LocationDbService getInstance(Context context) {
		if (instance == null) {
			instance = new LocationDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.LocationDao = instance.mDaoSession.getLocationDao();
		}
		return instance;
	}

	public void addLocation(Location Location) {
		LocationDao.insertOrReplace(Location);
	}

	public List<Location> getLocationList() {
		return LocationDao.loadAll();
	}

	public long countLocation() {
		return LocationDao.count();
	}

	public void deleteLocation(Location entity) {
		LocationDao.delete(entity);
	}

	public void updateLocationName(Location entity) {
		LocationDao.update(entity);
	}

	public Location getLocationByid(String id) {
		return LocationDao.queryBuilder().where(Properties.Id.eq(id)).unique();
	}

	public Location getLocationByAddr(String addr) {
		return LocationDao.queryBuilder().where(Properties.Address.eq(addr))
				.unique();
	}

}
