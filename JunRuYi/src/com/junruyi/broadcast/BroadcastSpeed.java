package com.junruyi.broadcast;

import com.junruyi.ui.AlarmActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastSpeed extends BroadcastReceiver {

	private Intent alarm = new Intent();
	public static boolean FLAG = true;
	String addr = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		double danger = intent.getDoubleExtra("danger", -1.0d);
		addr = intent.getStringExtra("addr");
		if (null == addr)
			addr = "";
		System.out.println("danger" + danger);
		if (danger >= 1.0d && FLAG) {
			FLAG = false;
			alarm.setClass(context, AlarmActivity.class);
			alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			alarm.putExtra("addr", addr);
			context.startActivity(alarm);
		}
	}
}
