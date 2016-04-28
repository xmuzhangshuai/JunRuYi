//package com.junruyi.service;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//
//public class SpeedService extends Service {
//
//	public static final String BROADCASTACTION = "com.xxn.speed";
//	public static int danger = 0;
//	public static boolean FLAG = false;
//	Timer timer;
//	int i = 0;
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//
//		timer = new Timer();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				// 定时更新速度
//				i++;
//				int speed = getSpeed(i);
//				System.out.println("speed:"+speed);
//				if (speed > 5) {
//					danger = 3;
//					Intent intent = new Intent();
//					intent.setAction(BROADCASTACTION);
//					intent.putExtra("danger", danger);
//					sendBroadcast(intent);
//				}
//			}
//		}, 2000, 1000);
//		
//		return super.onStartCommand(intent, flags, startId);
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		if (timer != null) {
//			timer.cancel();
//		}
//	}
//
//	public int getSpeed(int i) {
//		return i;
//	}
//
//}
