package com.junruyi.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.EquipmentDbService;
import com.junruyi.service.BlueToothService;
import com.junruyi.utils.LogTool;
import com.smallrhino.junruyi.R;

public class AddEquipmentActivity extends Activity {

	private EquipmentDbService equipmentDbService;

	private ListView lv;
	SimpleAdapter mAdapter = null;
	ArrayList<HashMap<String, Object>> listItem = null;

	private MsgReceiver msgReceiver;
	private String devices;
	Intent service = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_equipment);
		equipmentDbService = EquipmentDbService
				.getInstance(AddEquipmentActivity.this);

		lv = (ListView) findViewById(R.id.add_equipment_list);
		listItem = new ArrayList<>();
//		for (int i = 0; i < 10; i++) {
//			HashMap<String, Object> map = new HashMap<>();
//			map.put("ItemImage", R.drawable.ic_launcher);
//			map.put("ItemTitle", "第" + i + "行");
//			map.put("ItemText", "这是第" + i + "行");
//			listItem.add(map);
//		}

		String from[] = new String[] { "ItemImage", "ItemTitle", "ItemText" };
		int to[] = new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText };
		mAdapter = new SimpleAdapter(this, listItem, R.layout.bluetoothitem,
				from, to);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new ListViewListener());

		service = new Intent(this, BlueToothService.class);
		startService(service);
		LogTool.e("执行service");
		// 动态注册广播接收器
		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.communication.RECEIVER");
		registerReceiver(msgReceiver, intentFilter);
	}

	public class ListViewListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String addr = listItem.get(position).get("ItemText").toString();
			String name = listItem.get(position).get("ItemTitle").toString();
			addEquipMent(addr, name);
		}
	}

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
			if (!addr.isEmpty()) {
				System.out.println("addr:" + addr);
				HashMap<String, Object> map = new HashMap<>();
				map.put("ItemImage", R.drawable.ic_launcher);
				map.put("ItemTitle", name);
				map.put("ItemText", addr);
				listItem.add(map);
				mAdapter.notifyDataSetChanged();
				Toast.makeText(AddEquipmentActivity.this, "正在刷新",
						Toast.LENGTH_SHORT).show();
			}
			context.unregisterReceiver(this);
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
				// TODO Auto-generated method stub
				if (BlueToothService.connect(equipMentAddress)) {
					Toast.makeText(AddEquipmentActivity.this, "连接成功....",
							Toast.LENGTH_LONG).show();
					equipmentDbService
					.addEquipMent(equipMentAddress, equipmentName);
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
