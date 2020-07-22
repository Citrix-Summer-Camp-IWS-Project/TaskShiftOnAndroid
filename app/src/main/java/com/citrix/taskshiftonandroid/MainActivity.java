package com.citrix.taskshiftonandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // Hardcode tokens
    public String username = "xeal3k@gmail.com";
    public String token = "dK9YeYe38KuOfEDacc0wCC34";
    public String AccountID = "5f033116b545e200154e76f4";
//    public String username = "carlostian927@berkeley.edu";
//    public String token = "DwNBtNVKteYVQd7MjNHF0250";
//    public String AccountID = "5f03322ad6803200212f2dc0";



    private List<Item> Items;
    private RecyclerView rv;
    private adapter adapter;

    short rssi;
    //客户端服务端一体
    private BluetoothSocket clientSocket;
    private BluetoothDevice deviceToPair;
    private BluetoothDevice pairedDevice;
    public OutputStream os;
    private AcceptThread ac;

    //指收到了多少条消息，从第二条开始就已经是ITem了
    private int numTexts;
    //private static final int REQUEST_ENABLE_BT = 1;
    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;
    private UUID MY_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private static final int SELECT_DEVICE_REQUEST_CODE = 42;

    private BluetoothAdapter mBlueAdapter;
    DynamicReceiver dynamicReceiver = new DynamicReceiver();
    private static MainActivity mainActivity;
    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    public MainActivity() {
        mainActivity = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleviewlisttest);
        initializeBluetooth();
        ac = new AcceptThread();
        ac.start();
        connectForPaired();

        try {
            List<Dictionary> info = GetAllIssueInfo(username, token);
            System.out.println("this is start2");
            System.out.println("this is info" + info);
            /*
            for(int i = 0; i < info.size(); i++){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(info.get(i));
                System.out.println("this is " + json );
            }

             */
        } catch (JSONException e) {
            System.out.println("this is error");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("this is error");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("this is error");
            e.printStackTrace();
        }



        RecyclerView rv = (RecyclerView) findViewById(R.id.tasklist);
        //data

        //initiate recycle view
        //define the width of divider
        int space = 2;
        rv.addItemDecoration(new SpacesItemDecoration(space));

        //final adapter adapter = new adapter(Items);
        //这里我们选择创建一个LinearLayoutManager
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //为RecyclerView对象指定我们创建得到的layoutManager
        //rv.setLayoutManager(layoutManager);
        //rv.setAdapter(adapter);
//
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(rv);

        //test
        //ItemTouchHelper data = touchHelper;

//        myItemAnimator = new Animation();
//        myItemAnimator.setRemoveDuration(2000);
//        myRecyclerVIew.setItemAnimator(myItemAnimator);

    }
    private void initializeAdapter(){
        adapter = new adapter(Items);

        RecyclerView rv = (RecyclerView) findViewById(R.id.tasklist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //为RecyclerView对象指定我们创建得到的layoutManager
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
        return super.onCreateOptionsMenu(menu);
    }
    //menu button test
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //根据不同的id点击不同按钮控制activity需要做的事件
        switch (item.getItemId())
        {
            case R.id.action_add:

                deviceManager = getSystemService(CompanionDeviceManager.class);
                deviceFilter = new BluetoothDeviceFilter.Builder().build();
                pairingRequest = new AssociationRequest.Builder()
                        .addDeviceFilter(deviceFilter)
                        .setSingleDevice(false)
                        .build();
                deviceManager.associate(pairingRequest,
                        new CompanionDeviceManager.Callback() {
                            @Override
                            public void onDeviceFound(IntentSender chooserLauncher) {
                                try {
                                    startIntentSenderForResult(chooserLauncher,
                                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(CharSequence charSequence) {

                            }
                        },
                        null);
                break;
            case R.id.action_remove:
                //事件
                Items.remove(0);
                adapter.notifyItemRemoved(0);
                System.out.println("this is bug???");

                break;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_DEVICE_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            // User has chosen to pair with the Bluetooth device.
            deviceToPair =
                    data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            deviceToPair.createBond();

            // ... Continue interacting with the paired device.
            Context context = getApplicationContext();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            //注册广播接收
            registerReceiver(dynamicReceiver,filter);
        }
    }


    class DynamicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //通过土司验证接收到广播
            int bonded = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
            if (bonded == BluetoothDevice.BOND_BONDED) {
                Toast.makeText(context,"配对成功,正在连接: " + deviceToPair.getName(), Toast.LENGTH_SHORT).show();
                tryConnect(deviceToPair);
            } else if (bonded == BluetoothDevice.BOND_NONE) {
                Toast.makeText(context,"配对失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendTS(String ts) throws IOException {
        if (os == null) {
            Toast.makeText(getApplicationContext(), "请先连接你的同事。", Toast.LENGTH_SHORT).show();
            return;
        }
        os.write(ts.getBytes("GBK"));
        return;
    }

    private void initializeBluetooth() {
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter == null) {
            Toast.makeText(getApplicationContext(), "设备不支持蓝牙", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext()," " + device.getName() + "连接失败。", Toast.LENGTH_SHORT).show();
            }
            if (os != null) {
                String confirm = mBlueAdapter.getName() + "已与您连接。信号强度: " + Short.toString(rssi);
                os.write(confirm.getBytes("GBK"));
                Toast.makeText(getApplicationContext()," " + "已与" + device.getName() + "连接。信号强度: " + rssi, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
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

            if (parent.getChildPosition(view) != parent.getChildCount() ) {
                outRect.bottom = space;

                //detect the dynamic change in list
                System.out.println("this is number of items " + Items.size());
            }
        }
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            numTexts++;
            if (numTexts == 1) {
                Toast.makeText(getApplicationContext(), String.valueOf(msg.obj),
                        Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            } else {
                Item added = Item.toItem(String.valueOf(msg.obj));
                Items.add(added);
                super.handleMessage(msg);
                adapter.notifyItemInserted(Items.size() - 1);
            }
        }
    };
    // 线程服务类
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
                    synchronized (MainActivity.this) {
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

    public static List<String> ProjectKey(String username, String token) throws IOException, JSONException {
        /*
        Use API to all the project in json format
         */
        List<String> list = new ArrayList<String>();
        String credential = Credentials.basic(username, token);
        final CountDownLatch latch = new CountDownLatch(1);
        Request request = new Request.Builder()
                .url("https://nj-summer-camp-2020.atlassian.net/rest/api/3/project")
                .method("GET", null)
                .addHeader("Authorization", credential)
                .addHeader("Cookie", "atlassian.xsrf.token=3b8b59a3-a91d-43ab-91e9-1f39c1f730a8_5b1c7d1bbfe800ba2d5af1baeed5078f6ccf7d4d_lin")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();

        /*
        Return a list with all the projects
         */

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                latch.countDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArr = null;
                try {
                    jsonArr = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        String Pkey = (String) jsonObj.get("key");
                        list.add(Pkey);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });

        try{
            latch.await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<Dictionary> GetAllIssueInfo(String username, String token) throws IOException, JSONException, InterruptedException {
        List<Dictionary> infoList = new ArrayList<Dictionary>();
        List<String> projectKey = ProjectKey(username, token);
        List<String> issueList = new ArrayList<String>();
        List<String> summaryList = new ArrayList<String>();
        List<String> assigneeList = new ArrayList<String>();
        List<String> statusList = new ArrayList<String>();
        List<String> typeList = new ArrayList<String>();

        final CountDownLatch iLatch = new CountDownLatch(issueList.size());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        final CountDownLatch pLatch = new CountDownLatch(projectKey.size());
        for(int i = 0; i < projectKey.size(); i++) {
            final CountDownLatch latch = new CountDownLatch(1);
            String key = projectKey.get(i);
            String credential = Credentials.basic(username, token);
            Request request = new Request.Builder()
                    .url("https://nj-summer-camp-2020.atlassian.net/rest/api/3/search?jql=project=" + key)
                    .method("GET", null)
                    .addHeader("Authorization", credential)
                    .addHeader("Cookie", "atlassian.xsrf.token=3b8b59a3-a91d-43ab-91e9-1f39c1f730a8_5b1c7d1bbfe800ba2d5af1baeed5078f6ccf7d4d_lin")
                    .build();

            client.newCall(request).enqueue(new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    latch.countDown();
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String emailAddress = "";
                    try {
                        JSONObject JsonObj = new JSONObject(response.body().string());
                        JSONArray jsonArr = (JSONArray) JsonObj.get("issues");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            JSONObject fields = (JSONObject) jsonObj.get("fields");

                            String tKey = (String) jsonObj.get("key");
                            //System.out.println("this is " + tKey);
                            issueList.add(tKey);

                            String summary = (String) jsonObj.get("key");
                            summaryList.add(summary);

                            JSONObject issueType = (JSONObject) fields.get("issuetype");
                            String name = (String) issueType.get("name");
                            typeList.add(name);

                            JSONObject status = (JSONObject) fields.get("status");
                            String statusName = (String) status.get("name");
                            statusList.add(statusName);

                            try{
                                JSONObject assignee = (JSONObject) fields.get("assignee");
                                emailAddress = (String) assignee.get("emailAddress");
                            } catch ( Exception e){
                                emailAddress = "No assignee";
                            }
                            assigneeList.add(emailAddress);
                        }
                    } catch (JSONException e) {
                        System.out.println("this is error");
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            });
            latch.await();
            pLatch.countDown();
        }
        pLatch.await();
        System.out.println("this is " + (issueList.size() == statusList.size()));

        for(int j = 0; j < issueList.size(); j++){
            if(assigneeList.get(j).equals(username)){
                if( ! statusList.get(j).equals("Done")){
                    Dictionary Info = new Hashtable();
                    Info.put("Type", typeList.get(j));
                    Info.put("Status", statusList.get(j));
                    Info.put("Summary", summaryList.get(j));
                    Info.put("Assignee", assigneeList.get(j));
                    Info.put("Task", issueList.get(j));
                    infoList.add(Info);
                }
            }
        }
        Items = Item.initializeFromDictionary(infoList);
        initializeAdapter();
        return infoList;
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

                }
                else{
                }
            }
        });
    }
}