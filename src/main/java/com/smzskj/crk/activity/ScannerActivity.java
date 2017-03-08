package com.smzskj.crk.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartdevicesdk.device.DeviceInfo;
import com.smartdevicesdk.device.DeviceManage;
import com.smartdevicesdk.scanner.ScannerHelper3501;
import com.smartdevicesdk.utils.HandlerMessage;
import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.utils.L;

/**
 * Created by ztt on 2017/1/17.
 */

public class ScannerActivity extends BaseActivity {

	public static ScannerHelper3501 scanner = null;
	private Button btnScan;
	private TextView textView;
	private MediaPlayer player;

	public static void startInActivity(Context context) {
		Intent intent = new Intent(context, ScannerActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);

		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);

		btnScan = (Button) findViewById(R.id.btnScan);
		btnScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				textView.setText("please press scan");
				scanner.scan();
			}
		});
		textView = (TextView) findViewById(R.id.textView_scan);
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == HandlerMessage.SCANNER_DATA_MSG) {
//					player.start();
					L.e("扫描：" + msg.obj);
//					msg.
					textView.setText(msg.obj.toString());
				}
			}
		};
		DeviceInfo devInfo = DeviceManage.getDevInfo("PDA3501");
		// 获取扫描模块串口名称
		String device = devInfo.getScannerSerialport();
		// 获取扫描模块串口波特率
		int baudrate = devInfo.getScannerBaudrate();
		scanner = new ScannerHelper3501(device, baudrate, handler);
	}
}
