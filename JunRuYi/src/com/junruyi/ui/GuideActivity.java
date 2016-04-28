package com.junruyi.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.junruyi.base.BaseActivity;
import com.junruyi.db.CopyDataBase;
import com.junruyi.service.BlueToothService;
import com.junruyi.service.StepCounterService;
import com.junruyi.utils.SharePreferenceUtil;
import com.smallrhino.junruyi.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 类名称：GuideActivity
 * 类描述：引导页面，首次运行进入首次引导页面，GuidePagerActivity;
 * 用户没有登录则进入登录/注册页面；
 * 用户已经登录过则进入主页面加载页，
 * 期间完成程序的初始化工作。 
 * 创建人： 张帅
 * 创建时间：2015-4-4 上午8:32:52
 * 
 */

public class GuideActivity extends BaseActivity {
	private SharePreferenceUtil sharePreferenceUtil;
	private TextView versionInfotTextView;
	Intent bluetooth = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		// 获取启动的次数
		sharePreferenceUtil = new SharePreferenceUtil(this, SharePreferenceUtil.USE_COUNT);
		int count = sharePreferenceUtil.getUseCount();

		Intent step = new Intent(this, StepCounterService.class);
		startService(step);
		
		bluetooth = new Intent(this, BlueToothService.class);
		startService(bluetooth);
		
//		Intent speed = new Intent(this, SpeedService.class);
//		startService(speed);
		// 动态注册广播接收器
//		BroadcastSpeed broadcastSpeed = new BroadcastSpeed();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("com.xxn.speed");
//		registerReceiver(broadcastSpeed, intentFilter);
		
		// 开启百度推送服务
		// PushManager.startWork(getApplicationContext(),
		// PushConstants.LOGIN_TYPE_API_KEY,
		// Constants.BaiduPushConfig.API_KEY);
		// 基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());
		// 设置标签
		// List<String> tags = new ArrayList<String>();
		// String gender = userPreference.getU_gender();

		// if (!TextUtils.isEmpty(gender)) {
		// tags.add(gender);
		// PushManager.setTags(this, tags);
		// }

		/************ 初始化友盟服务 **************/
		// 禁止友盟禁止默认的页面统计方式
		MobclickAgent.openActivityDurationTrack(false);

		// MobclickAgent.updateOnlineConfig(this);
		//		new FeedbackAgent(getApplicationContext()).sync();

		if (count == 0) {// 如果是第一次登陆，则启动向导页面
			// 第一次运行拷贝数据库文件
			new initDataBase().execute();
			sharePreferenceUtil.setUseCount(++count);// 次数加1
			startActivity(new Intent(GuideActivity.this, GuidePagerActivity.class));
		} else {// 如果不是第一次使用,则不启动向导页面，显示欢迎页面。
			//			if (userPreference.getUserLogin()) {// 如果是已经登陆过
			//				setContentView(R.layout.activity_guide);
			//				findViewById();
			//				initView();
			//				ServerUtil.getInstance().login(GuideActivity.this, MainActivity.class);
			//			} else {// 如果用户没有登录过或者已经注销
			//				startActivity(new Intent(GuideActivity.this, LoginOrRegisterActivity.class));
			//				// SchoolDbService.getInstance(getApplication()).getSchoolNameById(831);
			//			}
			startActivity(new Intent(GuideActivity.this, MainActivity.class));
			sharePreferenceUtil.setUseCount(++count);// 次数加1
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		// versionInfotTextView = (TextView) findViewById(R.id.version_info);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// versionInfotTextView.setText("一线牵\n\n"+getVersion());
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.app_name) + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}

	/**
	 * 类名称：initDataBase 类描述：拷贝数据库 创建人： 张帅 创建时间：2014年7月8日 下午4:51:58
	 * 
	 */
	public class initDataBase extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			new CopyDataBase(GuideActivity.this).copyDataBase();// 拷贝数据库
			return null;
		}
	}
}
