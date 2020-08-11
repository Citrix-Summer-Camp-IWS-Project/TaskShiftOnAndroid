package com.citrix.taskshiftonandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    short rssi;
    private BluetoothSocket clientSocket;
    private BluetoothDevice deviceToPair;
    private BluetoothDevice pairedDevice;
    public OutputStream os;
    private AcceptThread ac;

    //指收到了多少条消息，从第二条开始就已经是ITem了
    private int numTexts;
    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;
    private UUID MY_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private static final int SELECT_DEVICE_REQUEST_CODE = 42;
    public BluetoothAdapter mBlueAdapter;
    private MainActivity mainActivity;

    public BluetoothService (MainActivity activity){
        mainActivity = activity;
        AndPermission.with(activity).runtime()
                .permission(Permission.ACCESS_COARSE_LOCATION , Permission.ACCESS_FINE_LOCATION)
                .onGranted(permissions -> {
                    System.out.println("Location permission got");
                })
                .onDenied(permissions -> {
                    System.out.println("Location permission needed");
                })
                .start();
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent =
                    new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1200);
            activity.startActivity(discoverableIntent);
        }
        ac = new AcceptThread();
        ac.start();
    }

    public void connectForPaired() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(discvoerReceiver, filter);
        if (mBlueAdapter.isDiscovering()) {
            mBlueAdapter.cancelDiscovery();
        }
        mBlueAdapter.startDiscovery();
    }
    private final BroadcastReceiver discvoerReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    pairedDevice = device;
                    rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    tryConnect(pairedDevice);
                }
            }
        }
    };
    public void tryConnect(BluetoothDevice device) {
        // 主动连接蓝牙
        try {
            // 判断是否在搜索,如果在搜索，就取消搜索
            if (mBlueAdapter.isDiscovering()) {
                mBlueAdapter.cancelDiscovery();
            }
            try {

                clientSocket = device
                        .createRfcommSocketToServiceRecord(MY_UUID);
                clientSocket.connect();
                os = clientSocket.getOutputStream();
            } catch (Exception e) {
                Toast.makeText(mainActivity.getApplicationContext()," " + device.getName() + "连接失败。", Toast.LENGTH_SHORT).show();
                tryConnect(device);
            }
            if (os != null) {
                String confirm = mBlueAdapter.getName() + "已与您连接。信号强度: " + Short.toString(rssi);
                os.write(confirm.getBytes("GBK"));
                Toast.makeText(mainActivity.getApplicationContext()," " + "已与" + device.getName() + "连接。信号强度: " + rssi, Toast.LENGTH_SHORT).show();
            } else {
                tryConnect(device);
            }
        } catch (Exception e) {

        }
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            numTexts++;
            if (numTexts == 1) {
                Toast.makeText(mainActivity.getApplicationContext(), String.valueOf(msg.obj),
                        Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            } else {

            }
        }
    };

    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        // 输入 输出流
        private OutputStream os;
        private InputStream is;

        public AcceptThread() {
            try {
                serverSocket = mBlueAdapter
                        .listenUsingRfcommWithServiceRecord("同事", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 截获客户端的蓝牙消息
            try {
                socket = serverSocket.accept(); // 如果阻塞了，就会一直停留在这里
                is = socket.getInputStream();
                os = socket.getOutputStream();
                while (true) {
                    synchronized (mainActivity) {
                        byte[] tt = new byte[is.available()];
                        if (tt.length > 0) {
                            is.read(tt, 0, tt.length);
                            Message msg = new Message();
                            msg.obj = new String(tt, "GBK");
                            handler.sendMessage(msg);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTS(String ts) throws IOException {
        if (os == null) {
            Toast.makeText(mainActivity.getApplicationContext(), "请先连接你的同事。", Toast.LENGTH_SHORT).show();
            return;
        }
        os.write(ts.getBytes("GBK"));
        return;
    }
}
