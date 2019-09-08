package shacs.dmotion.com.shacs;

import android.app.ProgressDialog;
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
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

import shacs.dmotion.com.shacs.bluetooth.Constants;
import shacs.dmotion.com.shacs.service.BTCTemplateService;
import widget.MultiDirectionSlidingDrawer;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizerManager mSpeechManager;
    private TextView textView;

    private MyHandler mHandler;

    private MultiDirectionSlidingDrawer mDrawer;
    private LeftMenuView menuView;
    private boolean bMenuOpen = false;
    private LinearLayout rightView = null;
    private boolean bClose = false;
    private ImageView usbIcon;

    private String TAG = "BLUETOOTH";

    protected ProgressDialog mProgressDialog = null;
    public static boolean bUsbConnection = false;
    private ActivityHandler mActivityHandler;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_READY:
                    Toast.makeText(context, "USB가 연결되었습니다.", Toast.LENGTH_SHORT).show();
                    bUsbConnection = true;
                    usbIcon.setVisibility(View.VISIBLE);
                    break;
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Permission granted", Toast.LENGTH_SHORT).show();
                    bUsbConnection = true;
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    bUsbConnection = false;
                    usbIcon.setVisibility(View.INVISIBLE);
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    bUsbConnection = false;
                    usbIcon.setVisibility(View.INVISIBLE);
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    bUsbConnection = false;
                    usbIcon.setVisibility(View.INVISIBLE);
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    bUsbConnection = false;
                    usbIcon.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    //블루투스 검색결과 BroadcastReceiver
    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action){
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(paired.getBondState()==BluetoothDevice.BOND_BONDED){
                        if(paired.getAddress().equals(BluetoothService.strDeviceAddr)) {
                            Toast.makeText(getApplicationContext(), "SHACS 페어링", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }
    };

    public static UsbService usbService;

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean bFirst = pref.getBoolean("FIRST", true);

        if(bFirst) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("FIRST", false);

            byte[] tempCmd1 = {0x03, 0x03, 0x02, 0x01};
            String strCommand = Base64.encodeToString(tempCmd1, Base64.DEFAULT);
            editor.putString("COMMAND_01", strCommand);

            byte[] tempCmd2 = {0x03, 0x03, 0x02, 0x00};
            strCommand = Base64.encodeToString(tempCmd2, Base64.DEFAULT);
            editor.putString("COMMAND_02", strCommand);

            byte[] tempCmd3 = {0x03, 0x03, 0x03, 0x01};
            strCommand = Base64.encodeToString(tempCmd3, Base64.DEFAULT);
            editor.putString("COMMAND_03", strCommand);

            byte[] tempCmd4 = {0x03, 0x03, 0x03, 0x00};
            strCommand = Base64.encodeToString(tempCmd4, Base64.DEFAULT);
            editor.putString("COMMAND_04", strCommand);

            byte[] tempCmd5 = {0x03, 0x03, 0x04, 0x01};
            strCommand = Base64.encodeToString(tempCmd5, Base64.DEFAULT);
            editor.putString("COMMAND_05", strCommand);
            byte[] tempCmd6 = {0x03, 0x03, 0x04, 0x00};
            strCommand = Base64.encodeToString(tempCmd6, Base64.DEFAULT);
            editor.putString("COMMAND_06", strCommand);

            byte[] tempCmd7 = {0x03, 0x03, 0x05, 0x01};
            strCommand = Base64.encodeToString(tempCmd7, Base64.DEFAULT);
            editor.putString("COMMAND_07", strCommand);

            byte[] tempCmd8 = {0x03, 0x03, 0x05, 0x00};
            strCommand = Base64.encodeToString(tempCmd8, Base64.DEFAULT);
            editor.putString("COMMAND_08", strCommand);

            byte[] tempCmd9 = {0x03, 0x03, 0x06, 0x01};
            strCommand = Base64.encodeToString(tempCmd9, Base64.DEFAULT);
            editor.putString("COMMAND_09", strCommand);

            byte[] tempCmd10 = {0x03, 0x03, 0x06, 0x00};
            strCommand = Base64.encodeToString(tempCmd10, Base64.DEFAULT);
            editor.putString("COMMAND_10", strCommand);

            byte[] tempCmd11 = {0x03, 0x03, 0x07, 0x01};
            strCommand = Base64.encodeToString(tempCmd11, Base64.DEFAULT);
            editor.putString("COMMAND_11", strCommand);

            byte[] tempCmd12 = {0x03, 0x03, 0x07, 0x00};
            strCommand = Base64.encodeToString(tempCmd12, Base64.DEFAULT);
            editor.putString("COMMAND_12", strCommand);

            editor.commit();
        }

        mActivityHandler = new ActivityHandler();

        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBluetoothSearchReceiver, searchFilter);

        mHandler = new MyHandler(this);

        textView = (TextView)findViewById(R.id.textView);
        usbIcon = (ImageView)findViewById(R.id.usbIcon);

        mDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
        menuView = new LeftMenuView(this, mDrawer);
        rightView = (LinearLayout)findViewById(R.id.rightView);

        ImageButton menuBtn = (ImageButton)findViewById(R.id.menuBtn);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.animateToggle();
            }
        });

        rightView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        if(bMenuOpen){
                            mDrawer.animateToggle();

                            return true;
                        } else
                            return false;
                    case MotionEvent.ACTION_MOVE :
                        break;
                    case MotionEvent.ACTION_UP :
                        break;
                }

                return false;
            }
        });

        bMenuOpen = mDrawer.isOpened();

        mDrawer.setOnAlphaChangeListener(new MultiDirectionSlidingDrawer.OnAlphaChangeListener() {
            public void onAlphaChanged(float fAlpha) {
                if(fAlpha == 0)
                    bMenuOpen = false;
                else
                    bMenuOpen = true;
            }
        });

        mDrawer.setOnDrawerOpenListener(new MultiDirectionSlidingDrawer.OnDrawerOpenListener() {
            public void onDrawerOpened() {
            }
        });

        doStartService();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        unregisterReceiver(mBluetoothSearchReceiver);
        doStopService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case PermissionHandler.RECORD_AUDIO:
                if(grantResults.length>0) {
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                        if(mSpeechManager==null)
                        {
                            SetSpeechListener();
                        }
                        else if(!mSpeechManager.ismIsListening())
                        {
                            mSpeechManager.destroy();
                            SetSpeechListener();
                        }
                        textView.setText("음성인식 대기중...");
                    }
                }
                break;
        }

        // 전등 꺼,              전등 켜,              선풍기 꺼,              선풍기 켜
        // 0xab 0xad 0x01 0x00,  0xab 0xad 0x01 0x01 , 0xab 0xad 0x02 0x00   , 0xab 0xad 0x02 0x01
    }

    private void SetSpeechListener()
    {
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {
                if(results!=null && results.size()>0)
                {
                    if(results.size()==1)
                    {
                        textView.setText(results.get(0));

                        String strResult = results.get(0);
                        strResult = strResult.replaceAll(" ", "");

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        String strIot1 = pref.getString("IOT01", "");
                        String strIot2 = pref.getString("IOT02", "");
                        String strIot3 = pref.getString("IOT03", "");
                        String strIot4 = pref.getString("IOT04", "");
                        String strIot5 = pref.getString("IOT05", "");
                        String strIot6 = pref.getString("IOT06", "");

                        if(usbService != null && bUsbConnection == true) {
                            if(strResult.contains(strIot1 + "켜")) {
                                usbService.write(Base64.decode(getCommand(1), Base64.DEFAULT));
                            } else if(strResult.contains(strIot1 + "꺼")) {
                                usbService.write(Base64.decode(getCommand(2), Base64.DEFAULT));
                            } else if(strResult.contains(strIot2 + "켜")) {
                                usbService.write(Base64.decode(getCommand(3), Base64.DEFAULT));
                            } else if(strResult.contains(strIot2 + "꺼")) {
                                usbService.write(Base64.decode(getCommand(4), Base64.DEFAULT));
                            } else if(strResult.contains(strIot3 + "켜")) {
                                usbService.write(Base64.decode(getCommand(5), Base64.DEFAULT));
                            } else if(strResult.contains(strIot3 + "꺼")) {
                                usbService.write(Base64.decode(getCommand(6), Base64.DEFAULT));
                            } else if(strResult.contains(strIot4 + "켜")) {
                                usbService.write(Base64.decode(getCommand(7), Base64.DEFAULT));
                            } else if(strResult.contains(strIot4 + "꺼")) {
                                usbService.write(Base64.decode(getCommand(8), Base64.DEFAULT));
                            } else if(strResult.contains(strIot5 + "켜")) {
                                usbService.write(Base64.decode(getCommand(9), Base64.DEFAULT));
                            } else if(strResult.contains(strIot5 + "꺼")) {
                                usbService.write(Base64.decode(getCommand(10), Base64.DEFAULT));
                            } else if(strResult.contains(strIot6 + "켜")) {
                                usbService.write(Base64.decode(getCommand(11), Base64.DEFAULT));
                            } else if(strResult.contains(strIot6 + "꺼")) {
                                usbService.write(Base64.decode(getCommand(12), Base64.DEFAULT));
                            }
                        } else if(Utils.mService != null) {
                            if(strResult.contains(strIot1 + "켜")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(1), Base64.DEFAULT));
                            } else if(strResult.contains(strIot1 + "꺼")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(2), Base64.DEFAULT));
                            } else if(strResult.contains(strIot2 + "켜")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(3), Base64.DEFAULT));
                            } else if(strResult.contains(strIot2 + "꺼")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(4), Base64.DEFAULT));
                            } else if(strResult.contains(strIot3 + "켜")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(5), Base64.DEFAULT));
                            } else if(strResult.contains(strIot3 + "꺼")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(6), Base64.DEFAULT));
                            } else if(strResult.contains(strIot4 + "켜")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(7), Base64.DEFAULT));
                            } else if(strResult.contains(strIot4 + "꺼")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(8), Base64.DEFAULT));
                            } else if(strResult.contains(strIot5 + "켜")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(9), Base64.DEFAULT));
                            } else if(strResult.contains(strIot5 + "꺼")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(10), Base64.DEFAULT));
                            } else if(strResult.contains(strIot6 + "켜")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(11), Base64.DEFAULT));
                            } else if(strResult.contains(strIot6 + "꺼")) {
                                Utils.mService.sendMessageToRemote(Base64.decode(getCommand(12), Base64.DEFAULT));
                            }
                        }
                    }
                    else {
                        StringBuilder sb = new StringBuilder();
                        if (results.size() > 5) {
                            results = (ArrayList<String>) results.subList(0, 5);
                        }
                        for (String strResult : results) {
                            strResult = strResult.replaceAll(" ", "");

                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            String strIot1 = pref.getString("IOT01", "");
                            String strIot2 = pref.getString("IOT02", "");
                            String strIot3 = pref.getString("IOT03", "");
                            String strIot4 = pref.getString("IOT04", "");
                            String strIot5 = pref.getString("IOT05", "");
                            String strIot6 = pref.getString("IOT06", "");

                            if(usbService != null && bUsbConnection == true) {
                                if(strResult.contains(strIot1 + "켜")) {
                                    usbService.write(Base64.decode(getCommand(1), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot1 + "꺼")) {
                                    usbService.write(Base64.decode(getCommand(2), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot2 + "켜")) {
                                    usbService.write(Base64.decode(getCommand(3), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot2 + "꺼")) {
                                    usbService.write(Base64.decode(getCommand(4), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot3 + "켜")) {
                                    usbService.write(Base64.decode(getCommand(5), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot3 + "꺼")) {
                                    usbService.write(Base64.decode(getCommand(6), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot4 + "켜")) {
                                    usbService.write(Base64.decode(getCommand(7), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot4 + "꺼")) {
                                    usbService.write(Base64.decode(getCommand(8), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot5 + "켜")) {
                                    usbService.write(Base64.decode(getCommand(9), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot5 + "꺼")) {
                                    usbService.write(Base64.decode(getCommand(10), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot6 + "켜")) {
                                    usbService.write(Base64.decode(getCommand(11), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot6 + "꺼")) {
                                    usbService.write(Base64.decode(getCommand(12), Base64.DEFAULT));
                                    break;
                                }
                            } else if(Utils.mService != null) {
                                if(strResult.contains(strIot1 + "켜")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(1), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot1 + "꺼")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(2), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot2 + "켜")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(3), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot2 + "꺼")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(4), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot3 + "켜")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(5), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot3 + "꺼")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(6), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot4 + "켜")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(7), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot4 + "꺼")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(8), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot5 + "켜")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(9), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot5 + "꺼")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(10), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot6 + "켜")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(11), Base64.DEFAULT));
                                    break;
                                } else if(strResult.contains(strIot6 + "꺼")) {
                                    Utils.mService.sendMessageToRemote(Base64.decode(getCommand(12), Base64.DEFAULT));
                                    break;
                                }
                            }
                        }

                        textView.setText(results.get(0));
                    }
                } else
                    textView.setText("음성 인식 대기중...");
            }
        });
    }

    private String getCommand(int nIndex) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String strCommand = String.format("COMMAND_%02d", nIndex);
        return pref.getString(strCommand, "");
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it

        if(PermissionHandler.checkPermission(MainActivity.this, PermissionHandler.RECORD_AUDIO)) {
            if(mSpeechManager==null)
            {
                SetSpeechListener();
            }
            else if(!mSpeechManager.ismIsListening())
            {
                mSpeechManager.destroy();
                SetSpeechListener();
            }
            textView.setText("음성인식 대기중...");
        } else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO, MainActivity.this);
        }
    }

    @Override
    protected void onPause() {
        if(mSpeechManager!=null) {
            mSpeechManager.destroy();
            mSpeechManager = null;
        }

        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);

        if(mSpeechManager!=null) {
            mSpeechManager.destroy();
            mSpeechManager = null;
        }

        super.onPause();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
//                    String data = (String) msg.obj;
//                    Toast.makeText(mActivity.get(), data, Toast.LENGTH_LONG).show();
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("블루투스 장치에 연결중입니다.");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    private final Handler mBleHandler = new Handler() {                        // 블루투스 메세지 리스너
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();

                    switch (msg.what) {
                        case BluetoothService.STATE_CONNECT_FAILED:
                            Toast.makeText(MainActivity.this, "블루투스 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
                            startActivityForResult(new Intent(MainActivity.this, BlutoothSearchListPopupActivity.class), BluetoothService.CONNECT_BT);
                            break;
                        case BluetoothService.STATE_CONNECT_LOST:
                            Toast.makeText(MainActivity.this, "블루투스 연결이 끊어졌습니다.\n다시 연결해 주세요.", Toast.LENGTH_LONG).show();
                            startActivityForResult(new Intent(MainActivity.this, BlutoothSearchListPopupActivity.class), BluetoothService.CONNECT_BT);
                            break;
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(MainActivity.this, "블루투스가 연결되었습니다.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        }
    };

    private ServiceConnection mServiceConn = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, "Activity - Service connected");

            Utils.mService = ((BTCTemplateService.ServiceBinder) binder).getService();

            // Activity couldn't work with mService until connections are made
            // So initialize parameters and settings here. Do not initialize while running onCreate()
            initialize();
        }

        public void onServiceDisconnected(ComponentName className) {
            Utils.mService = null;
        }
    };

    private void doStartService() {
        Log.d(TAG, "# Activity - doStartService()");
        startService(new Intent(this, BTCTemplateService.class));
        bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stop the service
     */
    private void doStopService() {
        Log.d(TAG, "# Activity - doStopService()");
        Utils.mService.finalizeService();
        stopService(new Intent(this, BTCTemplateService.class));
    }

    /**
     * Initialization / Finalization
     */
    private void initialize() {
        Log.d(TAG, "# Activity - initialize()");

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "블루투스가 지원되지 않는 장치입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Utils.mService.setupService(mActivityHandler);

        // If BT is not on, request that it be enabled.
        // RetroWatchService.setupBT() will then be called during onActivityResult
        if(!Utils.mService.isBluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
        }
    }

    public class ActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what) {
                // Receives BT state messages from service
                // and updates BT state UI
                case Constants.MESSAGE_BT_STATE_INITIALIZED:            // 초기화중
                    Toast.makeText(getApplicationContext(), "블루투스 초기화중입니다. 잠시 기다려주세요.", Toast.LENGTH_LONG).show();
                    break;
                case Constants.MESSAGE_BT_STATE_LISTENING:              // 대기중(연결안됨)
                    Toast.makeText(getApplicationContext(), "SHCA 장치를 선택해주세요.", Toast.LENGTH_LONG).show();
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTING:             // 연결중
                    Toast.makeText(getApplicationContext(), "블루투스 연결중입니다.", Toast.LENGTH_LONG).show();
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTED:              // 연결됨
                    Toast.makeText(getApplicationContext(), "블루투스가 연결되었습니다.", Toast.LENGTH_LONG).show();
                    if(Utils.mService != null) {
                        String deviceName = Utils.mService.getDeviceName();
                        if(deviceName != null) {
                        } else {
                        }
                    }
                    break;
                case Constants.MESSAGE_BT_STATE_ERROR:
                    Toast.makeText(getApplicationContext(), "블루투스 연결중 에러가 발생했습니다.", Toast.LENGTH_LONG).show();
                    break;

                // BT Command status
                case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:             // 전송실패
                    Toast.makeText(getApplicationContext(), "블루투스 전송 실패.", Toast.LENGTH_LONG).show();
                    break;

                ///////////////////////////////////////////////
                // When there's incoming packets on bluetooth
                // do the UI works like below
                ///////////////////////////////////////////////
                case Constants.MESSAGE_READ_CHAT_DATA:
                    if(msg.obj != null) {
                    }
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }
}
