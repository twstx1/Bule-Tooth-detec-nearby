package com.example.test5;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;

	private Button button;
	private Button clean;
	private TextView textview1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button1);
		clean = (Button) findViewById(R.id.button2);
		clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textview1.setText("");
			}
		});
		textview1 = (TextView) findViewById(R.id.Textview1);
		button.setOnClickListener(new blue());
		IntentFilter Filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(Receiver, Filter);

		Filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(Receiver, Filter);

	}

	private final BroadcastReceiver Receiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

					textview1.append(device.getName() + ":"
							+ device.getAddress() + "\n\n");
				}

			}

		}
	};

	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 解除注册
		unregisterReceiver(Receiver);
	}

	class blue implements OnClickListener {

		@Override
		public void onClick(View v) {

			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				Toast.makeText(MainActivity.this, "此设备不支持蓝牙传输功能！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "此设备支持蓝牙传输功能！",
						Toast.LENGTH_SHORT).show();
				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					enableIntent.putExtra(
							BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
					Toast.makeText(MainActivity.this, "蓝牙设备已经打开！",
							Toast.LENGTH_SHORT).show();

				}
				if (mBluetoothAdapter.isDiscovering()) {
					mBluetoothAdapter.cancelDiscovery();
				}
				// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
				mBluetoothAdapter.startDiscovery();

				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
						.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice bluetoothDevice : pairedDevices) {
						textview1.append(bluetoothDevice.getName() + ":"
								+ bluetoothDevice.getAddress() + "\n\n");
					}
				}
			}
		}
	}
}
