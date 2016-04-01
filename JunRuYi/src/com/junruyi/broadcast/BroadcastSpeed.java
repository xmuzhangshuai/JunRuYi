package com.junruyi.broadcast;

import com.junruyi.ui.AlarmActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastSpeed extends BroadcastReceiver{

	private Intent i = new Intent();
	public static boolean FLAG = true;
	@Override
	public void onReceive(Context context, Intent intent) {
		double danger = intent.getDoubleExtra("danger", -1.0d);
		System.out.println("danger"+danger);
		if(danger >= 1 && FLAG)
		{
			FLAG = false;
			i.setClass(context, AlarmActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			context.startActivity(i);
		}
	}
}
