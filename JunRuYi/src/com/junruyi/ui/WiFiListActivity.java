package com.junruyi.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.WifiDbService;
import com.junruyi.entities.Wifi;
import com.junruyi.utils.FastJsonTool;
import com.junruyi.utils.LogTool;
import com.smallrhino.junruyi.R;

public class WiFiListActivity extends Activity {

	private WifiManager wifiManager;
	private List<ScanResult> list;
	private PullToRefreshListView postListView;
	private MyAdapter myAdapter;
	private WifiDbService wifiDbService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_list);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiDbService = WifiDbService.getInstance(this);
		findViewById();
		initData();
		initView();
	}

	private void findViewById() {
		openWifi();
		postListView = (PullToRefreshListView) findViewById(R.id.newwifilist);
	}

	private void initData() {
		list = wifiManager.getScanResults();
		myAdapter = new MyAdapter();
		postListView.setAdapter(myAdapter);
		postListView.setOnItemClickListener(new WiFiItemListerner());
	}

	// 初始化wifi列表页面
	private void initView() {
		// 设置上拉下拉刷新事件
		postListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				postListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						list = wifiManager.getScanResults();
						LogTool.i(FastJsonTool.createJsonString(list));
						myAdapter.notifyDataSetChanged();
						postListView.onRefreshComplete();
					}
				}, 100);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
			}
		});

		if (list == null) {
			Toast.makeText(this, "wifi未打开", Toast.LENGTH_LONG).show();
		} else {
			postListView.setAdapter(myAdapter);
		}
	}

	private void openWifi() {
		if (!wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(true);
	}

	public class WiFiItemListerner implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			position = position - 1;
			String name = list.get(position).SSID;
			String bssid = list.get(position).BSSID;
			LogTool.i("position:"+position+"name:"+name+"--bssid:"+bssid);
			showAddWifi(name,bssid);
		}
	}
	/**
	 * 判断当前wifi是否需要添加到数据库
	 */
	private void showAddWifi(String name,String bssid) {
		if (null!=bssid && !bssid.isEmpty()) {
			long count = wifiDbService.countWifi();
			Wifi temp = wifiDbService.getWifiByBssid(bssid);
			if (count <= 6 && temp == null) {
				// 添加当前无线到安全wifi
				addWifi(name, bssid);
			}
			else{
				if(count > 6){
					Toast.makeText(this, "安全WiFi已经超过6个", Toast.LENGTH_LONG).show();
				}
				if(temp != null){
					Toast.makeText(this, "已经在安全WiFi列表", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	/**
	 * 添加安全wifi的方法
	 */
	public void addWifi(final String wifiName, final String bssid) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否设置当前连接的wifi为安全？");
		View.OnClickListener comfirm = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 点击确认则做添加安全wifi操作
				wifiDbService.addWifi(wifiName, bssid);
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
	
	
	/*
	 * wifi适配器
	 */
	public class MyAdapter extends BaseAdapter {

		LayoutInflater inflater;

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			inflater = LayoutInflater.from(WiFiListActivity.this);
			View view = null;
			view = inflater.inflate(R.layout.item_wifi_list, null);
			ScanResult scanResult = list.get(position);
			TextView textView = (TextView) view.findViewById(R.id.textView);
			textView.setText(scanResult.SSID);
			TextView signalStrenth = (TextView) view
					.findViewById(R.id.signal_strenth);
			signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
			// 判断信号强度，显示对应的指示图标
			if (Math.abs(scanResult.level) > 100) {
				// imageView.setImageDrawable(getResources().getDrawable(R.drawable.s));
			} else if (Math.abs(scanResult.level) > 80) {

			} else if (Math.abs(scanResult.level) > 70) {

			} else if (Math.abs(scanResult.level) > 60) {

			} else if (Math.abs(scanResult.level) > 50) {

			} else {

			}
			imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.wifi));
			return view;
		}

	}

}
