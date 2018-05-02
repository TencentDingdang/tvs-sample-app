package com.tencent.ai.dobbydemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.ai.tvs.LoginProxy;
import com.tencent.ai.tvs.env.ELoginPlatform;
import com.tencent.ai.tvs.info.ProductManager;
import com.tencent.ai.tvsdevice.cb.ProductInfoListener;
import com.tencent.ai.tvsdevice.cb.SoftAPConnListener;
import com.tencent.ai.tvsdevice.comm.LinkManager;
import com.tencent.ai.tvsdevice.comm.NetworkConfigClient;
import com.tencent.ai.tvsdevice.info.NetworkConfigInfo;
import com.tencent.ai.tvsdevice.comm.ble.BleLinkManager;
import com.tencent.ai.tvsdevice.comm.softAp.SoftApLinkManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoftAPActivity extends AppCompatActivity {

    private static final int WIFI_PERMISSION_REQUEST_CODE = 101;

    private static final int MSG_CONFIG_NETWORK_FOR_REMOTE_DEVICE = 1;
    private static final int MSG_CONFIG_NETWORK_COMPLETED = 2;

    private static final int MSG_SEND_ING_START_TYPE = 1000;
    private static final int MSG_RECV_PLATFORM_TYPE = 1001;
    private static final int MSG_SEND_APINFO_TYPE = 1002;
    private static final int MSG_RECV_PRODUCTINFO_TYPE = 1003;
    private static final int MSG_SEND_CLIENTID_TYPE = 1004;
    private static final int MSG_RECV_ING_TYPE = 1005;
    private static final int MSG_SEND_ING_END_TYPE = 1006;

    private static final int SOFTAP_RESULT = 2000;
    private static final int SOFTAP_RESULT_SUCCESS = 2001;
    private static final int SOFTAP_RESULT_ERROR = 2002;

    private NetworkConfigClient mNetworkConfigClient;


    private ListView mWifiListView;
    private EditText mWifiSsidView, mWifiPskView;
    private SwipeRefreshLayout mRefreshView;
    private ProgressDialog mProgressDialog;

    private DeviceListAdapter mAdapter;

    private ArrayList<NetworkConfigInfo> mScanList = new ArrayList<NetworkConfigInfo>();
    private LinkManager mLinkManager;

    private NetworkConfigHandler mHandler;

    private ELoginPlatform platform;

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (mRefreshView.isRefreshing()) {
                mRefreshView.setRefreshing(false);
            }

            if (isExist(device.getAddress()))
                return;

            NetworkConfigInfo info = new NetworkConfigInfo();
            info.bluetoothDevice = device;
            mScanList.add(info);
            mAdapter.notifyDataSetChanged();
        }
    };

    private boolean isExist(String deviceAddress) {
        for (NetworkConfigInfo info : mScanList) {
            if (info.bluetoothDevice.getAddress().equals(deviceAddress))
                return true;
        }
        return false;
    }

    private NetworkConfigClient.NetworkConfigListener mNetworkConfigListener = new NetworkConfigClient.NetworkConfigListener() {
        @Override
        public void onNetworkConfigResult(int resultCode, HashMap<String, String> data) {
            mHandler.obtainMessage(MSG_CONFIG_NETWORK_COMPLETED, resultCode, 0, data).sendToTarget();
        }
    };

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position >= mScanList.size())
                return;

            if (TextUtils.isEmpty(mWifiSsidView.getText().toString()))
                Toast.makeText(SoftAPActivity.this, "请输入Wi-Fi名称", Toast.LENGTH_SHORT).show();

            mHandler.obtainMessage(MSG_CONFIG_NETWORK_FOR_REMOTE_DEVICE, mScanList.get(position)).sendToTarget();
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                if (state == WifiManager.WIFI_STATE_ENABLED) {
                    updateScanList();
                }
            }
            else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int btState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (btState == BluetoothAdapter.STATE_ON || btState == BluetoothAdapter.STATE_OFF) {
                    updateScanList();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.softap_main);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            int loginPlatform = bundle.getInt("loginplatform");
            if (ELoginPlatform.WX.ordinal() == loginPlatform) {
                platform = ELoginPlatform.WX;
            }
            else if (ELoginPlatform.QQOpen.ordinal() == loginPlatform) {
                platform = ELoginPlatform.QQOpen;
            }
        }

        mWifiListView = (ListView) findViewById(R.id.wifi_list);
        mWifiSsidView = (EditText) findViewById(R.id.ssid);
        mWifiPskView  = (EditText) findViewById(R.id.psk);
        mRefreshView = (SwipeRefreshLayout) findViewById(R.id.refresh_view);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateScanList();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("正在配网，请稍等...");

        mHandler = new NetworkConfigHandler();
        mNetworkConfigClient = new NetworkConfigClient();

        mWifiListView.setOnItemClickListener(mItemClickListener);
        mAdapter = new DeviceListAdapter(this);
        mWifiListView.setAdapter(mAdapter);

        // 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH
            }, WIFI_PERMISSION_REQUEST_CODE);
        } else {
            updateScanList();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void updateScanList() {
        mScanList.clear();
        mAdapter.notifyDataSetChanged();

        SoftApLinkManager apLinkManager = SoftApLinkManager.getInstance(this);
        if (!apLinkManager.isWifiEnabled()) {
            // 打开wifi
            apLinkManager.openWifi();
            return;
        }
        // 获取当前连接网络
        mWifiSsidView.setText(apLinkManager.getCurrentNetworkSSID());

        // 如果蓝牙打开，使用蓝牙配网，否则默认使用AP配网
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            final BleLinkManager bleLinkManager = BleLinkManager.getInstance(this);
            if (mLinkManager == null || !(mLinkManager instanceof BleLinkManager)) {
                mLinkManager = bleLinkManager;
            }

            if (!mRefreshView.isRefreshing()) {
                mRefreshView.setRefreshing(true);
            }
            bleLinkManager.stopLeScan();
            bleLinkManager.startLeScan(mLeScanCallback);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRefreshView.isRefreshing())
                        mRefreshView.setRefreshing(false);

                    bleLinkManager.stopLeScan();
                }
            }, 10000);
        } else {
            if (mLinkManager == null || !(mLinkManager instanceof SoftApLinkManager))
                mLinkManager = apLinkManager;

            updateWifiList();
        }
    }

    private void updateWifiList() {
        new AsyncTask<Void, Void, List<ScanResult>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!mRefreshView.isRefreshing())
                    mRefreshView.setRefreshing(true);
            }

            @Override
            protected List<ScanResult> doInBackground(Void... voids) {
                SoftApLinkManager link = (SoftApLinkManager) mLinkManager;
                link.startScan();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return link.getScanResultList();
            }

            @Override
            protected void onPostExecute(List<ScanResult> scanResults) {
                super.onPostExecute(scanResults);
                if (scanResults == null)
                    return;

                for (ScanResult scanRst : scanResults) {
                    NetworkConfigInfo info = new NetworkConfigInfo();
                    info.ssid = scanRst.SSID;
                    mScanList.add(info);
                }
                mAdapter.notifyDataSetChanged();

                if (mRefreshView.isRefreshing())
                    mRefreshView.setRefreshing(false);
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WIFI_PERMISSION_REQUEST_CODE) {
            updateScanList();
        }
    }

    private class NetworkConfigHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONFIG_NETWORK_FOR_REMOTE_DEVICE:
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                    NetworkConfigInfo info = (NetworkConfigInfo) msg.obj;
                    info.configSsid = mWifiSsidView.getText().toString();
                    info.configPsk = mWifiPskView.getText().toString();
                    mNetworkConfigClient.configNetworkForRemoteDevice(mLinkManager, info, mNetworkConfigListener, mSoftAPConnListener, mProductInfoListener);
                    break;
                case MSG_CONFIG_NETWORK_COMPLETED:
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (msg.arg1 == NetworkConfigClient.RESULT_CODE_SUCCESS) {
                        HashMap<String, String> data = (HashMap<String, String>) msg.obj;
                        String pid = data.get("PID");
                        String dsn = data.get("DSN");
                        Toast.makeText(SoftAPActivity.this, "Config AP Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SoftAPActivity.this, "Config AP Fail: " + msg.arg1, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MSG_SEND_ING_START_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_ING_START_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_ING_START_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MSG_RECV_PLATFORM_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_RECV_PLATFORM_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_RECV_PLATFORM_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MSG_SEND_APINFO_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_APINFO_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_APINFO_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MSG_RECV_PRODUCTINFO_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_RECV_PRODUCTINFO_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_RECV_PRODUCTINFO_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MSG_SEND_CLIENTID_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_CLIENTID_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_CLIENTID_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MSG_RECV_ING_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_RECV_ING_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_RECV_ING_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MSG_SEND_ING_END_TYPE:
                    if (msg.arg1 == SOFTAP_RESULT) {
                        if (msg.arg2 == SOFTAP_RESULT_SUCCESS) {
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_ING_END_TYPE SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        else if (msg.arg2 == SOFTAP_RESULT_ERROR){
                            Toast.makeText(SoftAPActivity.this, "MSG_SEND_ING_END_TYPE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }



    private ProductInfoListener mProductInfoListener = new ProductInfoListener() {
        @Override
        public String getProductInfo(String productId, String dsn) {
            ProductManager.getInstance().productId = productId;
            ProductManager.getInstance().dsn = dsn;
            String clientId = LoginProxy.getInstance("", "", SoftAPActivity.this).getClientId(platform);
            return clientId;
        }
    };



    private SoftAPConnListener mSoftAPConnListener = new SoftAPConnListener() {
        @Override
        public void onSuccess(int type) {
            switch (type) {
                case SoftAPConnListener.SEND_ING_START_TYPE:
                    mHandler.obtainMessage(MSG_SEND_ING_START_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
                case SoftAPConnListener.RECV_PLATFORM_TYPE:
                    mHandler.obtainMessage(MSG_RECV_PLATFORM_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
                case SoftAPConnListener.SEND_APINFO_TYPE:
                    mHandler.obtainMessage(MSG_SEND_APINFO_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
                case SoftAPConnListener.RECV_PRODUCTINFO_TYPE:
                    mHandler.obtainMessage(MSG_RECV_PRODUCTINFO_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
                case SoftAPConnListener.SEND_CLIENTID_TYPE:
                    mHandler.obtainMessage(MSG_SEND_CLIENTID_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
                case SoftAPConnListener.RECV_ING_TYPE:
                    mHandler.obtainMessage(MSG_RECV_ING_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
                case SoftAPConnListener.SEND_ING_END_TYPE:
                    mHandler.obtainMessage(MSG_SEND_ING_END_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_SUCCESS).sendToTarget();
                    break;
            }
        }

        @Override
        public void onError(int type) {
            switch (type) {
                case SoftAPConnListener.SEND_ING_START_TYPE:
                    mHandler.obtainMessage(MSG_SEND_ING_START_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
                case SoftAPConnListener.RECV_PLATFORM_TYPE:
                    mHandler.obtainMessage(MSG_RECV_PLATFORM_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
                case SoftAPConnListener.SEND_APINFO_TYPE:
                    mHandler.obtainMessage(MSG_SEND_APINFO_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
                case SoftAPConnListener.RECV_PRODUCTINFO_TYPE:
                    mHandler.obtainMessage(MSG_RECV_PRODUCTINFO_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
                case SoftAPConnListener.SEND_CLIENTID_TYPE:
                    mHandler.obtainMessage(MSG_SEND_CLIENTID_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
                case SoftAPConnListener.RECV_ING_TYPE:
                    mHandler.obtainMessage(MSG_RECV_ING_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
                case SoftAPConnListener.SEND_ING_END_TYPE:
                    mHandler.obtainMessage(MSG_SEND_ING_END_TYPE, SOFTAP_RESULT, SOFTAP_RESULT_ERROR).sendToTarget();
                    break;
            }
        }
    };


    private class DeviceListAdapter extends BaseAdapter {
        private Context mContext;

        public DeviceListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mScanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mScanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_expandable_list_item_1, parent, false);
            }

            NetworkConfigInfo info = mScanList.get(position);
            if (info.bluetoothDevice != null) {
                String text = info.bluetoothDevice.getName();
                if (TextUtils.isEmpty(text)) {
                    text = info.bluetoothDevice.getAddress();
                }
                ((TextView) convertView).setText(text);
            } else {
                ((TextView) convertView).setText(info.ssid);
            }
            return convertView;
        }
    }
}
