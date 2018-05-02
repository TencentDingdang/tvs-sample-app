package com.tencent.ai.dobbydemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.ai.tvs.LoginProxy;
import com.tencent.ai.tvs.env.ELoginPlatform;
import com.tencent.ai.tvs.info.ProductManager;
import com.tencent.ai.tvsdevice.info.APListInfo;
import com.tencent.ai.tvsdevice.smartlinkcomm.DevConfig;
import com.tencent.ai.tvsdevice.smartlinkcomm.DevProtocol;
import com.tencent.ai.tvsdevice.cb.DevReqListener;
import com.tencent.ai.tvsdevice.DeviceApplication;
import com.tencent.ai.tvsdevice.cb.DlnaQueryListener;
import com.tencent.ai.tvsdevice.util.StringHexUtil;
import com.tencent.ai.tvsdevice.util.WifiConnUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.teleal.cling.model.meta.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SmartLinkActivity extends AppCompatActivity {

    private Button toWifiSettingBtn;

    private Button getStatusBtn;
    private ProgressBar getStatusProgress;
    private TextView getStatusText;

    private LinearLayoutManager mAPListLayoutManager, mDevListLayoutManager;

    private Button wlanGetApListBtn;
    private ProgressBar wlanGetApListProgress;
    private RecyclerView wlanGetApListRecyclerView;
    private APListRecyclerAdapter mAPListAdapter;

    private Button scanDeviceBtn, stopScanDeviceBtn;
    private ProgressBar scanDeviceProgress;
    private RecyclerView scanDeviceRecyclerView;
    private DevListRecyclerAdapter mDevListAdapter;

    private Button logoutUnbindBtn;
    private Button factoryResetBtn;
    private String realIP;

    private DevProtocol devProtocol;

    private APListInfo apListInfo;
    private List<APListInfo.AplistBean> aplistBeanList;

    private ScanDeviceReceiver mScanDeviceReceiver;

    private ELoginPlatform platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvs_main);

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

        devProtocol = DevProtocol.getInstance();
        devProtocol.setOwnActivity(this);
        devProtocol.setDevReqListener(new APListener());

        toWifiSettingBtn = (Button) findViewById(R.id.toWifiSettingBtn);

        getStatusBtn = (Button) findViewById(R.id.getStatusBtn);
        getStatusProgress = (ProgressBar) findViewById(R.id.getStatusProgress);
        getStatusText = (TextView) findViewById(R.id.getStatusText);

        wlanGetApListBtn = (Button) findViewById(R.id.wlanGetApListBtn);
        wlanGetApListProgress = (ProgressBar) findViewById(R.id.wlanGetApListProgress);
        wlanGetApListRecyclerView = (RecyclerView) findViewById(R.id.wlanGetApListRecyclerView);

        scanDeviceBtn = (Button) findViewById(R.id.scanDeviceBtn);
        stopScanDeviceBtn = (Button) findViewById(R.id.stopScanDeviceBtn);
        scanDeviceProgress = (ProgressBar) findViewById(R.id.scanDeviceProgress);
        scanDeviceRecyclerView = (RecyclerView) findViewById(R.id.scanDeviceRecyclerView);

        mAPListLayoutManager = new LinearLayoutManager(this);
        mDevListLayoutManager = new LinearLayoutManager(this);

        wlanGetApListRecyclerView.setLayoutManager(mAPListLayoutManager);
        wlanGetApListRecyclerView.setHasFixedSize(true);

        scanDeviceRecyclerView.setLayoutManager(mDevListLayoutManager);
        scanDeviceRecyclerView.setHasFixedSize(true);
        mDevListAdapter = new DevListRecyclerAdapter();
        scanDeviceRecyclerView.setAdapter(mDevListAdapter);

        logoutUnbindBtn = (Button) findViewById(R.id.logoutUnbindBtn);
        factoryResetBtn = (Button) findViewById(R.id.factoryResetBtn);

        toWifiSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });

        getStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatusProgress.setVisibility(View.VISIBLE);
                devProtocol.getStatus(DevConfig.DEV_EX_IP, true);
            }
        });

        wlanGetApListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wlanGetApListProgress.setVisibility(View.VISIBLE);
                devProtocol.wlanGetApList(DevConfig.DEV_EX_IP,true);
            }
        });

        scanDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDeviceProgress.setVisibility(View.VISIBLE);
                if (mScanDeviceReceiver == null) {
                    mScanDeviceReceiver = new ScanDeviceReceiver();
                }
                registerIF(mScanDeviceReceiver);
                ((DeviceApplication) getApplication()).startSearchDevices();
            }
        });

        stopScanDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDeviceProgress.setVisibility(View.GONE);
                ((DeviceApplication) getApplication()).stopSearchDevices();
                unregisterReceiver(mScanDeviceReceiver);
            }
        });

        if (mScanDeviceReceiver == null) {
            mScanDeviceReceiver = new ScanDeviceReceiver();
        }
        registerIF(mScanDeviceReceiver);

        logoutUnbindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realIP != null) {
                    devProtocol.alexaLogOut(realIP);
                }
            }
        });

        factoryResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realIP != null) {
                    devProtocol.restoreToDefault(realIP);
                }
            }
        });
    }

    private void registerIF(BroadcastReceiver reciver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DevConfig.REFRESH_UI_DEVICELIST);
        filter.addAction(DevConfig.REFRESH_UI_TIMEOUT);
        registerReceiver(reciver, filter);
    }

    private String getIP(String res) {
        if (!TextUtils.isEmpty(res)) {
            Matcher m = Pattern.compile("((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}))").matcher(res);
            while (m.find()) {
                return m.group(1);
            }
        }
        return null;
    }

    private class ScanDeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendMessage(handler.obtainMessage(0, intent.getAction()));
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Object obj = msg.obj;
            if (obj == null) {
                return;
            }
            if (obj.equals(DevConfig.REFRESH_UI_DEVICELIST)) {

                if (DeviceApplication.uuidList.size() <= 0) {
                    mDevListAdapter.setDeviceList(null);
                } else {
                    final List<Device> deviceList = new ArrayList<>();
                    for (int i = 0; i < DeviceApplication.uuidList.size(); i++) {
                        Device device = DeviceApplication.devicesMap.get(DeviceApplication.uuidList.get(i));
                        if (device != null)
                            deviceList.add(device);
                    }
                    mDevListAdapter.setDeviceList(deviceList);
                    mDevListAdapter.setOnItemClickListener(new DevListRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Device selectedDevice = deviceList.get(position);
                            String identity = selectedDevice.getIdentity().toString();
                            ((DeviceApplication) getApplication()).getDeviceLoginState(selectedDevice, "ALEXA", SmartLinkActivity.this, new DlnaQueryListener() {
                                @Override
                                public void onSuccess(String msg) {
                                    Toast.makeText(SmartLinkActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(String msg) {
                                    Toast.makeText(SmartLinkActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                            String ip = getIP(identity);
                            devProtocol.getAlexaProfile(ip);
                        }
                    });
                }
            }
            else if (obj.equals(DevConfig.REFRESH_UI_TIMEOUT)) {
                if (DeviceApplication.uuidList.size() <= 0) {
                    mDevListAdapter.setDeviceList(null);
                } else {
                    final List<Device> deviceList = new ArrayList<>();
                    for (int i = 0; i < DeviceApplication.uuidList.size(); i++) {
                        Device device = DeviceApplication.devicesMap.get(DeviceApplication.uuidList.get(i));
                        if (device != null)
                            deviceList.add(device);
                    }
                    mDevListAdapter.setDeviceList(deviceList);
                    mDevListAdapter.setOnItemClickListener(new DevListRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String identity = deviceList.get(position).getIdentity().toString();
                            String ip = getIP(identity);
                            devProtocol.getAlexaProfile(ip);
                        }
                    });
                }

            }
        };
    };

    private class APListener implements DevReqListener {

        String uuid = "";

        @Override
        public void onSuccess(int type, Object msg) {
            switch (type) {
                case DevReqListener.GETSTATUS_TYPE:
                    getStatusProgress.setVisibility(View.GONE);
                    getStatusText.setText((String)msg);
                    uuid = (String)msg;
                    ProductManager.getInstance().dsn = uuid;
                    if (realIP != null) {
                        LoginProxy proxy = LoginProxy.getInstance("", "", getApplicationContext());
                        String clientId = proxy.getClientId(platform);
                        devProtocol.setTVSAccessToken(realIP, clientId);
                    }
                    break;
                case DevReqListener.WLANGETAPLIST_TYPE:
                    wlanGetApListProgress.setVisibility(View.GONE);
                    apListInfo = (APListInfo)msg;
                    aplistBeanList = apListInfo.getAplist();
                    mAPListAdapter = new APListRecyclerAdapter(apListInfo);
                    wlanGetApListRecyclerView.setAdapter(mAPListAdapter);
                    mAPListAdapter.setOnItemClickListener(new APListRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            APListInfo.AplistBean ap = aplistBeanList.get(position);
                            final String ssid = ap.getSsid();
                            final String channel = ap.getChannel();
                            final String auth = ap.getAuth();
                            final String encry = ap.getEncry();
                            if ("NONE".equals(encry)) {
                                devProtocol.wlanConnectAp(DevConfig.DEV_EX_IP,true, ssid, channel, auth, encry, encry, "1");
                                WifiConnUtil.getInstance(SmartLinkActivity.this).connect(StringHexUtil.hexStr2Str(ssid), "", WifiConnUtil.WIFICIPHER_NOPASS);
                            }
                            else if ("AES".equals(encry)) {
                                final EditText et = new EditText(SmartLinkActivity.this);
                                new AlertDialog.Builder(SmartLinkActivity.this).setTitle("输入密码")
                                        .setView(et)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String input = et.getText().toString();
                                                devProtocol.wlanConnectAp(DevConfig.DEV_EX_IP,true, ssid, channel, auth, encry, StringHexUtil.str2HexStr(input), "1");
                                                WifiConnUtil.getInstance(SmartLinkActivity.this).connect(StringHexUtil.hexStr2Str(ssid), input, WifiConnUtil.WIFICIPHER_WPA);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }

                        }
                    });
                    break;
                case DevReqListener.WLANCONNECTAP_TYPE:
                    if ("OK".equals((String) msg)) {
                    }
                    break;
                case DevReqListener.WLANGETCONNECTSTATE_TYPE:
                    Toast.makeText(SmartLinkActivity.this, (String)msg, Toast.LENGTH_LONG).show();
                    break;
                case DevReqListener.GETALEXAPROFILE_TYPE:
                    try {
                        JSONObject obj = new JSONObject((String)msg);
                        String productId = obj.getString("productID");
                        String ip = obj.getString("ip");
                        realIP = ip;
                        ProductManager.getInstance().productId = productId;
                        devProtocol.getStatus(ip, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case DevReqListener.SETTVSTOKEN_TYPE:
                    break;
            }
        }

        @Override
        public void onError(int type, Object msg) {
            switch (type) {
                case DevReqListener.GETSTATUS_TYPE:
                    getStatusProgress.setVisibility(View.GONE);
                    getStatusText.setText((String)msg);
                    break;
                case DevReqListener.WLANGETAPLIST_TYPE:
                    wlanGetApListProgress.setVisibility(View.GONE);
                    break;
                case WLANCONNECTAP_TYPE:
                    break;
                case DevReqListener.WLANGETCONNECTSTATE_TYPE:
                    break;
                case DevReqListener.GETALEXAPROFILE_TYPE:
                    break;
                case DevReqListener.SETTVSTOKEN_TYPE:
                    break;
            }
        }
    };
}
