package com.junruyi.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.junruyi.base.BaseApplication;
import com.junruyi.base.BaseV4Fragment;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.entities.Location;
import com.junruyi.utils.DateTimeTools;
import com.smallrhino.junruyi.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @description:定位列表
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:36:14
 */
public class MainLocationFragment extends BaseV4Fragment {
	public final static String TAG = "MainLocationFragment";
	private View rootView;// 根View
	private ListView locationListView;
	private List<Location> dataList;
	private TextView cleanBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_location, container, false);
		initLocationList();
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		locationListView = (ListView) rootView.findViewById(R.id.location_list);
		cleanBtn = (TextView) rootView.findViewById(R.id.clean);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		locationListView.setAdapter(new LocationAdapter());
		cleanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clean();
			}
		});
	}

	/**
	 * 退出登录
	 */
	private void clean() {
		// 设置用户不曾登录

		final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否清空位置记录？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();

			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("清空", comfirm);
		myAlertDialog.setNegativeButton("取消", cancle);
		myAlertDialog.show();

	}

	/**
	 * 初始化数据
	 */
	private void initLocationList() {
		dataList = new ArrayList<>();

		Location l1 = new Location(Long.valueOf(1), "北纬34.45’33", "东经113.02'07", "厦门市厦门大学三家村广场", new Date(), 1);
		Location l2 = new Location(Long.valueOf(2), "北纬34.45’33", "东经113.02'07", "厦门市思明区思明南路442号", new Date(), 2);
		Location l3 = new Location(Long.valueOf(3), "北纬34.45’33", "东经113.02'07", "厦门市龙虎山路134号", new Date(), 3);
		Location l4 = new Location(Long.valueOf(4), "北纬34.45’33", "东经113.02'07", "厦门市吕岭路109号", new Date(), 4);
		dataList.add(l1);
		dataList.add(l2);
		dataList.add(l3);
		dataList.add(l4);
	}

	/**
	 * @description:
	 * @company: smallrhino
	 * @author：张帅
	 * @date 2015年11月12日 上午10:38:32
	 */
	private class LocationAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView image;
			public TextView nameTextView;
			public TextView datextView;
			public TextView longlatiTextView;
			public TextView addressTextView;

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
			final Location location = dataList.get(position);
			if (location == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.location_list, null);
				holder = new ViewHolder();
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.datextView = (TextView) view.findViewById(R.id.date);
				holder.longlatiTextView = (TextView) view.findViewById(R.id.longlati);
				holder.addressTextView = (TextView) view.findViewById(R.id.address);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			holder.image.setImageResource(R.drawable.bag);
			holder.nameTextView.setText("书包");
			holder.datextView.setText(DateTimeTools.getCurrentDateTimeForString());
			holder.longlatiTextView.setText("定位：  " + location.getLatitude() + location.getLongtitude());
			holder.addressTextView.setText("地点：  " + location.getAddress());
			return view;
		}
	}
}
