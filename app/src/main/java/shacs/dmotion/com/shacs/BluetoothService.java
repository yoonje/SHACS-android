package shacs.dmotion.com.shacs;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {
    private static BluetoothService _instance = null;

    private static final String TAG = "BluetoothService";

    public static final int ENABLE_BT = 1;
    public static final int CONNECT_BT = 2;
    public static final int VOLUME_BT = 3;
    public static final int TIMER_BT = 4;

    private Activity mActivity;
    private Handler mHandler;

//    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
private static final UUID MY_UUID = UUID.fromString("88E86285-7DD4-8C8F-7173-CDFEA69F9C32");
    private BluetoothAdapter btAdapter;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private int mState;
    public static String strDeviceAddr;

    // 상태를 나타내는 상태 변수
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECT_FAILED = 10;
    public static final int STATE_CONNECT_LOST = 11;

    public static synchronized BluetoothService getInstance(Activity ac, Handler handler) {
        if (_instance == null)
            _instance = new BluetoothService(ac, handler);

        return _instance;
    }

    public BluetoothService(Activity ac, Handler handler) {
        mActivity = ac;
        mHandler = handler;

        // BluetoothAdapter 얻기
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean getDeviceState() {
        Log.d(TAG, "Check the Bluetooth support");

        if(btAdapter == null) {
            Log.d(TAG, "Bluetooth is not available");
            return false;
        } else {
            Log.d(TAG, "Bluetooth is available");
            return true;
        }
    }

    public void enableBluetooth() {
        Log.i(TAG, "Check the enabled Bluetooth");


        if(btAdapter.isEnabled()) {
            // 기기의 블루투스 상태가 On인 경우
            Log.d(TAG, "Bluetooth Enable Now");
        } else {
            // 기기의 블루투스 상태가 Off인 경우
            Log.d(TAG, "Bluetooth Enable Request");

            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i, ENABLE_BT);
        }
    }

    public Set<BluetoothDevice> getBondedDevices() {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        return pairedDevices;
    }

    // Bluetooth 상태 set
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    // Bluetooth 상태 get
    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread == null) {

        } else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    public synchronized  void connectBLE(String strDeviceAddr) {
        BluetoothDevice device = btAdapter.getRemoteDevice(strDeviceAddr);
        device.createBond();
    }

    // ConnectThread 초기화 device의 모든 연결 제거
    public synchronized void connect(String strDeviceAddr) {
        this.strDeviceAddr = strDeviceAddr;
        BluetoothDevice device = btAdapter.getRemoteDevice(strDeviceAddr);
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread == null) {

            } else {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);

        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    // ConnectedThread 초기화
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread == null) {

        } else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    // 모든 thread stop
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    // 값을 쓰는 부분(보내는 부분)
    public void write(byte[] out) { // Create temporary object
        ConnectedThread r; // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;

            r.write(out);
        }
    }

    // 연결 실패했을때
    private void connectionFailed() {
        setState(STATE_LISTEN);
    }

    // 연결을 잃었을 때
    private void connectionLost() {
        setState(STATE_LISTEN);
        mHandler.sendEmptyMessage(STATE_CONNECT_FAILED);
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // 디바이스 정보를 얻어서 BluetoothSocket 생성
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            btAdapter.cancelDiscovery();

            // BluetoothSocket 연결 시도
            try {
                // BluetoothSocket 연결 시도에 대한 return 값은 succes 또는 exception이다.
                mmSocket.connect();
                Log.d(TAG, "Connect Success");

            } catch (IOException e) {
                connectionFailed();		// 연결 실패시 불러오는 메소드
                Log.d(TAG, "Connect Fail");

                // socket을 닫는다.
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG,
                            "unable to close() socket during connection failure",
                            e2);
                }
                // 연결 대기상태인 메소드를 호출한다.
                BluetoothService.this.start();

                mHandler.sendEmptyMessage(STATE_CONNECT_FAILED);
                return;
            }

            // ConnectThread 클래스를 reset한다.
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // ConnectThread를 시작한다.
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // BluetoothSocket의 inputstream 과 outputstream을 얻는다.
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            mHandler.sendEmptyMessage(STATE_CONNECTED);
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
                    bytes = mmInStream.read(buffer);

                    if(bytes > 0) {
                        String strReadString = buffer.toString();

                        Log.d(TAG, strReadString);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                // 값을 쓰는 부분(값을 보낸다)
                mmOutStream.write(buffer);

            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}