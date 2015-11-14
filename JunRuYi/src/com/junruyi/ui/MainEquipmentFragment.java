package com.junruyi.ui;

import java.util.ArrayList;
import java.util.List;

import com.junruyi.base.BaseV4Fragment;
import com.junruyi.entities.EquipMent;
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
 * @description:设备列表
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:35:05
 */
public class MainEquipmentFragment extends BaseV4Fragment {
	public final static String TAG = "MainEquipmentFragment";
	private View rootView;// 根View
	private ListView equipMentListView;
	private List<EquipMent> dataList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_equipment, container, false);
		initEquipmentList();
		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		equipMentListView = (ListView) rootView.findViewById(R.id.equipment_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		equipMentListView.setAdapter(new EquipMentAdapter());
	}

	/**
	 * 初始化数据
	 */
	private void initEquipmentList() {
		dataList = new ArrayList<>();
		EquipMent e1 = new EquipMent(Long.valueOf(1), "皮包1", R.drawable.logo1);
		EquipMent e2 = new EquipMent(Long.valueOf(2), "手镯", R.drawable.logo2);
		EquipMent e3 = new EquipMent(Long.valueOf(3), "书包1", R.drawable.logo3);
		EquipMent e4 = new EquipMent(Long.valueOf(4), "皮包2", R.drawable.logo4);
		EquipMent e5 = new EquipMent(Long.valueOf(5), "腰带", R.drawable.logo5);
		EquipMent e6 = new EquipMent(Long.valueOf(6), "书包2", R.drawable.logo6);
		EquipMent e7 = new EquipMent(Long.valueOf(7), "皮包3", R.drawable.logo7);
		dataList.add(e1);
		dataList.add(e2);
		dataList.add(e3);
		dataList.add(e4);
		dataList.add(e5);
		dataList.add(e6);
		dataList.add(e7);
	}

	/**
	 * @description:
	 * @company: smallrhino
	 * @author：张帅
	 * @date 2015年11月12日 上午10:38:32
	 */
	private class EquipMentAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView imageView;
			public TextView nameTextView;
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
			final EquipMent equipMent = dataList.get(position);
			if (equipMent == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.equipment_list, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			holder.imageView.setImageResource(equipMent.getEquipMentLogo());
			holder.nameTextView.setText(equipMent.getEquipMentName());

			return view;
		}
	}
}
