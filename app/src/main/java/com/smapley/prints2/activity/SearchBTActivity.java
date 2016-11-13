package com.smapley.prints2.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Pos;
import com.smapley.prints2.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchBTActivity extends Activity implements OnClickListener, IOCallBack {

    private LinearLayout linearlayoutdevices;
    private ProgressBar progressBarSearchStatus;

    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;

    private Button btnSearch;
   SearchBTActivity mActivity;

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    BTPrinting mBt = new BTPrinting();

    private static String TAG = "SearchBTActivity";
    private SharedPreferences sp_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbt);
        sp_user = getSharedPreferences("user", MODE_PRIVATE);
        mActivity = this;
        progressBarSearchStatus = (ProgressBar) findViewById(R.id.progressBarSearchStatus);
        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);

        btnSearch = (Button) findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(this);
        btnSearch.setEnabled(true);


        initBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uninitBroadcast();
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.buttonSearch: {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null == adapter) {
                    finish();
                    break;
                }

                if (!adapter.isEnabled()) {
                    if (adapter.enable()) {
                        while (!adapter.isEnabled())
                            ;
                        Log.v(TAG, "Enable BluetoothAdapter");
                    } else {
                        finish();
                        break;
                    }
                }

                adapter.cancelDiscovery();
                linearlayoutdevices.removeAllViews();
                adapter.startDiscovery();
                break;
            }
        }
    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
                    final String address = device.getAddress();
                    String name = device.getName();
                    if (name == null)
                        name = "BT";
                    else if (name.equals(address))
                        name = "BT";
                    Button button = new Button(context);
                    button.setText(name + ": " + address);

                    for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                    {
                        Button btn = (Button)linearlayoutdevices.getChildAt(i);
                        if(btn.getText().equals(button.getText()))
                        {
                            return;
                        }
                    }
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    button.setOnClickListener(new OnClickListener() {

                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            // 只有没有连接且没有在用，这个才能改变状态
                            Toast.makeText(mActivity, "正在链接....", Toast.LENGTH_SHORT).show();
                            btnSearch.setEnabled(false);
                            linearlayoutdevices.setEnabled(false);
                            for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                            {
                                Button btn = (Button)linearlayoutdevices.getChildAt(i);
                                btn.setEnabled(false);
                            }

                            SharedPreferences.Editor editor = sp_user.edit();
                            editor.putString("address", address);
                            editor.commit();

                            es.submit(new TaskOpen(mBt,address, mActivity));
                        }
                    });
                    button.getBackground().setAlpha(100);
                    linearlayoutdevices.addView(button);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(false);
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public class TaskOpen implements Runnable
    {
        BTPrinting bt = null;
        String address = null;
        Context context = null;

        public TaskOpen(BTPrinting bt, String address, Context context)
        {
            this.bt = bt;
            this.address = address;
            this.context = context;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            bt.Open(address,context);
        }
    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                btnSearch.setEnabled(false);
                linearlayoutdevices.setEnabled(false);
                for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                {
                    Button btn = (Button)linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(false);
                }
                Toast.makeText(mActivity, "链接成功!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                btnSearch.setEnabled(true);
                linearlayoutdevices.setEnabled(true);
                for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                {
                    Button btn = (Button)linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
                Toast.makeText(mActivity, "链接失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                btnSearch.setEnabled(true);
                linearlayoutdevices.setEnabled(true);
                for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                {
                    Button btn = (Button)linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
            }
        });
    }




}
