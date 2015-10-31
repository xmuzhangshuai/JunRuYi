package com.junruyi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 类名称：SharePreferenceUtil 
 * 类描述：SharedPreferences的一个工具类，调用setParam就能保存String,
 * Integer, Boolean, Float, Long类型的参�?同样调用getParam就能获取到保存在手机里面的数�?
 * 创建人： 张帅
 * 创建时间�?015�?�?3�?上午9:00:37
 * 
 */
public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static final String USE_COUNT = "count";// 记录软件使用次数

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// 记录软件使用次数
	public int getUseCount() {
		return sp.getInt("count", 0);
	}

	public void setUseCount(int count) {
		editor.putInt("count", count);
		editor.commit();
	}

}
