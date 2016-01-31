package com.junruyi.service;

import java.util.ArrayList;
import java.util.List;

import com.junruyi.utils.BluetoothLeClass;
import com.junruyi.utils.Utils;
import com.junruyi.utils.BluetoothLeClass.OnDataAvailableListener;
import com.junruyi.utils.BluetoothLeClass.OnServiceDiscoverListener;

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
import android.util.Log;
import android.widget.Toast;

public class BlueToothService extends Service {

	private final static String TAG = BlueToothService.class.getSimpleName();
	private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
	private final static String PASS_KEY_DATA = "0000ffe2-0000-1000-8000-00805f9b34fb";
	private final static String BATTERY_KEY_DATA = "00002a19-0000-1000-8000-00805f9b34fb";
	
	private boolean mScanning;
	private Handler mHandler;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothLeClass mBLE;

	
	private List<BluetoothDevice> devices ;
	private static final long SCAN_PERIOD = 10000;
	
	private Intent bleintent = new Intent("com.example.communication.RECEIVER");
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("onCreated");
		mHandler = new Handler();
		devices = new ArrayList<>();

		//初始化蓝牙适配器
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();
			onDestroy();
		}

		//该设备支持蓝牙，开启蓝牙
		mBluetoothAdapter.enable();
		mBLE = new BluetoothLeClass(getApplicationContext());
		if (!mBLE.initialize()) {
			Log.e(TAG, "Unable to initialize Bluetooth");
			onDestroy();
		}

		//发现BLE终端的Service时回调
		mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
		//收到BLE终端数据交互的事件
		mBLE.setOnDataAvailableListener(mOnDataAvailable);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onStart");
		
		//开始搜索蓝牙设备
		scanLeDevice(true);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroy");
		super.onDestroy();
	}

	private void scanLeDevice(final boolean enable){
		if(enable){
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		}
		else{
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
			//int ss = mBLE.getRssi();
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
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			Log.i(TAG, "操作蓝牙模块");
			Log.e(TAG, "onCharWrite "+ gatt.getDevice().getName()+"  write "+
			characteristic.getUuid().toString()+" -> "+Utils.bytesToHexString(characteristic.getValue()));
			
			if(Utils.bytesToHexString(characteristic.getValue()).equals("01")){
				Log.i(TAG, "拍照");
				//先切换一下
				//完成快捷呼叫功能
				
				Intent phone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"18046175915"));
				phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
				startActivity(phone);
			}
			if(Utils.bytesToHexString(characteristic.getValue()).equals("05")){
				Log.i(TAG, "呼叫");
			}
			System.out.println("=="+Utils.bytesToHexString(characteristic.getValue()));
		}
		/**
		 * BLE终端数据被读的事件，操作手机
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			Log.i(TAG, "操作手机,写入数据");
			if(characteristic.getUuid().toString().equals(BATTERY_KEY_DATA)){
				int battery  = characteristic.getValue()[0];
				System.out.println("battery"+battery);
			}
		}
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			if(status == BluetoothGatt.GATT_SUCCESS)
				System.out.println("rssi=="+rssi);
		}
	};

	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null) {
			return;
		}
		for (BluetoothGattService gattService : gattServices) {
			//---Service的字段信息
			int type = gattService.getType();
			Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
			Log.e(TAG, "-->includedServices size:" + gattService.getIncludedServices().size());
			Log.e(TAG, "-->service uuid:" + gattService.getUuid());

			//---Characteristics的字段信息---//

			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

				Log.e(TAG, "---->characteristics uuid:" + gattCharacteristic.getUuid());

				int permission = gattCharacteristic.getPermissions();
				Log.e(TAG, "---->characteristics permission:" + Utils.getCharPermission(permission));

				int property = gattCharacteristic.getProperties();
				Log.e(TAG, "---->characteristics property:" + Utils.getCharPropertie(property));

				byte[] data = gattCharacteristic.getValue();
				if (data != null && data.length > 0) {
					Log.e(TAG, "---->char value:"+Utils.bytesToHexString(data));
				}
				
				//UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
				if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA)){
					//测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mBLE.readCharacteristic(gattCharacteristic);
						}
					}, 500);
				}
				
				
				
				
				//非常关键的一步，让Characteristic可以接受被写入的功能
				mBLE.setCharacteristicNotification(gattCharacteristic, true);
				
//				if(gattCharacteristic.getUuid().toString().equals(PASS_KEY_DATA)){
//					gattCharacteristic.setValue("01FE00660065");
//					//设置数据内容
//					Log.i("~~~~~~", "取消密码");
//					//往蓝牙模块写入数据
//					mBLE.writeCharacteristic(gattCharacteristic);
//				}
				
			}

		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new LeScanCallback() {
		
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			//通知UI
			if(!devices.contains(device))
			{
				System.out.println("捕获一只野生蓝牙");
				devices.add(device);
				System.out.println("准备对接"+devices.size());
				System.out.println("devices:"+devices.toString());
				bleintent.putExtra("progress", device.toString());
				Log.e("progress.......", device + "");
				sendBroadcast(bleintent);
			}
//			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			//mBLE.connect(device.getAddress());
		}
	};
	
}
