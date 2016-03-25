package com.junruyi.ui;

import java.util.ArrayList;
import java.util.List;

import com.junruyi.base.BaseV4Fragment;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.WifiDbService;
import com.junruyi.entities.Wifi;
import com.junruyi.utils.LogTool;
import com.smallrhino.junruyi.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
		wifiDbService = WifiDbService.getInstance(getActivity());

		initWifiList();
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		wifiListView = (ListView) rootView.findViewById(R.id.wifi_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		wifiAdapter = new WifiAdapter();
		wifiListView.setAdapter(wifiAdapter);
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
	}

	/**
	 * 初始化数据
	 */
	private void initWifiList() {
		dataList = new ArrayList<>();
		dataList = wifiDbService.getWifiList();
		for (Wifi wifi : dataList) {
			if (wifi != null) {
				LogTool.e("------" + wifi.getBssid() + wifi.getWifiName());
			}
		}
		//		Wifi w1 = new Wifi(Long.valueOf(1), "510", "1");
		//		Wifi w2 = new Wifi(Long.valueOf(1), "TP_LINK_242", "1");
		//		Wifi w3 = new Wifi(Long.valueOf(1), "办公室", "1");
		//		Wifi w4 = new Wifi(Long.valueOf(1), "家", "1");
		//		Wifi w5 = new Wifi(Long.valueOf(1), "509", "1");
		//		dataList.add(w1);
		//		dataList.add(w2);
		//		dataList.add(w3);
		//		dataList.add(w4);
		//		dataList.add(w5);
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

			if (position == 1) {
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
