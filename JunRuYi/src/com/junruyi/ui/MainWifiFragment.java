package com.junruyi.ui;

import java.util.ArrayList;
import java.util.List;

import com.junruyi.base.BaseV4Fragment;
import com.junruyi.entities.EquipMent;
import com.junruyi.entities.Wifi;
import com.smallrhino.junruyi.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
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
		wifiListView.setAdapter(new WifiAdapter());
	}

	/**
	 * 初始化数据
	 */
	private void initWifiList() {
		dataList = new ArrayList<>();
		Wifi w1 = new Wifi(Long.valueOf(1), "510");
		Wifi w2 = new Wifi(Long.valueOf(1), "TP_LINK_242");
		Wifi w3 = new Wifi(Long.valueOf(1), "办公室");
		Wifi w4 = new Wifi(Long.valueOf(1), "家");
		Wifi w5 = new Wifi(Long.valueOf(1), "509");
		dataList.add(w1);
		dataList.add(w2);
		dataList.add(w3);
		dataList.add(w4);
		dataList.add(w5);
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
}
