package com.junruyi.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static final String USER_SHAREPREFERENCE = "userSharePreference";// SharePreference
	private Context context;

	public UserPreference(Context context) {
		this.context = context;
		sp = context.getSharedPreferences(USER_SHAREPREFERENCE, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	
	
	public boolean getInfoNotify() {
		return sp.getBoolean("infonotify", false);
	}
	public void setInfoNotify(boolean notify){
		editor.putBoolean("infonotify", notify);
		editor.commit();
	}
	
	public boolean getNoWarn() {
		return sp.getBoolean("nowarn", false);
	}
	public void setNoWarn(boolean nowarn){
		editor.putBoolean("nowarn", nowarn);
		editor.commit();
	}
	
//	/**
//	 * ������
//	 */
//	public void clear() {
//		String tel = getU_tel();
//		editor.clear();
//		setU_tel(tel);
//		editor.commit();
//	}
//
//	// ��¼�û��Ƿ��¼
//	public boolean getUserLogin() {
//		return sp.getBoolean("login", false);
//	}
//
//	public void setUserLogin(boolean login) {
//		editor.putBoolean("login", login);
//		editor.commit();
//	}

}
