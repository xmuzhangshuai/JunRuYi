package com.junruyi.ui;

import java.util.ArrayList;
import java.util.List;

import com.junruyi.base.BaseV4Fragment;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.WifiDbService;
import com.junruyi.entities.Wifi;
import com.junruyi.utils.LogTool;
import com.junruyi.utils.WifiUtil;
import com.smallrhino.junruyi.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @description:Wifi列表
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:35:40
 */
public class MainWifiFragment extends BaseV4Fragment {
	public final static String TAG = "MainWifiFragment";
	private View rootView;// 根View
	private ListView wifiListView;
	private List<Wifi> dataList;
	private WifiDbService wifiDbService;
	private WifiAdapter wifiAdapter;
	private WifiUtil wifiUtil;
	private View addwifiView;
	int nowWiFiLocation = -1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
		wifiDbService = WifiDbService.getInstance(getActivity());

		return rootView;
	}
	@Override
	public void onResume() {
		super.onResume();
		initWifiList();
		findViewById();// 初始化views
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		wifiListView = (ListView) rootView.findViewById(R.id.wifi_list);
		addwifiView = (View) rootView.findViewById(R.id.addwifi);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		wifiAdapter = new WifiAdapter();
		wifiListView.setAdapter(wifiAdapter);
//		if(nowWiFiLocation != -1){
//			wifiListView.getChildAt(nowWiFiLocation).setBackgroundColor(Color.GRAY);
////			View view = wifiListView.getChildAt(nowWiFiLocation);
////			view.setBackgroundResource(R.color.dark_gray);	
//		}
		
		//wifilist单击事件
		wifiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Wifi wifi = dataList.get(position);
				final EditText inputServer = new EditText(getActivity());
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("请输入新Wifi名称").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消", null);
				builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String newWifiName = inputServer.getText().toString();
						if (!newWifiName.isEmpty()) {
							//修改wifi名称
							Wifi temp = new Wifi(wifi.getId(), newWifiName, wifi.getBssid());
							wifiDbService.updateWifiName(temp);
							//通知数据源有更新
							initWifiList();
							wifiAdapter.notifyDataSetChanged();
						}
					}
				});
				builder.show();
			}
		});
		/**
		 * 长按删除
		 */
		wifiListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Wifi wifi = dataList.get(position);
				deleteWifi(wifi);
				return true;
			}
		});
		
		addwifiView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), WiFiListActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initWifiList() {
		wifiUtil = new WifiUtil(getActivity());
		dataList = new ArrayList<>();
		dataList = wifiDbService.getWifiList();
		for (int i =0;i < dataList.size();i++) {
			if (dataList.get(i) != null) {
				if(wifiUtil.getBSSID().equals(dataList.get(i).getBssid()))
				{
					nowWiFiLocation = i;
					System.out.println("nowWiFiLocation"+nowWiFiLocation);
					break;
				}
			}
		}
	}

	/**
	 * @description:
	 * @company: smallrhino
	 * @author：张帅
	 * @date 2015年11月12日 上午10:38:32
	 */
	private class WifiAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView checkImage;
			public TextView nameTextView;
			public ImageView wifiImage;
			public ImageView infoBtn;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final Wifi wifi = dataList.get(position);
			if (wifi == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.wifi_list, null);
				holder = new ViewHolder();
				holder.checkImage = (ImageView) view.findViewById(R.id.image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.wifiImage = (ImageView) view.findViewById(R.id.wifi);
				holder.infoBtn = (ImageView) view.findViewById(R.id.infoBtn);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			if (position == nowWiFiLocation) {
				holder.checkImage.setImageResource(R.drawable.logo1);
				holder.checkImage.setVisibility(View.VISIBLE);
			} else {
				holder.checkImage.setVisibility(View.INVISIBLE);
			}

			holder.nameTextView.setText(wifi.getWifiName());

			return view;
		}
	}

	/**
	 * 删除wifi的方法
	 */
	public void deleteWifi(final Wifi delWifi) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否将该安全wifi移除？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击确认则做删除安全wifi操作
				wifiDbService.deleteWifi(delWifi);
				myAlertDialog.dismiss();
				//通知数据源有更新
				initWifiList();
				wifiAdapter.notifyDataSetChanged();
			}
		};
		View.OnClickListener cancel = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("移除", comfirm);
		myAlertDialog.setNegativeButton("取消", cancel);
		myAlertDialog.show();
	}
}
