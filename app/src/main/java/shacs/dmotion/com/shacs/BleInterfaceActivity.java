package shacs.dmotion.com.shacs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import shacs.dmotion.com.shacs.bluetooth.Constants;
import shacs.dmotion.com.shacs.service.BTCTemplateService;

public class BleInterfaceActivity extends AppCompatActivity {
    private String strDeviceName;
    private String strDeviceAddr;

    private String TAG = "BLUETOOTH";

    TextView deviceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_interface);

        Button scanBtn = (Button)findViewById(R.id.scanBtn);
        Button connectBtn = (Button)findViewById(R.id.connectBtn);
        Button sendBtnButton= (Button)findViewById(R.id.sendBtn);

        sendBtnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] msgByte = new byte[]{0x01, 0x02};
                Utils.mService.sendMessageToRemote(msgByte);
            }
        });

        deviceView = (TextView)findViewById(R.id.deviceView);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(BleInterfaceActivity.this, BlutoothSearchListPopupActivity.class), BluetoothService.CONNECT_BT);
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strDeviceAddr != null && strDeviceAddr.length() > 0)
                    Utils.mService.connectDevice(strDeviceAddr);
            }
        });

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {

        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1111);
            }
        }

//        doStartService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == BluetoothService.CONNECT_BT && resultCode == Activity.RESULT_OK){
            strDeviceName = data.getStringExtra("DEVICE_NAME");
            strDeviceAddr = data.getStringExtra("DEVICE_ADDR");

            deviceView.setText(strDeviceName + ", RSSI : " + data.getStringExtra("DEVICE_RSSI"));
        } else if(requestCode == 1010) {

        }
    }
}
