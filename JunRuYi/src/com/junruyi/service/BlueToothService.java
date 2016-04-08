package com.junruyi.service;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.junruyi.db.EquipmentDbService;
import com.junruyi.entities.EquipMent;
import com.junruyi.utils.BluetoothLeClass;
import com.junruyi.utils.BluetoothLeClass.OnDataAvailableListener;
import com.junruyi.utils.BluetoothLeClass.OnServiceDiscoverListener;
import com.junruyi.utils.LogTool;
import com.junruyi.utils.Utils;

public class BlueToothService extends Service {

	private EquipmentDbService equipmentDbService;
	private List<EquipMent> list = null;
	private final static String TAG = BlueToothService.class.getSimpleName();
	private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
	private final static String PASS_KEY_DATA = "0000ffe2-0000-1000-8000-00805f9b34fb";
	private final static String BATTERY_KEY_DATA = "00002a19-0000-1000-8000-00805f9b34fb";
	private boolean mScanning;
	private Handler mHandler;
	private BluetoothAdapter mBluetoothAdapter;
	private static BluetoothLeClass mBLE;

	private static final long SCAN_PERIOD = 10000;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		equipmentDbService = EquipmentDbService
				.getInstance(BlueToothService.this);
		list = equipmentDbService.getEquipMentList();

		mHandler = new Handler();
		// 初始化蓝牙适配器
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG)
					.show();
			onDestroy();
		}
		// 该设备支持蓝牙，开启蓝牙
		mBluetoothAdapter.enable();
		mBLE = new BluetoothLeClass(getApplicationContext());
		if (!mBLE.initialize()) {
			LogTool.e(TAG, "Unable to initialize Bluetooth");
			// Log.e(TAG, "Unable to initialize Bluetooth");
			onDestroy();
		}

		// 发现BLE终端的Service时回调
		mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
		// 收到BLE终端数据交互的事件
		mBLE.setOnDataAvailableListener(mOnDataAvailable);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onStart");

		// 开始搜索蓝牙设备
		scanLeDevice(true);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroy");
		super.onDestroy();
	}

	public void scanLeDevice(final boolean enable) {
		if (enable) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);

		}
	}

	/**
	 * 搜索到BLE终端服务的事件
	 */
	private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener() {
		
		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			
			displayGattServices(mBLE.getSupportedGattServices());
			mBLE.readBattery();
			// int ss = mBLE.getRssi();
			mBLE.getRssi();
		}
	};

	/**
	 * 收到BLE终端数据交互的事件
	 */
	private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener() {

		/**
		 * 收到BLE终端写入数据回调,操作蓝牙模块
		 */
		
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			LogTool.i(TAG, "操作蓝牙模块");
			LogTool.e(TAG, "onCharWrite " + gatt.getDevice().getName()
					+ "  write " + characteristic.getUuid().toString() + " -> "
					+ Utils.bytesToHexString(characteristic.getValue()));

			if (Utils.bytesToHexString(characteristic.getValue()).equals("01")) {
				LogTool.i(TAG, "拍照");
				// 先切换一下
				// 完成快捷呼叫功能

				Intent phone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ "18046175915"));
				phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				startActivity(phone);
			}
			if (Utils.bytesToHexString(characteristic.getValue()).equals("05")) {
				LogTool.i(TAG, "呼叫");
			}
			LogTool.i("==" + Utils.bytesToHexString(characteristic.getValue()));
		}

		/**
		 * BLE终端数据被读的事件，操作手机
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			LogTool.i(TAG, "操作手机,写入数据");
			if (characteristic.getUuid().toString().equals(BATTERY_KEY_DATA)) {
				int battery = characteristic.getValue()[0];
				LogTool.i("battery" + battery);
			}
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS)
				LogTool.i("rssi==" + rssi);
		}
	};

	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null) {
			return;
		}
		for (BluetoothGattService gattService : gattServices) {
			// ---Service的字段信息
			int type = gattService.getType();
			LogTool.e(TAG, "-->service type:" + Utils.getServiceType(type));
			LogTool.e(TAG, "-->includedServices size:"
					+ gattService.getIncludedServices().size());
			LogTool.e(TAG, "-->service uuid:" + gattService.getUuid());

			// ---Characteristics的字段信息---//

			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

				LogTool.e(TAG, "---->characteristics uuid:"
						+ gattCharacteristic.getUuid());

				int permission = gattCharacteristic.getPermissions();
				LogTool.e(
						TAG,
						"---->characteristics permission:"
								+ Utils.getCharPermission(permission));

				int property = gattCharacteristic.getProperties();
				LogTool.e(
						TAG,
						"---->characteristics property:"
								+ Utils.getCharPropertie(property));

				byte[] data = gattCharacteristic.getValue();
				if (data != null && data.length > 0) {
					LogTool.e(TAG,
							"---->char value:" + Utils.bytesToHexString(data));
				}

				// UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
				if (gattCharacteristic.getUuid().toString()
						.equals(UUID_KEY_DATA)) {
					// 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mBLE.readCharacteristic(gattCharacteristic);
						}
					}, 500);
				}

				// 非常关键的一步，让Characteristic可以接受被写入的功能
				mBLE.setCharacteristicNotification(gattCharacteristic, true);

				// if(gattCharacteristic.getUuid().toString().equals(PASS_KEY_DATA)){
				// gattCharacteristic.setValue("01FE00660065");
				// //设置数据内容
				// Log.i("~~~~~~", "取消密码");
				// //往蓝牙模块写入数据
				// mBLE.writeCharacteristic(gattCharacteristic);
				// }

			}

		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			// 通知UI

			boolean flag = true;
			for (int i = 0; i < list.size(); i++) {
				System.out.println("addr:" + device.getAddress());
				if (list.get(i).getEquipMentAddress().equals(device.getAddress())) {
					flag = false;
				}
			}
			if (flag) {
				LogTool.i("捕获一只野生蓝牙" + device.getName());
				LogTool.i("捕获一只野生蓝牙" + device.getAddress());
				Intent bleintent = new Intent();
				bleintent.setAction("com.example.communication.RECEIVER");
				bleintent.putExtra("addr", device.getAddress() + "");
				bleintent.putExtra("name", device.getName() + "");
				sendBroadcast(bleintent);
			} else {
				LogTool.i("数据库已经有了..");
			}
			// mBluetoothAdapter.stopLeScan(mLeScanCallback);
			// mBLE.connect(device.getAddress());
		}
	};

	public static boolean connect(String address) {
		return mBLE.connect(address);
	}
	

}
