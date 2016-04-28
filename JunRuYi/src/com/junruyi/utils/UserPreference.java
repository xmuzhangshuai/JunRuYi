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
		sp = context.getSharedPreferences(USER_SHAREPREFERENCE,
				Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public boolean getInfoNotify() {
		return sp.getBoolean("infonotify", false);
	}

	public void setInfoNotify(boolean notify) {
		editor.putBoolean("infonotify", notify);
		editor.commit();
	}

	public boolean getNoWarn() {
		return sp.getBoolean("nowarn", false);
	}

	public void setNoWarn(boolean nowarn) {
		editor.putBoolean("nowarn", nowarn);
		editor.commit();
	}

	// 手机号
	public String getU_tel() {
		return sp.getString("tel", "");
	}

	public void setU_tel(String u_tel) {
		editor.putString("tel", u_tel);
		editor.commit();
	}

}
