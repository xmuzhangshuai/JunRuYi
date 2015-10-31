package com.junruyi.utils;

import java.util.Date;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static final String USER_SHAREPREFERENCE = "userSharePreference";// �û�SharePreference
	private Context context;

	public UserPreference(Context context) {
		this.context = context;
		sp = context.getSharedPreferences(USER_SHAREPREFERENCE, Context.MODE_PRIVATE);
		editor = sp.edit();
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
