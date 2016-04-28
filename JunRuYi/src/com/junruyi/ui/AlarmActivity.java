package com.junruyi.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.junruyi.broadcast.BroadcastSpeed;
import com.junruyi.customewidget.MyAlertDialog;
import com.smallrhino.junruyi.R;

public class AlarmActivity extends Activity{
	
	private WakeLock mWakelock;
	private MediaPlayer mediaPlayer;
	String addr ;
	Intent intent = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mediaPlayer = MediaPlayer.create(this, R.raw.air);
		final Window win = getWindow();
		 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		 | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		 win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		//电源管理器
		PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				|PowerManager.SCREEN_DIM_WAKE_LOCK,"SimpleTimer");
	}
	@Override
	protected void onResume() {
		super.onResume();
		final MyAlertDialog myAlertDialog = new MyAlertDialog(AlarmActivity.this);
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否关闭警报？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
//				SpeedService.danger = 0;
				mediaPlayer.stop();
				myAlertDialog.dismiss();
				finish();
			}
		};
		View.OnClickListener cancel = new OnClickListener() {

			@Override
			public void onClick(View v) {
				myAlertDialog.dismiss();
				BroadcastSpeed.FLAG = true;
			}
		};
		myAlertDialog.setPositiveButton("关闭", comfirm);
		myAlertDialog.setNegativeButton("取消", cancel);
		myAlertDialog.show();
		
		mediaPlayer.start();
		mWakelock.acquire();//点亮
	}
	
	@Override
	protected void onDestroy() {
		mWakelock.release();//关闭
		mediaPlayer.stop();
		super.onDestroy();
	}
	
	
	
}
