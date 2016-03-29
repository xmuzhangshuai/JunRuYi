package com.junruyi.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.junruyi.dao.DaoMaster;
import com.junruyi.dao.DaoMaster.OpenHelper;
import com.junruyi.dao.DaoSession;
import com.junruyi.utils.UserPreference;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.R.bool;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.media.MediaPlayer;

/**   
 *    
 * 项目名称：lanquan   
 * 类名称：BaseApplication   
 * 类描述：   将取得DaoMaster对象的方法放到Application层这样避免多次创建生成Session对象。
 * 创建人：张帅     
 * 创建时间：2013-12-20 下午9:10:55   
 * 修改人：张帅     
 * 修改时间：2013-12-20 下午9:10:55   
 * 修改备注：   
 * @version    
 *    
 */
public class BaseApplication extends Application {
	private static BaseApplication myApplication;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	private UserPreference userPreference;
	private MediaPlayer messagePlayer;

	
	public static boolean FLAG = true;//是否警报状态
	public static int DANGER = 0;
	public static int WIFI = 0;
	public static int SPEED = 0;
	
	
	public static List<Map<String, Object>> bluetoothGattList = null;	
	public synchronized static BaseApplication getInstance() {
		return myApplication;
	}

	// @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		if (myApplication == null)
			myApplication = this;

		initImageLoader(getApplicationContext());

		//		initFaceMap();
		initData();

	}

	private void initData() {
		userPreference = new UserPreference(this);
		//		messagePlayer = MediaPlayer.create(this, R.raw.office);
		
	}

	/**
	 * 返回消息提示声音
	 * @return
	 */
	//	public synchronized MediaPlayer getMessagePlayer() {
	//		if (messagePlayer == null)
	//			messagePlayer = MediaPlayer.create(this, R.raw.office);
	//		return messagePlayer;
	//	}

	public synchronized UserPreference getUserPreference() {
		if (userPreference == null)
			userPreference = new UserPreference(this);
		return userPreference;
	}
	public synchronized List<Map<String, Object>> getbluetoothGatt() {
		if (bluetoothGattList == null)
			bluetoothGattList = new ArrayList<>();
		return bluetoothGattList;
	}
	
	/** 
	 * 取得DaoMaster 
	 *  
	 * @param context 
	 * @return 
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, "quanzi.db", null);
			daoMaster = new DaoMaster(openHelper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * 取得DaoSession 
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

}
