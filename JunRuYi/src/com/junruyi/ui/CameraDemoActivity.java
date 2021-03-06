package com.junruyi.ui;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.junruyi.entities.EquipMent;
import com.junruyi.entities.Location;
import com.junruyi.ui.MainActivity.DisconnectReceiver;
import com.smallrhino.junruyi.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CameraDemoActivity extends Activity{

	private final static String TAG = "CameraActivity";
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private File picture;
	private Button btnSave;
	private TakePhotoReceiver takePhotoReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		setupViews();
		//注册蓝牙广播拍照
		takePhotoReceiver = new TakePhotoReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.xxn.takephoto");
		registerReceiver(takePhotoReceiver, intentFilter);
	}
	
	private void setupViews(){
		surfaceView = (SurfaceView) findViewById(R.id.camera_preview); // Camera interface to instantiate components
		surfaceHolder = surfaceView.getHolder(); // Camera interface to instantiate components
		surfaceHolder.addCallback(surfaceCallback); // Add a callback for the SurfaceHolder
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		btnSave = (Button) findViewById(R.id.save_pic);
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				takePic();
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA
				|| keyCode == KeyEvent.KEYCODE_SEARCH) {
			takePic();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void takePic() {

		camera.stopPreview();// stop the preview

		camera.takePicture(null, null, pictureCallback); // picture
	}

	// Photo call back
	Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		//@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			new SavePictureTask().execute(data);
			camera.startPreview();
		}
	};

	// save pic
	class SavePictureTask extends AsyncTask<byte[], String, String> {
		@Override
		protected String doInBackground(byte[]... params) {
			String fname = DateFormat.format("yyyyMMddhhmmss", new Date()).toString()+".jpg";
			
			Log.i(TAG, "fname="+fname+";dir="+Environment.getExternalStorageDirectory());
			//picture = new File(Environment.getExternalStorageDirectory(),fname);// create file
			
			picture = new File(Environment.getExternalStorageDirectory()+"/"+fname);
			
			try {
				FileOutputStream fos = new FileOutputStream(picture.getPath()); // Get file output stream
				fos.write(params[0]); // Written to the file
				fos.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// SurfaceHodler Callback handle to open the camera, off camera and photo size changes
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		public void surfaceCreated(SurfaceHolder holder) {
			Log.i(TAG, "surfaceCallback====");
			camera = Camera.open(); // Turn on the camera
			try {
				camera.setPreviewDisplay(holder); // Set Preview
			} catch (IOException e) {
				camera.release();// release camera
				camera = null;
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.i(TAG,"====surfaceChanged");
			Camera.Parameters parameters = camera.getParameters(); // Camera parameters to obtain
			parameters.setPictureFormat(PixelFormat.JPEG);// Setting Picture Format
//			parameters.set("rotation", 180); // Arbitrary rotation
			camera.setDisplayOrientation(0);
//			parameters.setPreviewSize(400, 300); // Set Photo Size
			camera.setParameters(parameters); // Setting camera parameters
			camera.startPreview(); // Start Preview
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.i(TAG,"====surfaceDestroyed");
			camera.stopPreview();// stop preview
			camera.release(); // Release camera resources
			camera = null;
		}
	};
	public class TakePhotoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拿到数据
			Toast.makeText(CameraDemoActivity.this, "蓝牙拍照", Toast.LENGTH_LONG).show();
			takePic();
		}
	}
}