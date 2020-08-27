package com.citrix.taskshiftonandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.content.IntentSender;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // Hardcode tokens
    //public String username = "xeal3k@gmail.com";
    //public String token = "dK9YeYe38KuOfEDacc0wCC34";
    //public String AccountID = "5f033116b545e200154e76f4";


    private List<Item> Items = new ArrayList<>();
    private Account mAccount;
    public Account getmAccount() {
        return mAccount;
    }
    private RecyclerView rv;
    private adapter adapter;
    private ProgressBar loadingView;
    private Menu mMenu;
    private MenuItem personUI;
    private ImageView personImage;

    short rssi;
    private BluetoothSocket clientSocket;
    private BluetoothDevice deviceToPair;
    private BluetoothDevice pairedDevice;
    public OutputStream os;
    private AcceptThread ac;

    private int numTexts;
    //private static final int REQUEST_ENABLE_BT = 1;
    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;
    private UUID MY_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private static final int SELECT_DEVICE_REQUEST_CODE = 42;

    private BluetoothAdapter mBlueAdapter;
    DynamicReceiver dynamicReceiver = new DynamicReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleviewlisttest);
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            initializeBluetooth();
            ac = new AcceptThread(this);
            ac.start();
            connectForPaired();
        }
        initializeAdapter();
        RecyclerView rv = (RecyclerView) findViewById(R.id.tasklist);
        //define the width of divider
        int space = 2;
        rv.addItemDecoration(new SpacesItemDecoration(space));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
        }
    }

    private void initializeAdapter() {
        Intent intent = getIntent();
        ArrayList<Item> itemsString = (ArrayList<Item>) intent.getSerializableExtra("items");
        mAccount = (Account)intent.getSerializableExtra("account");
        for (Item item : itemsString) {
            Items.add(item);
        }
        adapter = new adapter(Items, this);
        rv = (RecyclerView) findViewById(R.id.tasklist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(discvoerReceiver);
        unregisterReceiver(dynamicReceiver);
    }

    //launch menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mMenu = menu;
        personUI = menu.findItem(R.id.menuitem);
        Context context = getApplicationContext();
        ConstraintLayout personLayout = (ConstraintLayout) personUI.getActionView();
        personImage = (ImageView) personLayout.findViewById(R.id.imageUI);
        personImage.setVisibility(View.GONE);

        if (BluetoothAdapter.getDefaultAdapter() != null) {
            connectForPaired();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_DEVICE_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            // User has chosen to pair with the Bluetooth device.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceToPair =
                        data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            }
            deviceToPair.createBond();

            // ... Continue interacting with the paired device.
            Context context = getApplicationContext();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            registerReceiver(dynamicReceiver,filter);
        }
    }


    class DynamicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int bonded = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
            if (bonded == BluetoothDevice.BOND_BONDED) {
                Toast.makeText(context,"Pairing successful. Trying to connect:" + deviceToPair.getName(), Toast.LENGTH_SHORT).show();
                tryConnect(deviceToPair);
            } else if (bonded == BluetoothDevice.BOND_NONE) {
                Toast.makeText(context,"Pairing Failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendTS(String ts) throws IOException {
        if (os == null) {
            Toast.makeText(getApplicationContext(), "Please make a connection first.", Toast.LENGTH_SHORT).show();
            return;
        }
        os.write(ts.getBytes("GBK"));
        return;
    }

    private void initializeBluetooth() {
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter == null) {
            Toast.makeText(getApplicationContext(), "bluetooth not found", Toast.LENGTH_LONG).show();
        } else if (mBlueAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent =
                    new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    public void connectForPaired() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discvoerReceiver, filter);
        if (mBlueAdapter.isDiscovering()) {
            mBlueAdapter.cancelDiscovery();
        }
        mBlueAdapter.startDiscovery();
    }
    private final BroadcastReceiver discvoerReceiver = new BroadcastReceiver() {
        private int pairedCount = 0;
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action) && pairedCount == 0) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    pairedDevice = device;
                    pairedCount ++;
                    rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    tryConnect(pairedDevice);
                }
            }
        }
    };
    private int connectedCount = 0;
    public void tryConnect(BluetoothDevice device) {
        try {
            if (mBlueAdapter.isDiscovering()) {
                mBlueAdapter.cancelDiscovery();
            }
            try {

                clientSocket = device
                        .createRfcommSocketToServiceRecord(MY_UUID);
                clientSocket.connect();
                os = clientSocket.getOutputStream();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext()," " + device.getName() + "Connection Failed.", Toast.LENGTH_SHORT).show();
                tryConnect(device);
            }
            if (os != null && connectedCount == 0) {
                Toast.makeText(getApplicationContext()," " + "Already connected to " + device.getName() + "with Rssi: " + rssi, Toast.LENGTH_SHORT).show();
                connectedCount ++;
                verifyIdentity(mAccount, device);
            }
        } catch (Exception e) {

        }
    }

    public static byte[] bytesMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        // read bytes from the input stream and store them in buffer
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
    public void verifyIdentity(Account mAccount, BluetoothDevice device) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {

        PrivateKey key = Account.getPriKey(mAccount, this);

        byte[] identity = mAccount.RSAEncrypt(mAccount.getUsername(), key);
        String confirm = mAccount.getUsername() + " has connected to you with Rssi: " + Short.toString(rssi);
        byte[] sendMsg = bytesMerger(identity, confirm.getBytes("UTF-8"));
        os.write(sendMsg);
    }
    // for the divider width
    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != parent.getChildCount()) {
                outRect.bottom = space;

                //detect the dynamic change in list
                System.out.println("this is number of items " + Items.size());
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        private int numTexts;
        private MyHandler(MainActivity activity) {
            this.mActivity = new WeakReference<MainActivity>(activity);
            this.numTexts = 0;
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                if (numTexts == 0) {
                    super.handleMessage(msg);
                    byte[] byteMsg = (byte[]) msg.obj;
                    try {
                        connectionVerification(byteMsg, activity);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
                        e.printStackTrace();
                    }
                    numTexts ++;
                } else {
                    String item = "";
                    try {
                        item = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Item added = Item.toItem(item);
                    added.emailAddress = activity.mAccount.getUsername();
                    activity.Items.add(added);
                    super.handleMessage(msg);
                    activity.adapter.notifyItemInserted(activity.Items.size() - 1);
                }
            }
        }

        public void connectionVerification(byte[] byteMsg, MainActivity activity) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
            String helMsg = "";
            byte[] identity = new byte[128];
            byte[] helloMsg = new byte[byteMsg.length - 128];
            System.arraycopy(byteMsg, 0, identity, 0, 128);
            System.arraycopy(byteMsg, 128, helloMsg, 0, byteMsg.length - 128);

            helMsg = new String(helloMsg, "UTF-8");
            Toast.makeText(activity.getApplicationContext(), helMsg, Toast.LENGTH_SHORT).show();

            PublicKey pubKey = Account.getPubKey(helMsg, activity);
            byte[] decryptedIdentity = Account.RSADecrypt(identity, pubKey);
            String finalMsg = new String(decryptedIdentity, "UTF-8");
            int emailPos = helMsg.indexOf(" ");
            String email = helMsg.substring(0, emailPos);
            if (email.equals(finalMsg)){
                Toast.makeText(activity.getApplicationContext(), "Identity verified", Toast.LENGTH_SHORT).show();
                Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), AllAccounts.getAccount(email).getImageID());
                activity.personImage.setImageDrawable(image);
                activity.personImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(activity.getApplicationContext(), "Identity unverified", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public BluetoothAdapter getmBlueAdapter() {
        return mBlueAdapter;
    }

    // 线程服务类
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        private OutputStream os;
        private InputStream is;
        private MainActivity mActivity;
        private MyHandler handler;
        public AcceptThread(MainActivity activity) {
            this.mActivity = activity;
            this.handler = new MyHandler(mActivity);
            try {
                serverSocket = mBlueAdapter
                        .listenUsingRfcommWithServiceRecord("同事", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();
                while (true) {
                    synchronized (MainActivity.this) {
                        byte[] tt = new byte[is.available()];
                        if (tt.length > 0) {
                            is.read(tt, 0, tt.length);
                            Message msg = new Message();
                            msg.obj = tt;

                            handler.sendMessage(msg);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ChangeIssueAssignee(String username, String token, String Issue, String AssigneeID) {
        //Use api to change issue assignee
        String credential = Credentials.basic(username, token);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n  \"accountId\": \"" +  AssigneeID + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
                .url("https://nj-summer-camp-2020.atlassian.net/rest/api/3/issue/" + Issue + "/assignee")
                .method("PUT", body)
                .addHeader("Authorization", credential)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "atlassian.xsrf.token=3b8b59a3-a91d-43ab-91e9-1f39c1f730a8_5b1c7d1bbfe800ba2d5af1baeed5078f6ccf7d4d_lin")
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 204){
                    System.out.println("Status Change succeeded");
                }
                else{
                    System.out.println("Assign failed" + response.code());
                }
            }
        });
    }

    public void ChangeIssueStatus(String username, String token, String issue, String status) throws JSONException {
        String credential = Credentials.basic(username, token);
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonO = new JSONObject();
        jsonO.put("id", status);
        jsonObject.put("transition", jsonO);
        RequestBody body = RequestBody.create(jsonObject.toString(),mediaType);
        Request request = new Request.Builder()
                .url("https://nj-summer-camp-2020.atlassian.net/rest/api/3/issue/" + issue +
                        "/transitions?expand=transitions.fields")
                .method("POST", body)
                .addHeader("Authorization", credential)
                .addHeader("Content-Type", "application/json")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Assign failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 204){
                    System.out.println("Status Change succeeded");
                }
                else{
                    System.out.println("Assign failed" + response.code());
                }
            }
        });
    }
}