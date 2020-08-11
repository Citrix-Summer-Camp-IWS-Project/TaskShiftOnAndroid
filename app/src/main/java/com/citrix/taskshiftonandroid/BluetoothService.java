package com.citrix.taskshiftonandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.Intent;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    short rssi;
    private BluetoothSocket clientSocket;
    private BluetoothDevice deviceToPair;
    private BluetoothDevice pairedDevice;
    public OutputStream os;

    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;
    private UUID MY_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private static final int SELECT_DEVICE_REQUEST_CODE = 42;
    private BluetoothAdapter mBlueAdapter;
    private MainActivity activity;

    public BluetoothService (MainActivity activity){
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
    }
}
