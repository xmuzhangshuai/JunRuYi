package com.junruyi.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.EquipmentDbService;
import com.junruyi.service.BlueToothService;
import com.smallrhino.junruyi.R;

public class EquipmentListActivity extends Activity {

	private EquipmentDbService equipmentDbService;

	private PullToRefreshListView postListView;
	SimpleAdapter mAdapter = null;
	ArrayList<HashMap<String, Object>> listItem = null;

	private MsgReceiver msgReceiver;
	private BlueToothService bleservice = null;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment_list);
		equipmentDbService = EquipmentDbService
				.getInstance(EquipmentListActivity.this);

		postListView = (PullToRefreshListView) findViewById(R.id.add_equipment_list);
		
		listItem = new ArrayList<>();
		String from[] = new String[] { "ItemImage", "ItemTitle", "ItemText" };
		int to[] = new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText };
		mAdapter = new SimpleAdapter(this, listItem, R.layout.bluetoothitem,
				from, to);
		postListView.setAdapter(mAdapter);
		postListView.setOnItemClickListener(new ListViewListener());
		
		initView();
		// 动态注册广播接收器
		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.communication.RECEIVER");
		registerReceiver(msgReceiver, intentFilter);
	}
	@Override
	protected void onResume() {
		super.onResume();
		startService(intent);
	}
	
	public void initView(){
		intent = new Intent();  
		intent.setClass(this,BlueToothService.class); 
		//bind服务
		postListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				postListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						startService(intent);
						postListView.onRefreshComplete();
					}
				}, 100);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				
			}

		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(msgReceiver);
	}

	public class ListViewListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			position = position - 1;
			String addr = listItem.get(position).get("ItemText").toString();
			String name = listItem.get(position).get("ItemTitle").toString();
			addEquipMent(addr, name);
		}
	}
	
	ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bleservice = ((BlueToothService.MsgBinder)service).getService();
			System.out.println("bleservice"+bleservice);
		}
	};
	

	/**
	 * 广播接收器
	 * 
	 * @author len
	 * 
	 */
	public class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拿到数据
			String addr = intent.getStringExtra("addr");
			String name = intent.getStringExtra("name");
			System.out.println("addr:" + addr);
			if (!addr.isEmpty()) {
				System.out.println("addr:" + addr);
				HashMap<String, Object> map = new HashMap<>();
				map.put("ItemImage", R.drawable.ic_launcher);
				map.put("ItemTitle", name);
				map.put("ItemText", addr);
				if(!listItem.contains(map)){
					listItem.add(map);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * 添加蓝牙设备的方法
	 */
	public void addEquipMent(final String equipMentAddress,
			final String equipmentName) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否添加当前蓝牙设备");
		View.OnClickListener comfirm = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (BlueToothService.connect(equipMentAddress)) {
					Toast.makeText(EquipmentListActivity.this, "连接成功....",
							Toast.LENGTH_LONG).show();
					equipmentDbService
					.addEquipMent(equipMentAddress, "Ruy'smart");
				}
				myAlertDialog.dismiss();
			}
		};
		View.OnClickListener cancel = new OnClickListener() {
			@Override
			public void onClick(View v) {
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("确定", comfirm);
		myAlertDialog.setNegativeButton("取消", cancel);
		myAlertDialog.show();
	}

}
