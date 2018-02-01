package com.tencent.ai.dobby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.ai.tvs.AuthorizeListener;
import com.tencent.ai.tvs.BindingListener;
import com.tencent.ai.tvs.ConstantValues;
import com.tencent.ai.tvs.LoginProxy;
import com.tencent.ai.tvs.env.ELocationType;
import com.tencent.ai.tvs.env.ELoginEnv;
import com.tencent.ai.tvs.env.ELoginPlatform;
import com.tencent.ai.tvs.env.EUserAttrType;
import com.tencent.ai.tvs.info.CPMemberManager;
import com.tencent.ai.tvs.info.DeviceCPMemberManager;
import com.tencent.ai.tvs.info.DeviceManager;
import com.tencent.ai.tvs.info.LocManager;
import com.tencent.ai.tvs.info.LocationInfo;
import com.tencent.ai.tvs.info.ProductManager;
import com.tencent.ai.tvs.info.PushInfoManager;
import com.tencent.ai.tvs.info.QQInfoManager;
import com.tencent.ai.tvs.info.QQOpenInfoManager;
import com.tencent.ai.tvs.info.UserInfoManager;
import com.tencent.ai.tvs.info.WxInfoManager;
import com.tencent.ai.tvs.ui.UserCenterStateListener;
import com.tencent.connect.common.Constants;

import SmartService.EAIPushIdType;
import oicq.wlogin_sdk.request.WtloginHelper;

public class MainActivity extends AppCompatActivity implements AuthorizeListener, BindingListener {


    private static final String TEST_APPID_WX = "wxd077c3460b51e427";
    private static final String TEST_APPID_QQOPEN = "1105886239";
    private static final long TEST_APPID_QQ = 1600001268L;

    private static final String TEST_PRODUCTID = "7e8ab486-c6f6-4ecc-b52e-7ea8da82c9da:9cb1fbf4c54442cc80c9aed8cb3c25b6";
    private static final String TEST_DSN = "FF31F085A55DD019C8575CFB";

    private static final String TEST_DEVOEM = "GGMM";
    private static final String TEST_DEVTYPE = "SPEAKER";

    private LoginProxy proxy;

    // 内部使用
    private LoginProxy innerProxy;

    private WxInfoManager wxInfoManager;
    private QQOpenInfoManager qqOpenInfoManager;
    private QQInfoManager qqInfoManager;

    private RadioGroup netEnvRG;
    private RadioButton testEnvRB, formalEnvRB;

    private Button wxLoginBtn, wxLogoutBtn;
    private Button qqOpenLoginBtn, qqOpenLogoutBtn;
    private Button qqLoginBtn, qqLogoutBtn;
    private Button toUserCenterBtn, toUserCenterWithCallbackBtn, toGetClientIdBtn;

    private EditText getCaptchaEditText;
    private Button getCaptchaButton;
    private TextView getCaptchaTextView;

    private EditText bindPhoneNumberEditText;
    private Button bindPhoneNumberButton;
    private TextView bindPhoneNumberTextView;

    private Button bindLocationButton, queryLocationButton;
    private TextView bindLocationTextView;

    private LinearLayout queryLocationLayout;
    private TextView queryLocationTextView;

    private Button devicebindButton;
    private TextView devicebindTextView;

    private Button deviceunbindButton;
    private TextView deviceunbindTextView;

    private Button getBindDevicesButton;
    private TextView getBindDevicesTextView;

    private Button getBindAccountButton;
    private TextView getBindAccountTextView;

    private Button getMemberStatusButton;
    private TextView getMemberStatusTextView;

    private Button getDeviceStatusButton;
    private TextView getDeviceStatusTextView;

    private Button toSmartLinkButton, toSoftAPButton, toQRLoginButton;

    private LinearLayout wxTokenLayout, qqopenTokenLayout, qqTokenLayout;
    private TextView wxATTextView, wxRTTextView, qqopenATTextView, qqATTextView;

    private DeviceManager deviceManager;

    private static ELoginPlatform TEST_PLATFORM = ELoginPlatform.WX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        registerProxy();

        requestProxyOp();

        showTokenInfo();

        netEnvRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (testEnvRB.getId() == checkedId) {
                    proxy.setLoginEnv(ELoginEnv.TEST);
                    proxy.setUserCenterEnv(ELoginEnv.TEST);
                }
                else if (formalEnvRB.getId() == checkedId) {
                    proxy.setLoginEnv(ELoginEnv.FORMAL);
                    proxy.setUserCenterEnv(ELoginEnv.FORMAL);
                }
            }
        });

        wxLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!proxy.isWXAppInstalled()) {
                    Toast.makeText(MainActivity.this, "WX Not Installed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!proxy.isWXAppSupportAPI()) {
                    Toast.makeText(MainActivity.this, "WX Not SupportAPI", Toast.LENGTH_SHORT).show();
                    return;
                }
                TEST_PLATFORM = ELoginPlatform.WX;
                proxy.requestLogin(ELoginPlatform.WX, TEST_PRODUCTID, TEST_DSN, MainActivity.this);
            }
        });

        qqOpenLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEST_PLATFORM = ELoginPlatform.QQOpen;
                proxy.requestLogin(ELoginPlatform.QQOpen, TEST_PRODUCTID, TEST_DSN, MainActivity.this);
            }
        });

        qqLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innerProxy.requestLogin(ELoginPlatform.QQ, TEST_PRODUCTID, TEST_DSN, MainActivity.this);
            }
        });

        wxLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.clearToken(ELoginPlatform.WX, MainActivity.this);
            }
        });

        qqOpenLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.clearToken(ELoginPlatform.QQOpen, MainActivity.this);
            }
        });

        qqLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innerProxy.clearToken(ELoginPlatform.QQ, MainActivity.this);
            }
        });

        toGetClientIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("ClientId", proxy.getClientId(TEST_PLATFORM));
            }
        });

        toUserCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceManager = new DeviceManager();
                deviceManager.productId = TEST_PRODUCTID;
                deviceManager.dsn = TEST_DSN;
                deviceManager.deviceOEM = TEST_DEVOEM;
                deviceManager.deviceType = TEST_DEVTYPE;
                proxy.toUserCenter(EUserAttrType.HOMEPAGE, deviceManager);
            }
        });

        toUserCenterWithCallbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceManager = new DeviceManager();
                deviceManager.productId = TEST_PRODUCTID;
                deviceManager.dsn = TEST_DSN;
                deviceManager.deviceOEM = TEST_DEVOEM;
                deviceManager.deviceType = TEST_DEVTYPE;
                proxy.toUserCenter(EUserAttrType.HOMEPAGE, deviceManager, new UserCenterStateListener() {
                    @Override
                    public void onSuccess(ELoginPlatform platform, int type) {
                        switch (type) {
                            case UserCenterStateListener.LOGIN_TYPE:
                                break;
                            case UserCenterStateListener.LOGOUT_TYPE:
                                break;
                        }
                    }

                    @Override
                    public void onError(int type) {
                        switch (type) {
                            case UserCenterStateListener.LOGIN_TYPE:
                                break;
                        }
                    }

                    @Override
                    public void onCancel(int type) {

                    }
                });
            }
        });

        getCaptchaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.requestGetCaptcha(TEST_PLATFORM, getCaptchaEditText.getText().toString());
            }
        });

        bindPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.requestBindPhoneNumber(TEST_PLATFORM, getCaptchaEditText.getText().toString(), bindPhoneNumberEditText.getText().toString());
            }
        });

        bindLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocManager locManager = LocManager.getInstance();
                LocationInfo homeLocationInfo = locManager.getLocation(ELocationType.HOME);
                homeLocationInfo.addr = "h_addr";
                homeLocationInfo.name = "h_name";
                homeLocationInfo.longitube = "h_longitube";
                homeLocationInfo.latitube = "h_latitube";
                homeLocationInfo.cabAddr = "h_caddr";
                homeLocationInfo.cabName = "h_cname";
                homeLocationInfo.cabLongitube = "h_clongitube";
                homeLocationInfo.cabLatitube = "h_clatitube";
                LocationInfo companyLocationInfo = locManager.getLocation(ELocationType.COMPANY);
                companyLocationInfo.addr = "c_addr";
                companyLocationInfo.name = "c_name";
                companyLocationInfo.longitube = "c_longitube";
                companyLocationInfo.latitube = "c_latitube";
                companyLocationInfo.cabAddr = "c_caddr";
                companyLocationInfo.cabName = "c_cname";
                companyLocationInfo.cabLongitube = "c_clongitube";
                companyLocationInfo.cabLatitube = "c_clatitube";
                if (proxy.isLocationOpValid(TEST_PLATFORM)) {
                    proxy.requestBindLocation(TEST_PLATFORM, homeLocationInfo, companyLocationInfo);
                }
            }
        });

        queryLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proxy.isLocationOpValid(TEST_PLATFORM)) {
                    proxy.requestQueryLocation(TEST_PLATFORM);
                }
            }
        });

        devicebindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushInfoManager pushManager = PushInfoManager.getInstance();
                DeviceManager devManager = new DeviceManager();
                pushManager.idType = EAIPushIdType._ETVSSpeakerIdentifier;
                pushManager.idExtra = ConstantValues.PUSHMGR_IDEXTRA;
                devManager.productId = TEST_PRODUCTID;
                devManager.dsn = TEST_DSN;

                proxy.requestSetPushMapInfoEx(TEST_PLATFORM, pushManager, devManager);
            }
        });

        deviceunbindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushInfoManager pushManager = PushInfoManager.getInstance();
                DeviceManager devManager = new DeviceManager();
                pushManager.idType = EAIPushIdType._ETVSSpeakerIdentifier;
                pushManager.idExtra = ConstantValues.PUSHMGR_IDEXTRA;
                devManager.productId = TEST_PRODUCTID;
                devManager.dsn = TEST_DSN;

                proxy.requestDelPushMapInfo(TEST_PLATFORM, pushManager, devManager);
            }
        });

        getBindDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.requestGetPushDeviceInfo(TEST_PLATFORM);
            }
        });

        getBindAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushInfoManager pushManager = PushInfoManager.getInstance();
                DeviceManager devManager = new DeviceManager();
                pushManager.idType = EAIPushIdType._ETVSSpeakerIdentifier;
                pushManager.idExtra = ConstantValues.PUSHMGR_IDEXTRA;
                devManager.productId = TEST_PRODUCTID;
                devManager.dsn = TEST_DSN;
                proxy.requestGetBoundAcctByPushInfo(TEST_PLATFORM, pushManager, devManager);
            }
        });

        getMemberStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceManager = new DeviceManager();
                deviceManager.productId = TEST_PRODUCTID;
                deviceManager.dsn = TEST_DSN;
                deviceManager.deviceOEM = TEST_DEVOEM;
                deviceManager.deviceType = TEST_DEVTYPE;

                proxy.getMemberStatus(TEST_PLATFORM, deviceManager);
            }
        });

        getDeviceStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceManager = new DeviceManager();
                deviceManager.productId = TEST_PRODUCTID;
                deviceManager.dsn = TEST_DSN;
                deviceManager.deviceOEM = TEST_DEVOEM;
                deviceManager.deviceType = TEST_DEVTYPE;

                proxy.getDeviceStatus(TEST_PLATFORM, deviceManager);
            }
        });

        toSmartLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toSoftAPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toQRLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        proxy.handleCheckQQOpenTokenValid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                proxy.handleQQOpenIntent(requestCode, resultCode, data);
            }
            else if (RESULT_CANCELED == resultCode){
            }
        }
        else if (requestCode == WtloginHelper.QuickLoginRequestCode.REQUEST_QQ_LOGIN
                || requestCode == WtloginHelper.QuickLoginRequestCode.REQUEST_PT_LOGIN) {
            if (RESULT_OK == resultCode) {
                innerProxy.handleQQIntent(data);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccess(int type) {
        switch (type)
        {
            case AuthorizeListener.AUTH_TYPE:
                break;
            case AuthorizeListener.REFRESH_TYPE:
                wxLoginBtn.setEnabled(false);
                break;
            case AuthorizeListener.WX_TVSIDRECV_TYPE:
                wxLoginBtn.setEnabled(false);
                break;
            case AuthorizeListener.QQOPEN_TVSIDRECV_TYPE:
                qqOpenLoginBtn.setEnabled(false);
                break;
            case AuthorizeListener.QQ_TVSIDRECV_TYPE:
                qqLoginBtn.setEnabled(false);
                break;
            case AuthorizeListener.TOKENVERIFY_TYPE:
                qqOpenLoginBtn.setEnabled(false);
                qqLoginBtn.setEnabled(false);
                break;
            case AuthorizeListener.USERINFORECV_TYPE:
                UserInfoManager mgr = UserInfoManager.getInstance();
                break;
            case AuthorizeListener.WX_VALID_LOGIN_TYPE:
                break;
            case AuthorizeListener.QQOPEN_VALID_LOGIN_TYPE:
                break;
            case BindingListener.GET_CAPTCHA_TYPE:
                getCaptchaTextView.setText("Captcha Send Success");
                break;
            case BindingListener.BIND_PHONENUMBER_TYPE:
                bindPhoneNumberTextView.setText("Bind Success");
                break;
            case BindingListener.BIND_LOCATION_TYPE:
                bindLocationTextView.setText("Bind Success");
                break;
            case BindingListener.QUERY_LOCATION_TYPE:
                LocationInfo homeInfo = LocManager.getInstance().getLocation(ELocationType.HOME);
                LocationInfo companyInfo = LocManager.getInstance().getLocation(ELocationType.COMPANY);
                queryLocationLayout.setVisibility(View.VISIBLE);
                queryLocationTextView.setText("HomeInfo:"+homeInfo.addr+"|"+homeInfo.name+"|"+homeInfo.latitube+"|"+homeInfo.longitube
                                                            + "|" +homeInfo.cabAddr+"|"+homeInfo.cabName+"|"+homeInfo.cabLatitube+"|"+homeInfo.cabLongitube
                                                                +"\nCompanyInfo:"+companyInfo.addr+"|"+companyInfo.name+"|"+companyInfo.latitube+"|"+companyInfo.longitube
                                                                    + "|" +companyInfo.cabAddr+"|"+companyInfo.cabName+"|"+companyInfo.cabLatitube+"|"+companyInfo.cabLongitube);
                break;
            case BindingListener.SET_PUSH_MAP_INFOEX_TYPE:
                devicebindTextView.setText("Bind Success Ex");
                break;
            case BindingListener.SET_PUSH_MAP_INFO_TYPE:
                devicebindTextView.setText("Bind Success");
                break;
            case BindingListener.DEL_PUSH_MAP_INFO_TYPE:
                deviceunbindTextView.setText("Unbind Success");
                break;
            case BindingListener.GET_PUSH_DEVICE_INFO_TYPE:
                getBindDevicesTextView.setText("Devices count = " + ProductManager.getInstance().pushDeviceInfos.size());
                break;
            case BindingListener.GET_BOUND_ACCT_BY_PUSH_INFO_TYPE:
                getBindAccountTextView.setText("OpenId :" + ProductManager.getInstance().aiAcctInfo.strAcctId);
                break;
            case BindingListener.BIND_GET_MEMBER_STATUS_TYPE:
                getMemberStatusTextView.setText("vip=" + CPMemberManager.getInstance().vip + ",sT=" + CPMemberManager.getInstance().startTime +",eT=" + CPMemberManager.getInstance().expireTime);
                break;
            case BindingListener.BIND_GET_DEVICE_STATUS_TYPE:
                getDeviceStatusTextView.setText("sta=" + DeviceCPMemberManager.getInstance().deviceStatus
                                + ",amt=" + DeviceCPMemberManager.getInstance().deviceAmt
                                    +",amtU=" + DeviceCPMemberManager.getInstance().deviceAmtUnit);
                break;
        }
    }

    @Override
    public void onError(int type) {
        switch (type)
        {
            case AuthorizeListener.AUTH_TYPE:
                break;
            case AuthorizeListener.REFRESH_TYPE:
                wxLoginBtn.setEnabled(true);
                break;
            case AuthorizeListener.WX_TVSIDRECV_TYPE:
                wxLoginBtn.setEnabled(true);
                break;
            case AuthorizeListener.QQOPEN_TVSIDRECV_TYPE:
                qqOpenLoginBtn.setEnabled(true);
                break;
            case AuthorizeListener.QQ_TVSIDRECV_TYPE:
                qqLoginBtn.setEnabled(true);
                break;
            case AuthorizeListener.TOKENVERIFY_TYPE:
                qqOpenLoginBtn.setEnabled(true);
                qqLoginBtn.setEnabled(true);
                break;
            case AuthorizeListener.WX_VALID_LOGIN_TYPE:
                Toast.makeText(MainActivity.this, "WX Login InValid", Toast.LENGTH_SHORT).show();
                break;
            case AuthorizeListener.QQOPEN_VALID_LOGIN_TYPE:
                Toast.makeText(MainActivity.this, "QQOpen Login InValid", Toast.LENGTH_SHORT).show();
                break;
            case AuthorizeListener.USERINFORECV_TYPE:
                break;
            case BindingListener.GET_CAPTCHA_TYPE:
                getCaptchaTextView.setText("Captcha Send Error");
                break;
            case BindingListener.BIND_PHONENUMBER_TYPE:
                bindPhoneNumberTextView.setText("Bind Error: wrong captcha");
                break;
            case BindingListener.BIND_LOCATION_TYPE:
                bindLocationTextView.setText("Bind Error");
                break;
            case BindingListener.QUERY_LOCATION_TYPE:
                queryLocationLayout.setVisibility(View.VISIBLE);
                queryLocationTextView.setText("Query Error");
                break;
            case BindingListener.SET_PUSH_MAP_INFOEX_TYPE:
                devicebindTextView.setText("Bind Error Ex");
                break;
            case BindingListener.SET_PUSH_MAP_INFO_TYPE:
                devicebindTextView.setText("Bind Error");
                break;
            case BindingListener.DEL_PUSH_MAP_INFO_TYPE:
                deviceunbindTextView.setText("Unbind Error");
                break;
            case BindingListener.GET_PUSH_DEVICE_INFO_TYPE:
                getBindDevicesTextView.setText("Get BindDevices Error");
                break;
            case BindingListener.GET_BOUND_ACCT_BY_PUSH_INFO_TYPE:
                getBindAccountTextView.setText("GetBindACCT Error");
                break;
            case BindingListener.BIND_GET_MEMBER_STATUS_TYPE:
                getMemberStatusTextView.setText("Get Member Status Error");
                break;
            case BindingListener.BIND_GET_DEVICE_STATUS_TYPE:
                getDeviceStatusTextView.setText("Get Device Status Error");
                break;
        }
    }

    @Override
    public void onCancel(int type) {
        switch (type) {
            case AuthorizeListener.AUTH_TYPE:
                break;
        }
    }

    private void findViewsById() {
        netEnvRG = (RadioGroup) findViewById(R.id.netenvrg);
        testEnvRB = (RadioButton) findViewById(R.id.testenvrb);
        formalEnvRB = (RadioButton) findViewById(R.id.formalenvrb);

        wxLoginBtn = (Button)findViewById(R.id.wxlogin);
        wxLogoutBtn = (Button)findViewById(R.id.wxlogout);
        toUserCenterBtn = (Button) findViewById(R.id.tousercenterbtn);
        toUserCenterWithCallbackBtn = (Button) findViewById(R.id.tousercenterwithcbbtn);

        qqOpenLoginBtn = (Button)findViewById(R.id.qqopenlogin);
        qqOpenLogoutBtn = (Button)findViewById(R.id.qqopenlogout);

        qqLoginBtn = (Button)findViewById(R.id.qqlogin);
        qqLogoutBtn = (Button)findViewById(R.id.qqlogout);

        toGetClientIdBtn = (Button) findViewById(R.id.getclientidbtn);

        getCaptchaEditText = (EditText) findViewById(R.id.getcaptchaedittext);
        getCaptchaButton = (Button) findViewById(R.id.getcaptchabutton);
        getCaptchaTextView = (TextView) findViewById(R.id.getcaptchatextview);

        bindPhoneNumberEditText = (EditText) findViewById(R.id.bindnumberedittext);
        bindPhoneNumberButton = (Button) findViewById(R.id.bindnumberbutton);
        bindPhoneNumberTextView = (TextView) findViewById(R.id.bindnumbertextview);

        bindLocationButton = (Button) findViewById(R.id.bindlocation);
        queryLocationButton = (Button) findViewById(R.id.querylocation);
        bindLocationTextView = (TextView) findViewById(R.id.bindlocationtextview);

        queryLocationLayout = (LinearLayout) findViewById(R.id.querylocationlayout);
        queryLocationTextView = (TextView) findViewById(R.id.querylocationtextview);

        devicebindButton = (Button) findViewById(R.id.devicebindbtn);
        devicebindTextView = (TextView) findViewById(R.id.devicebindtextview);

        deviceunbindButton = (Button) findViewById(R.id.deviceunbindbtn);
        deviceunbindTextView = (TextView) findViewById(R.id.deviceunbindtextview);

        getBindDevicesButton = (Button) findViewById(R.id.getbinddevicesbtn);
        getBindDevicesTextView = (TextView) findViewById(R.id.getbinddevicestext);

        getBindAccountButton = (Button) findViewById(R.id.getbindaccountbtn);
        getBindAccountTextView = (TextView) findViewById(R.id.getbindaccounttext);

        getMemberStatusButton = (Button) findViewById(R.id.getmemberstatusbtn);
        getMemberStatusTextView = (TextView) findViewById(R.id.getmemberstatustext);

        getDeviceStatusButton = (Button) findViewById(R.id.getdevicestatusbtn);
        getDeviceStatusTextView = (TextView) findViewById(R.id.getdevicestatustext);

        toSmartLinkButton = (Button) findViewById(R.id.tosmartlinkbtn);
        toSoftAPButton = (Button) findViewById(R.id.tosoftapbtn);
        toQRLoginButton = (Button) findViewById(R.id.toqrloginbtn);

        wxTokenLayout = (LinearLayout) findViewById(R.id.wxtokenlayout);
        wxATTextView = (TextView)findViewById(R.id.accesstokenid);
        wxRTTextView = (TextView)findViewById(R.id.refreshtokenid);

        qqopenTokenLayout = (LinearLayout) findViewById(R.id.qqopentokenlayout);
        qqopenATTextView = (TextView)findViewById(R.id.accesstokenidqqopen);

        qqTokenLayout = (LinearLayout) findViewById(R.id.qqtokenlayout);
        qqATTextView = (TextView) findViewById(R.id.accesstokenidqq);
    }

    private void registerProxy() {
        proxy = LoginProxy.getInstance(TEST_APPID_WX, TEST_APPID_QQOPEN, this);
        innerProxy = LoginProxy.getInstance(TEST_APPID_WX, TEST_APPID_QQ, this);

        wxInfoManager = (WxInfoManager) proxy.getInfoManager(ELoginPlatform.WX);
        qqOpenInfoManager = (QQOpenInfoManager) proxy.getInfoManager(ELoginPlatform.QQOpen);
        qqInfoManager = (QQInfoManager) innerProxy.getInfoManager(ELoginPlatform.QQ);

        proxy.setOwnActivity(this);


        proxy.setAuthorizeListener(this);
        proxy.setBindingListener(this);

        innerProxy.setOwnActivity(this);
        innerProxy.setAuthorizeListener(this);
        innerProxy.setBindingListener(this);
    }

    private void requestProxyOp() {
        if (proxy.isTokenExist(ELoginPlatform.WX, this)) {
            proxy.requestTokenVerify(ELoginPlatform.WX, "productId", "dsn");
        }
        else {
            wxLoginBtn.setEnabled(true);
        }

        if (proxy.isTokenExist(ELoginPlatform.QQOpen, this)) {
            proxy.requestTokenVerify(ELoginPlatform.QQOpen, "productId", "dsn");
        }
        else {
            qqOpenLoginBtn.setEnabled(true);
        }

        if (innerProxy.isTokenExist(ELoginPlatform.QQ, this)) {
            innerProxy.requestTokenVerify(ELoginPlatform.QQ, "productId", "dsn");
        }
        else {
            qqLoginBtn.setEnabled(true);
        }
    }

    private void showTokenInfo() {
        if (!"".equals(wxInfoManager.accessToken)) {
            wxTokenLayout.setVisibility(View.VISIBLE);
            wxATTextView.setText("AccessToken:" + wxInfoManager.accessToken);
        }

        if (!"".equals(wxInfoManager.refreshToken)) {
            wxTokenLayout.setVisibility(View.VISIBLE);
            wxRTTextView.setText("RefreshToken:" + wxInfoManager.refreshToken);
        }

        if (!"".equals(qqOpenInfoManager.accessToken)) {
            qqopenTokenLayout.setVisibility(View.VISIBLE);
            qqopenATTextView.setText("AccessToken:" + qqOpenInfoManager.accessToken);
        }

        if (!"".equals(qqInfoManager.accessToken)) {
            qqTokenLayout.setVisibility(View.VISIBLE);
            qqATTextView.setText("AccessToken: " + qqInfoManager.accessToken);
        }
    }
}
