package shacs.dmotion.com.shacs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import shacs.dmotion.com.shacs.bluetooth.BleManager;
import shacs.dmotion.com.shacs.bluetooth.Logs;

import static android.content.ContentValues.TAG;

public class BlutoothSearchListPopupActivity extends Activity {
    private ArrayList<HashMap<String, String>> deviceParedList;
    private ArrayList<HashMap<String, String>> deviceScanedList;
    private ArrayList<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();

    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private ListView btParedListView;
    private ListView btScanedListView;
    private CParedAdapter paredAdapter;
    private CScanedAdapter scanedAdapter;

    private int nSelectedIndex = -1;
    private int nSelectedList = -1;         // 0 = 페어링, 1 = 검색
    private boolean bSelected = false;

    protected ProgressDialog mProgressDialog = null;

    private BleManager mBleManager;
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_popup);

        deviceScanedList = new ArrayList<>();
        deviceParedList = new ArrayList<>();

        btParedListView = (ListView)findViewById(R.id.btParedListView);
        btScanedListView = (ListView)findViewById(R.id.btScanedListView);

        btParedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(nSelectedIndex == position) {
                    bSelected = false;
                    nSelectedList = -1;
                } else {
                    nSelectedIndex = position;
                    nSelectedList = 1;
                    bSelected = true;
                }

                paredAdapter.notifyDataSetChanged();
            }
        });

        btScanedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(nSelectedIndex == position) {
                    bSelected = false;
                    nSelectedList = -1;
                } else {
                    nSelectedIndex = position;
                    nSelectedList = 2;
                    bSelected = true;
                }

                scanedAdapter.notifyDataSetChanged();
            }
        });

        Button okBtn = (Button)findViewById(R.id.okBtn);
        Button cancelBtn = (Button)findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nSelectedIndex == -1) {
                    Toast.makeText(BlutoothSearchListPopupActivity.this, "블루투스 장치를 선택해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                HashMap<String, String> deviceMap = null;

                if(nSelectedList == 1)
                    deviceMap = deviceParedList.get(nSelectedIndex);
                else if(nSelectedList == 2)
                    deviceMap = deviceScanedList.get(nSelectedIndex);

                Intent intent = new Intent();
                intent.putExtra("DEVICE_NAME", deviceMap.get("DEVICE_NAME"));
                intent.putExtra("DEVICE_ADDR", deviceMap.get("DEVICE_ADDR"));
                intent.putExtra("DEVICE_RSSI", deviceMap.get("DEVICE_RSSI"));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get BLE Manager
        mBleManager = BleManager.getInstance(getApplicationContext(), null);
        mBleManager.setScanCallback(mLeScanCallback);

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.adapter_device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.adapter_device_name);

        btParedListView.setAdapter(mPairedDevicesArrayAdapter);
        btParedListView.setOnItemClickListener(mParedClickListener);

        // Find and set up the ListView for newly discovered devices
        btScanedListView.setAdapter(mNewDevicesArrayAdapter);
        btScanedListView.setOnItemClickListener(mScanedClickListener);
    }

    private AdapterView.OnItemClickListener mParedClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            if(info != null && info.length() > 16) {
                String address = info.substring(info.length() - 17);
                Log.d(TAG, "User selected device : " + address);

                // Create the result Intent and include the MAC address
                Intent intent = new Intent();

                HashMap<String, String> currentMap = deviceParedList.get(position);

                intent.putExtra("DEVICE_ADDR", currentMap.get("DEVICE_ADDR"));
                intent.putExtra("DEVICE_NAME", currentMap.get("DEVICE_NAME"));
                intent.putExtra("DEVICE_RSSI", currentMap.get("DEVICE_RSSI"));

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    private AdapterView.OnItemClickListener mScanedClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            if(info != null && info.length() > 16) {
                String address = info.substring(info.length() - 17);
                Log.d(TAG, "User selected device : " + address);

                // Create the result Intent and include the MAC address
                Intent intent = new Intent();

                HashMap<String, String> currentMap = deviceScanedList.get(position);

                intent.putExtra("DEVICE_ADDR", currentMap.get("DEVICE_ADDR"));
                intent.putExtra("DEVICE_NAME", currentMap.get("DEVICE_NAME"));
                intent.putExtra("DEVICE_RSSI", currentMap.get("DEVICE_RSSI"));

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                HashMap<String, String> map = new HashMap<>();
                map.put("DEVICE_NAME", device.getName());
                map.put("DEVICE_ADDR", device.getAddress());
                deviceParedList.add(map);
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "페어링된 기기 없음";
            mPairedDevicesArrayAdapter.add(noDevices);
        }

        mBleManager.scanLeDevice(true);

    }

    @Override
    public void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }

    public void onBackPressed() {
        return;
    }

    private boolean checkDuplicated(BluetoothDevice device) {
        for(BluetoothDevice dvc : mDevices) {
            if(device.getAddress().equalsIgnoreCase(dvc.getAddress())) {
                return true;
            }
        }
        return false;
    }

    private void doDiscovery() {
        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // Empty cache
        mDevices.clear();

        // If we're already discovering, stop it
        if (mBleManager.getState() == BleManager.STATE_SCANNING) {
            mBleManager.scanLeDevice(false);
        }

        // Request discover from BluetoothAdapter
        mBleManager.scanLeDevice(true);

        // Stops scanning after a pre-defined scan period.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopDiscovery();
            }
        }, 8 * 1000);
    }

    private void stopDiscovery() {
        mBleManager.scanLeDevice(false);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    Logs.d("# Scan device rssi is " + rssi);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                                if(!checkDuplicated(device)) {
                                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                                    mNewDevicesArrayAdapter.notifyDataSetChanged();

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("DEVICE_NAME", device.getName());
                                    map.put("DEVICE_ADDR", device.getAddress());
                                    map.put("DEVICE_RSSI", "" + rssi);
                                    deviceScanedList.add(map);

                                    mDevices.add(device);
                                }
                            }
                        }
                    });
                }
            };

    public class CParedAdapter extends ArrayAdapter<Object>
    {
        private int mCellLayout;
        private LayoutInflater mLiInflater;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        CParedAdapter(Context context, int layout, List titles)
        {
            super(context, layout, titles);
            mCellLayout = layout;

            mLiInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null || convertView != mLiInflater.inflate(mCellLayout, parent, false)) {
                convertView = mLiInflater.inflate(mCellLayout, parent, false);
            }

            HashMap<String, String> currentMap = deviceParedList.get(position);

            TextView textView1 = (TextView)convertView.findViewById(R.id.textView1);
            TextView textView2 = (TextView)convertView.findViewById(R.id.textView2);
            LinearLayout listBgLayout = (LinearLayout)convertView.findViewById(R.id.listBgLayout);

            textView1.setText(currentMap.get("DEVICE_NAME"));
            textView2.setText(currentMap.get("DEVICE_ADDR"));

            listBgLayout.setBackgroundColor(Color.parseColor("#ffffff"));

            if(nSelectedIndex > -1 && nSelectedIndex == position && nSelectedList == 1) {
                if(bSelected) {
                    listBgLayout.setBackgroundColor(Color.parseColor("#ff9db6"));
                } else {
                    nSelectedIndex = -1;
                }
            }

            return convertView;
        }
    }

    public class CScanedAdapter extends ArrayAdapter<Object>
    {
        private int mCellLayout;
        private LayoutInflater mLiInflater;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        CScanedAdapter(Context context, int layout, List titles)
        {
            super(context, layout, titles);
            mCellLayout = layout;

            mLiInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null || convertView != mLiInflater.inflate(mCellLayout, parent, false)) {
                convertView = mLiInflater.inflate(mCellLayout, parent, false);
            }

            HashMap<String, String> currentMap = deviceScanedList.get(position);

            TextView textView1 = (TextView)convertView.findViewById(R.id.textView1);
            TextView textView2 = (TextView)convertView.findViewById(R.id.textView2);
            LinearLayout listBgLayout = (LinearLayout)convertView.findViewById(R.id.listBgLayout);

            textView1.setText(currentMap.get("DEVICE_NAME"));
            textView2.setText(currentMap.get("DEVICE_ADDR"));

            listBgLayout.setBackgroundColor(Color.parseColor("#ffffff"));

            if(nSelectedIndex > -1 && nSelectedIndex == position && nSelectedList == 2) {
                if(bSelected) {
                    listBgLayout.setBackgroundColor(Color.parseColor("#ff9db6"));
                } else {
                    nSelectedIndex = -1;
                }
            }

            return convertView;
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BlutoothSearchListPopupActivity.this);
            mProgressDialog.setMessage("블루투스 장치를 검색 중입니다.");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }
}
