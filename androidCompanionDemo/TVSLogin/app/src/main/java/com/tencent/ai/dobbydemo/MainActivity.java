package com.tencent.ai.dobbydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.tencent.ai.tvs.business.AlarmBusiness;
import com.tencent.ai.tvs.business.AlarmBusinessDeviceInfo;
import com.tencent.ai.tvs.business.AlarmBusinessInfo;
import com.tencent.ai.tvs.business.EAlarmOpResultType;
import com.tencent.ai.tvs.business.EAlarmOper;
import com.tencent.ai.tvs.business.EAlarmRepeatType;
import com.tencent.ai.tvs.comm.CommOpInfo;
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
import com.tencent.ai.tvs.qrcode.QRStateListener;
import com.tencent.ai.tvs.ui.UserCenterStateListener;
import com.tencent.connect.common.Constants;

import java.util.ArrayList;

import SmartService.EAIPushIdType;
import oicq.wlogin_sdk.request.WtloginHelper;

public class MainActivity extends AppCompatActivity implements AuthorizeListener, BindingListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String TEST_APPID_WX = "wxdbd76c1af795f58e";
    private static final String TEST_APPID_QQOPEN = "101470979";
    private static final long TEST_APPID_QQ = 1600001268L;

    private static final long TIME_MILLIS_DELTA = 1000 * 60 * 60;


    private static final String TEST_APPKEY = "7e8ab486-c6f6-4ecc-b52e-7ea8da82c9da";
    private static final String TEST_APPACCEETOKEN = "9cb1fbf4c54442cc80c9aed8cb3c25b6";
    private static final String TEST_GUID = "9cb1fbf4c54442cc80c9aed8cb3c25b6";
    private static final String TEST_PRODUCTID = "7e8ab486-c6f6-4ecc-b52e-7ea8da82c9da:9cb1fbf4c54442cc80c9aed8cb3c25b6";
    private static final String TEST_DSN = "FF31F085A55DD019C8575CFB";

    private static final String TEST_DEVOEM = "GGMM";
    private static final String TEST_DEVTYPE = "SPEAKER";

    private static final String TEST_WX_AUTH_RET = "{\"access_token\":\"8_SIkvkPRb_YzKpfLNal5aANjPkZri_eZiOUh7pYuN2dF8liBhVyL9lawYb3Em-S6R1bzw_uPi6W8PMSFHK-DVP5SMR5GE6HsCRHdzuJcTz6s\",\"expires_in\":7200,\"refresh_token\":\"8_umoSNLEEY_u-a48x39m-Ti41sVNe4actHJHYBu07i4e9yv3bS9Fakq0xOlLnhwOSCng3ZnjuRu7v5cXa68Wv9AtiHF-_zOBXjF32dmIVJqI\",\"openid\":\"olW1HwuZs4zRIiTs8xN_5i65DU4Q\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"o9GiTuAkK5sryCobPgdS_iDo1W8A\"}";
    private static final String TEST_WX_REFRESH_RET = "{\"openid\":\"olW1HwuZs4zRIiTs8xN_5i65DU4Q\",\"access_token\":\"8_SIkvkPRb_YzKpfLNal5aANjPkZri_eZiOUh7pYuN2dF8liBhVyL9lawYb3Em-S6R1bzw_uPi6W8PMSFHK-DVP5SMR5GE6HsCRHdzuJcTz6s\",\"expires_in\":7200,\"refresh_token\":\"8_umoSNLEEY_u-a48x39m-Ti41sVNe4actHJHYBu07i4e9yv3bS9Fakq0xOlLnhwOSCng3ZnjuRu7v5cXa68Wv9AtiHF-_zOBXjF32dmIVJqI\",\"scope\":\"snsapi_base,snsapi_userinfo,\"}";
    private static final String TEST_QQOPEN_AUTH_RET = "{\"ret\":0,\"openid\":\"24361BBA12837FFA742C79EC810A6DA8\",\"access_token\":\"B8EC8C0ADD6DEF7F2E380C8DC8CD6451\",\"pay_token\":\"399D4E199447C1D2B51376E6692CC55C\",\"expires_in\":7776000,\"pf\":\"desktop_m_qq-10000144-android-2002-\",\"pfkey\":\"7195cbb183f325e51c93a09d441f9243\",\"msg\":\"\",\"login_cost\":98,\"query_authority_cost\":352,\"authority_cost\":0,\"expires_time\":1530603609881}";
    private static final String TEST_WX_USER_RET = " {\"openid\":\"olW1HwuZs4zRIiTs8xN_5i65DU4Q\",\"nickname\":\"时间段hddjd大喊大叫觉得\",\"sex\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"headimgurl\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/eM2qvBP8HYxrXdCTlAib2ibmeJw4LYZfJOYsbDShNocXuIUWEnQ1Nwh5Nk5lBjb3LJhOt1r2dt5lHtIAdcUGx7RA\\/132\",\"privilege\":[],\"unionid\":\"o9GiTuAkK5sryCobPgdS_iDo1W8A\"}";
    private static final String TEST_QQOPEN_USER_RET = "{\"ret\":0,\"msg\":\"\",\"is_lost\":0,\"nickname\":\"\",\"gender\":\"男\",\"province\":\"\",\"city\":\"\",\"year\":\"0\",\"figureurl\":\"http:\\/\\/qzapp.qlogo.cn\\/qzapp\\/1105886239\\/24361BBA12837FFA742C79EC810A6DA8\\/30\",\"figureurl_1\":\"http:\\/\\/qzapp.qlogo.cn\\/qzapp\\/1105886239\\/24361BBA12837FFA742C79EC810A6DA8\\/50\",\"figureurl_2\":\"http:\\/\\/qzapp.qlogo.cn\\/qzapp\\/1105886239\\/24361BBA12837FFA742C79EC810A6DA8\\/100\",\"figureurl_qq_1\":\"http:\\/\\/thirdqq.qlogo.cn\\/qqapp\\/1105886239\\/24361BBA12837FFA742C79EC810A6DA8\\/40\",\"figureurl_qq_2\":\"http:\\/\\/thirdqq.qlogo.cn\\/qqapp\\/1105886239\\/24361BBA12837FFA742C79EC810A6DA8\\/100\",\"is_yellow_vip\":\"0\",\"vip\":\"0\",\"yellow_vip_level\":\"0\",\"level\":\"0\",\"is_yellow_year_vip\":\"0\"}";

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
    private Button reportEndStateBtn, toUserCenterWithCallbackBtn, toGetClientIdBtn;
    private Button alarmCreate, alarmDel, alarmUpdate, alarmQuery;
    private long addedAlarmId;

    private TextView reportRelationTextView;

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

    private boolean isSimpleInterface;

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
                if (isSimpleInterface) {
                    proxy.tvsAuth(TEST_PLATFORM, TEST_WX_AUTH_RET);
                }
                else {
                    proxy.requestLogin(ELoginPlatform.WX, TEST_PRODUCTID, TEST_DSN, MainActivity.this);
                }
            }
        });

        qqOpenLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEST_PLATFORM = ELoginPlatform.QQOpen;
                if (isSimpleInterface) {
                    proxy.tvsAuth(TEST_PLATFORM, TEST_QQOPEN_AUTH_RET);
                }
                else {
                    proxy.requestLogin(ELoginPlatform.QQOpen, TEST_PRODUCTID, TEST_DSN, MainActivity.this);
                }
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
                Log.v(LOG_TAG, "ClientId = " + proxy.getClientId(TEST_PLATFORM));
            }
        });

        alarmCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmBusiness business = new AlarmBusiness();

                ArrayList<AlarmBusinessInfo> alarmBusinessInfos = new ArrayList<>();
                AlarmBusinessInfo alarmBusinessInfo = getNewAlarmBusinessInfo();
                alarmBusinessInfos.add(alarmBusinessInfo);

                business.alarmBusinessInfos = alarmBusinessInfos;
                business.alarmOper = EAlarmOper.CREATE;

                proxy.requestAlarmManagement(TEST_PLATFORM, business);
            }
        });

        alarmDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmBusiness business = new AlarmBusiness();

                ArrayList<AlarmBusinessInfo> alarmBusinessInfos = new ArrayList<>();
                AlarmBusinessInfo alarmBusinessInfo = new AlarmBusinessInfo();
                alarmBusinessInfo.deviceInfo = getAlarmBusinessDeviceInfo();
                alarmBusinessInfo.alarmId = addedAlarmId;
                alarmBusinessInfos.add(alarmBusinessInfo);

                business.alarmBusinessInfos = alarmBusinessInfos;
                business.alarmOper = EAlarmOper.DELETE;

                proxy.requestAlarmManagement(TEST_PLATFORM, business);
            }
        });

        alarmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmBusiness business = new AlarmBusiness();

                ArrayList<AlarmBusinessInfo> alarmBusinessInfos = new ArrayList<>();
                AlarmBusinessInfo alarmBusinessInfo = new AlarmBusinessInfo();
                alarmBusinessInfo.deviceInfo = getAlarmBusinessDeviceInfo();
                alarmBusinessInfo.alarmId = addedAlarmId;
                alarmBusinessInfo.alarmTime = System.currentTimeMillis() + TIME_MILLIS_DELTA * 2;
                alarmBusinessInfo.note = "New Alarm note";
                alarmBusinessInfo.repeatType = EAlarmRepeatType.DAY;
                alarmBusinessInfos.add(alarmBusinessInfo);

                business.alarmBusinessInfos = alarmBusinessInfos;
                business.alarmOper = EAlarmOper.UPDATE;

                proxy.requestAlarmManagement(TEST_PLATFORM, business);
            }
        });

        alarmQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmBusiness business = new AlarmBusiness();

                ArrayList<AlarmBusinessInfo> alarmBusinessInfos = new ArrayList<>();
                AlarmBusinessInfo alarmBusinessInfo = new AlarmBusinessInfo();
                alarmBusinessInfo.deviceInfo = getAlarmBusinessDeviceInfo();
                alarmBusinessInfos.add(alarmBusinessInfo);

                business.alarmBusinessInfos = alarmBusinessInfos;
                business.alarmOper = EAlarmOper.QUERY;

                proxy.requestAlarmManagement(TEST_PLATFORM, business);
            }
        });

        reportEndStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceManager = new DeviceManager();
                deviceManager.productId = TEST_PRODUCTID;
                deviceManager.dsn = TEST_DSN;
                deviceManager.deviceOEM = TEST_DEVOEM;
                deviceManager.deviceType = TEST_DEVTYPE;
                deviceManager.guid = "";
                proxy.reportEndState(TEST_PLATFORM, deviceManager);
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
                    public void onSuccess(ELoginPlatform platform, int type, CommOpInfo commOpInfo) {
                        switch (type) {
                            case UserCenterStateListener.LOGIN_TYPE:
                                break;
                            case UserCenterStateListener.LOGOUT_TYPE:
                                break;
                        }
                    }

                    @Override
                    public void onError(int type, CommOpInfo commOpInfo) {
                        switch (type) {
                            case UserCenterStateListener.LOGIN_TYPE:
                                break;
                        }
                    }

                    @Override
                    public void onCancel(int type, CommOpInfo commOpInfo) {

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
                Intent intent = new Intent(MainActivity.this, SmartLinkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loginplatform", TEST_PLATFORM.ordinal());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        toSoftAPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SoftAPActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loginplatform", TEST_PLATFORM.ordinal());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        toQRLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginProxy loginProxy = LoginProxy.getWebInstance(null, null, MainActivity.this);
                loginProxy.requestQRLogin(MainActivity.this, new QRStateListener() {
                    @Override
                    public void onSuccess(ELoginPlatform platform, int type, CommOpInfo commOpInfo) {
                        if (QRStateListener.LOGIN_TYPE == type) {
                            Toast.makeText(MainActivity.this, "QRLogin valid token", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(int type, CommOpInfo commOpInfo) {
                        Toast.makeText(MainActivity.this, "QRLogin invalid token", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(int type, CommOpInfo commOpInfo) {

                    }
                });
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
    public void onSuccess(int type, CommOpInfo commOpInfo) {
        switch (type)
        {
            case AuthorizeListener.AUTH_TYPE:
                break;
            case AuthorizeListener.REFRESH_TYPE:
                wxLoginBtn.setEnabled(false);
                break;
            case AuthorizeListener.WX_TVSIDRECV_TYPE:
                wxLoginBtn.setEnabled(false);
                if (isSimpleInterface) {
                    proxy.tvsSetUser(ELoginPlatform.WX, TEST_WX_USER_RET);
                }
                break;
            case AuthorizeListener.QQOPEN_TVSIDRECV_TYPE:
                qqOpenLoginBtn.setEnabled(false);
                if (isSimpleInterface) {
                    proxy.tvsSetUser(ELoginPlatform.QQOpen, TEST_QQOPEN_USER_RET);
                }
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
            case AuthorizeListener.REPORTENDSTATE_TYPE:
                reportRelationTextView.setText("Succ");
                break;
            case AuthorizeListener.MANAGEACCT_TYPE:
                break;
            case AuthorizeListener.ALARMMANAGEMENT_TYPE:
                AlarmBusiness alarmBusiness = ProductManager.getInstance().alarmBusiness;
                ArrayList<AlarmBusinessInfo> effectBusinessInfos = alarmBusiness.alarmBusinessInfos;
                EAlarmOper alarmOper = alarmBusiness.alarmOper;
                Toast.makeText(MainActivity.this, alarmOper + " Alarms Size = " + effectBusinessInfos.size(), Toast.LENGTH_SHORT).show();
                if (effectBusinessInfos != null && effectBusinessInfos.size() > 0) {
                    AlarmBusinessInfo alarmBusinessInfo = effectBusinessInfos.get(0);
                    Log.v(LOG_TAG, alarmOper + " --- " + alarmBusinessInfo.toString());
                    if (EAlarmOper.CREATE == alarmOper) {
                        addedAlarmId = alarmBusinessInfo.alarmId;
                    }
                }
                Log.v(LOG_TAG, "retCode = " + commOpInfo.retCode);
                if (commOpInfo.retCode == EAlarmOpResultType.NO_ALARM_DATA.getOpResult()) {
                    Log.v(LOG_TAG, "No Alarm Data");
                }
                break;
        }
    }

    @Override
    public void onError(int type, CommOpInfo commOpInfo) {
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
            case AuthorizeListener.REPORTENDSTATE_TYPE:
                reportRelationTextView.setText("Error");
                break;
            case AuthorizeListener.MANAGEACCT_TYPE:
                break;
            case AuthorizeListener.ALARMMANAGEMENT_TYPE:
                Toast.makeText(MainActivity.this, "alarmManagement onError", Toast.LENGTH_SHORT).show();
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
        reportEndStateBtn = (Button) findViewById(R.id.reportendstatebtn);
        toUserCenterWithCallbackBtn = (Button) findViewById(R.id.tousercenterwithcbbtn);

        reportRelationTextView = (TextView) findViewById(R.id.reportrelationtextview);

        qqOpenLoginBtn = (Button)findViewById(R.id.qqopenlogin);
        qqOpenLogoutBtn = (Button)findViewById(R.id.qqopenlogout);

        qqLoginBtn = (Button)findViewById(R.id.qqlogin);
        qqLogoutBtn = (Button)findViewById(R.id.qqlogout);

        toGetClientIdBtn = (Button) findViewById(R.id.getclientidbtn);

        alarmCreate = (Button) findViewById(R.id.alarmcreate);
        alarmDel = (Button) findViewById(R.id.alarmdel);
        alarmUpdate = (Button) findViewById(R.id.alarmupdate);
        alarmQuery = (Button) findViewById(R.id.alarmquery);

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

        proxy.initNetEnv();
        proxy.setOwnActivity(this);


        proxy.setAuthorizeListener(this);
        proxy.setBindingListener(this);

        innerProxy.setOwnActivity(this);
        innerProxy.setAuthorizeListener(this);
        innerProxy.setBindingListener(this);

        isSimpleInterface = false;
    }

    private void requestProxyOp() {
        if (proxy.isTokenExist(ELoginPlatform.WX, this)) {
            if (isSimpleInterface) {
                proxy.tvsAuth(ELoginPlatform.WX, TEST_WX_REFRESH_RET);
            }
            else {
                proxy.requestTokenVerify(ELoginPlatform.WX, "productId", "dsn");
            }
        }
        else {
            wxLoginBtn.setEnabled(true);
        }

        if (proxy.isTokenExist(ELoginPlatform.QQOpen, this)) {
            if (isSimpleInterface) {
                proxy.tvsQQOpenVerify(qqOpenInfoManager.appId, qqOpenInfoManager.openID, qqOpenInfoManager.accessToken);
            }
            else {
                proxy.requestTokenVerify(ELoginPlatform.QQOpen, "productId", "dsn");
            }
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

    private AlarmBusinessDeviceInfo getAlarmBusinessDeviceInfo() {
        AlarmBusinessDeviceInfo alarmBusinessDeviceInfo = new AlarmBusinessDeviceInfo();
        alarmBusinessDeviceInfo.productId = TEST_PRODUCTID;
        alarmBusinessDeviceInfo.guid = TEST_GUID;
        return alarmBusinessDeviceInfo;
    }

    private AlarmBusinessInfo getNewAlarmBusinessInfo() {
        AlarmBusinessInfo alarmBusinessInfo = new AlarmBusinessInfo();
        alarmBusinessInfo.deviceInfo = getAlarmBusinessDeviceInfo();
        alarmBusinessInfo.alarmTime = System.currentTimeMillis() + TIME_MILLIS_DELTA;
        alarmBusinessInfo.note = "Alarm note";
        alarmBusinessInfo.repeatType = EAlarmRepeatType.ONCE;
        return alarmBusinessInfo;
    }
}
