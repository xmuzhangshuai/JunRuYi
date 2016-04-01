package com.junruyi.service;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;


 //service负责后台的需要长期运行的任务
 // 计步器服务
 // 运行在后台的服务程序，完成了界面部分的开发后
 // 就可以开发后台的服务类StepService
 // 注册或注销传感器监听器，在手机屏幕状态栏显示通知，与StepActivity进行通信，走过的步数记到哪里了？？？
public class StepCounterService extends Service {
	public static final String BROADCASTACTION = "com.xxn.speed";
	public static Boolean FLAG = false;// 服务运行标志

	private SensorManager mSensorManager;// 传感器服务
	private StepDetector detector;// 传感器监听对象

	private PowerManager mPowerManager;// 电源管理服务
	private WakeLock mWakeLock;// 屏幕灯
	
	private long timer = 0;// 运动时间
	private  long startTimer = 0;// 开始时间
	private  long tempTime = 0;
	private int step_length = 70;
	private int total_step = 0;   //走的总步数
	private Double distance = 0.0;// 路程：米
	private Double calories = 0.0;// 热量：卡路里
	private Double velocity = 0.0;// 速度：米每秒
	private Thread thread;  //定义线程对象
	Handler handler = new Handler() {// Handler对象用于更新当前步数,定时发送消息，调用方法查询数据用于显示？？？？？？？？？？
		//主要接受子线程发送的数据, 并用此数据配合主线程更新UI
		//Handler运行在主线程中(UI线程中), 它与子线程可以通过Message对象来传递数据, 
		//Handler就承担着接受子线程传过来的(子线程用sendMessage()方法传递Message对象，(里面包含数据)
		//把这些消息放入主线程队列中，配合主线程进行更新UI。

		@Override                  //这个方法是从父类/接口 继承过来的，需要重写一次
		public void handleMessage(Message msg) {
			super.handleMessage(msg);        // 此处可以更新UI
			countDistance();     //调用距离方法，看一下走了多远
			if (timer != 0 && distance != 0.0) {
				//速度velocity
				velocity = distance * 1000 / timer;
			} else {
				calories = 0.0;
				velocity = 0.0;
			}
			countStep();          //调用步数方法
			if(velocity > 1.0){
				Intent intent = new Intent();
				intent.setAction(BROADCASTACTION);
				intent.putExtra("danger", velocity);
				sendBroadcast(intent);
			}
			//System.out.println("velocity:"+velocity+"--total_step:"+total_step);
		}
	};
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("stepservice");
		FLAG = true;// 标记为服务正在运行
		// 创建监听器类，实例化监听对象
		detector = new StepDetector(this);

		// 获取传感器的服务，初始化传感器
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// 注册传感器，注册监听器
		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
		// 电源管理服务
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
		mWakeLock.acquire();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("velocity:"+velocity+"--total_step:"+total_step);
		startTimer = System.currentTimeMillis();
		tempTime = timer;
		if (thread == null) {

			thread = new Thread() {// 子线程用于监听当前步数的变化

				@Override
				public void run() {
					super.run();
					int temp = 0;
					while (true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (StepCounterService.FLAG) {
							Message msg = new Message();
							if (temp != StepDetector.CURRENT_SETP) {
								temp = StepDetector.CURRENT_SETP;
							}
							if (startTimer != System.currentTimeMillis()) {
								timer = tempTime + System.currentTimeMillis()
										- startTimer;
							}
							handler.sendMessage(msg);// 通知主线程
						}
					}
				}
			};
			thread.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		FLAG = false;// 服务停止
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}

		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}
	/**
	 * 计算行走的距离                                                                        
	 */
	private void countDistance() {
		if (StepDetector.CURRENT_SETP % 2 == 0) {
			distance = (StepDetector.CURRENT_SETP / 2) * 3 * step_length * 0.01;
		} else {
			distance = ((StepDetector.CURRENT_SETP / 2) * 3 + 1) * step_length * 0.01;
		}
	}
	/**
	 * 实际的步数
	 */
	private void countStep() {
		if (StepDetector.CURRENT_SETP % 2 == 0) {
			total_step = StepDetector.CURRENT_SETP;
		} else {
			total_step = StepDetector.CURRENT_SETP +1;
		}
		total_step = StepDetector.CURRENT_SETP;
	}
}
