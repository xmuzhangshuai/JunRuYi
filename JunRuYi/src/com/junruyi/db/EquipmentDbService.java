package com.junruyi.db;

import java.util.List;

import android.content.Context;

import com.junruyi.base.BaseApplication;
import com.junruyi.dao.DaoSession;
import com.junruyi.dao.EquipMentDao;
import com.junruyi.dao.EquipMentDao.Properties;
import com.junruyi.entities.EquipMent;

public class EquipmentDbService {
	private static final String TAG = EquipmentDbService.class.getSimpleName();
	private static EquipmentDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public EquipMentDao equipmentDao;

	public EquipmentDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static EquipmentDbService getInstance(Context context) {
		if (instance == null) {
			instance = new EquipmentDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.equipmentDao = instance.mDaoSession.getEquipMentDao();
		}
		return instance;
	}

	private void addEquipMent(EquipMent equipMent) {
		equipmentDao.insertOrReplace(equipMent);
	}

	public void addEquipMent(String equipMentAddress, String equipmentName) {
		EquipMent equipMent = equipmentDao.queryBuilder().
							where(Properties.EquipMentAddress.eq(equipMentAddress)).unique();
		if (equipMent == null) {
			addEquipMent(new EquipMent(null, equipMentAddress, equipmentName, null));
		} else {
			equipMent.setEquipMentName(equipmentName);
			equipmentDao.update(equipMent);
		}
	}

	public List<EquipMent> getEquipMentList() {
		return equipmentDao.loadAll();
	}
	public long countEquipMent(){
		return equipmentDao.count();
	}
	
	public void deleteEquipMent(EquipMent entity) {
		equipmentDao.delete(entity);
	}

	public void updateEquipMentName(EquipMent entity) {
		equipmentDao.update(entity);
	}
	public EquipMent getEquipMentByid(String id){
		return equipmentDao.queryBuilder().where(Properties.Id.eq(id)).unique();
	}
	public EquipMent getEquipMentByAddr(String addr){
		return equipmentDao.queryBuilder().where(Properties.EquipMentAddress.eq(addr)).unique();
	}

}
