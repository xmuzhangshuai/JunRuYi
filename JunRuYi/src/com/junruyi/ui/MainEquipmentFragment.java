package com.junruyi.ui;

import java.util.List;

import com.junruyi.base.BaseV4Fragment;
import com.junruyi.customewidget.MyAlertDialog;
import com.junruyi.db.EquipmentDbService;
import com.junruyi.entities.EquipMent;
import com.junruyi.service.BlueToothService;
import com.smallrhino.junruyi.R;

import android.R.string;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
 * @description:设备列表
 * @company: smallrhino
 * @author：张帅
 * @date 2015年10月31日 下午3:35:05
 */
public class MainEquipmentFragment extends BaseV4Fragment {
	private EquipmentDbService equipmentDbService;
	public final static String TAG = "MainEquipmentFragment";
	private View rootView;// 根View
	private ListView equipMentListView;
	private List<EquipMent> dataList;
	private EquipMentAdapter equipMentAdapter;
	private SoundPool soundPool;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_equipment, container, false);
		equipmentDbService = EquipmentDbService.getInstance(getActivity());
		

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		initEquipmentList();
		findViewById();// 初始化views
		initView();
	}
	@Override
	protected void findViewById() {
		equipMentListView = (ListView) rootView.findViewById(R.id.equipment_list);
	}

	@Override
	protected void initView() {
		equipMentAdapter = new EquipMentAdapter();
		equipMentListView.setAdapter(equipMentAdapter);
		
		equipMentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EquipMent equipMent = dataList.get(position);
				updateEquipMent(equipMent);
			}
		});
		equipMentListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				EquipMent equipMent = dataList.get(position);
				deleteEquipMent(equipMent);
				return true;
			}
		});
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(getActivity(), R.raw.air, 1);
	}

	/**
	 * 初始化数据
	 */
	private void initEquipmentList() {
		dataList = equipmentDbService.getEquipMentList();
		for(EquipMent e: dataList){
			if(BlueToothService.connect(e.getEquipMentAddress())){
				e.setEquipMentLogo(R.drawable.logo2);
			}
			else{
				e.setEquipMentLogo(R.drawable.logo3);
			}
		}
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
			public Button warnBtn;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
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
				holder.warnBtn = (Button) view.findViewById(R.id.warnBtn);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			holder.imageView.setImageResource(equipMent.getEquipMentLogo());
			holder.nameTextView.setText(equipMent.getEquipMentName());
			holder.warnBtn.setTag(position);
			holder.warnBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String text = holder.warnBtn.getText().toString();
					if(text.equals("报警"))
					{
						holder.warnBtn.setText("取消");
						baojing(position);
					}
					else{
						holder.warnBtn.setText("报警");
						cancelbaojing(position);
					}
				}
			});
			return view;
		}
		public void baojing(int position){
			Toast.makeText(getActivity(), "我要报警", Toast.LENGTH_LONG).show();
			soundPool.play(1,1,1,0,0,1);
		}
		public void cancelbaojing(int position){
			Toast.makeText(getActivity(), "取消报警", Toast.LENGTH_LONG).show();
			soundPool.stop(1);
		}
	}
	/**
	 * 更改蓝牙设备名字
	 */
	public void updateEquipMent(final EquipMent updateEquipMent) {
//		final Wifi wifi = dataList.get(position);
		final EditText inputServer = new EditText(getActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("输入新蓝牙设备名称").setIcon(android.R.drawable.ic_dialog_info)
										 .setView(inputServer)
										 .setNegativeButton("取消", null);
		builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				String newEquipMentName = inputServer.getText().toString();
				if (!newEquipMentName.isEmpty()) {
					//修改蓝牙设备名称
					EquipMent e = new EquipMent(updateEquipMent.getId(), updateEquipMent.getEquipMentAddress(),
							newEquipMentName, updateEquipMent.getEquipMentLogo());
					equipmentDbService.updateEquipMentName(e);
					//通知数据源有更新
					initEquipmentList();
					equipMentAdapter.notifyDataSetChanged();
				}
			}
		});
		builder.show();
	}
	
	/**
	 * 移除蓝牙设备的方法
	 */
	public void deleteEquipMent(final EquipMent delEquipMent) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
		myAlertDialog.setTitle("提示");
		myAlertDialog.setMessage("是否将该蓝牙设备移除？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				//点击确认则做删除安全wifi操作
				equipmentDbService.deleteEquipMent(delEquipMent);
				myAlertDialog.dismiss();
				//通知数据源有更新
				initEquipmentList();
				equipMentAdapter.notifyDataSetChanged();
			}
		};
		View.OnClickListener cancel = new OnClickListener() {
			@Override
			public void onClick(View v) {
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("移除", comfirm);
		myAlertDialog.setNegativeButton("取消", cancel);
		myAlertDialog.show();
	}
}
