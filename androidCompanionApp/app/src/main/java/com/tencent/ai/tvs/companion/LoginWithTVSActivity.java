package com.tencent.ai.tvs.companion;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.ai.dobby.R;
import com.tencent.ai.tvs.AuthorizeListener;
import com.tencent.ai.tvs.ConstantValues;
import com.tencent.ai.tvs.LoginProxy;
import com.tencent.ai.tvs.env.ELoginPlatform;
import com.tencent.ai.tvs.info.QQOpenInfoManager;
import com.tencent.ai.tvs.info.UserInfoManager;
import com.tencent.ai.tvs.info.WxInfoManager;
import com.tencent.connect.common.Constants;

public class LoginWithTVSActivity extends AppCompatActivity implements AuthorizeListener {

    private static final String TAG = LoginWithTVSActivity.class.getName();

    private static final String BUNDLE_KEY_EXCEPTION = "exception";

    private static final int MIN_CONNECT_PROGRESS_TIME_MS = 1*1000;

    private ProvisioningClient mProvisioningClient;
    private DeviceProvisioningInfo mDeviceProvisioningInfo;

    private EditText mAddressTextView;
    private Button mConnectButton;
    private ProgressBar mConnectProgress;

    private ProgressBar mLoginProgressWX;
    private ImageButton mLoginButtonWX;
    private TextView mLoginMessage;

    private ProgressBar mLoginProgressQQ;
    private ImageButton mLoginButtonQQ;

    private String appidWx = "wxd077c3460b51e427";
    private String appidQQOpen = "222222";

    private LoginProxy proxy;

    private WxInfoManager wxInfoManager;
    private QQOpenInfoManager qqOpenInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tvslogin_activity);

        mAddressTextView = (EditText) findViewById(R.id.addressTextView);

        mConnectButton = (Button) findViewById(R.id.connectButton);
        mConnectProgress = (ProgressBar) findViewById(R.id.connectProgressBar);

        mLoginButtonWX = (ImageButton) findViewById(R.id.loginButtonWX);
        mLoginProgressWX = (ProgressBar) findViewById(R.id.loginProgressBarWX);
        mLoginMessage = (TextView) findViewById(R.id.loginMessage);

        mLoginButtonQQ = (ImageButton) findViewById(R.id.loginButtonQQ);
        mLoginProgressQQ = (ProgressBar) findViewById(R.id.loginProgressBarQQ);

        connectCleanState();

        try {
            mProvisioningClient = new ProvisioningClient(this);
        } catch(Exception e) {
            connectErrorState();
            showAlertDialog(e);
            Log.e(TAG, "Unable to use Provisioning Client. CA Certificate is incorrect or does not exist.", e);
        }

        String savedDeviceAddress = getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.saved_device_address), null);
        if (savedDeviceAddress != null) {
            mAddressTextView.setText(savedDeviceAddress);
        }

        proxy = LoginProxy.getInstance(appidWx, appidQQOpen);
        wxInfoManager = (WxInfoManager) proxy.getInfoManager(ELoginPlatform.WX);
        qqOpenInfoManager = (QQOpenInfoManager) proxy.getInfoManager(ELoginPlatform.QQOpen);

        proxy.setOwnActivity(this);
        proxy.setAuthorizeListener(this);
//        proxy.setLoginEnv(ELoginEnv.FORMAL);

        mConnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String address = mAddressTextView.getText().toString();
                mProvisioningClient.setEndpoint(address);

                new AsyncTask<Void, Void, DeviceProvisioningInfo>() {
                    private Exception errorInBackground;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        connectInProgressState();
                    }

                    @Override
                    protected DeviceProvisioningInfo doInBackground(Void... voids) {
                        try {
                            long startTime = System.currentTimeMillis();
                            DeviceProvisioningInfo response = mProvisioningClient.getDeviceProvisioningInfo();
                            long duration = System.currentTimeMillis() - startTime;

                            if (duration < MIN_CONNECT_PROGRESS_TIME_MS) {
                                try {
                                    Thread.sleep(MIN_CONNECT_PROGRESS_TIME_MS - duration);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            return response;
                        } catch (Exception e) {
                            errorInBackground = e;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(DeviceProvisioningInfo deviceProvisioningInfo) {
                        super.onPostExecute(deviceProvisioningInfo);
                        if (deviceProvisioningInfo != null) {
                            mDeviceProvisioningInfo = deviceProvisioningInfo;

                            SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.saved_device_address), address);
                            editor.commit();

                            if (proxy.isTokenExist(ELoginPlatform.WX, LoginWithTVSActivity.this)) {
                                proxy.requestTokenVerify(ELoginPlatform.WX, mDeviceProvisioningInfo.getProductId(), mDeviceProvisioningInfo.getDsn());
                            }
                            else {
                                connectSuccessState();
                            }

                            if (proxy.isTokenExist(ELoginPlatform.QQOpen, LoginWithTVSActivity.this)) {
                                proxy.requestTokenVerify(ELoginPlatform.QQOpen, mDeviceProvisioningInfo.getProductId(), mDeviceProvisioningInfo.getDsn());
                            }
                            else {
                                connectSuccessState();
                            }
                        } else {
                            connectCleanState();
                            showAlertDialog(errorInBackground);
                        }
                    }
                }.execute();
            }
        });

        mLoginButtonWX.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String productId = mDeviceProvisioningInfo.getProductId();
                final String dsn = mDeviceProvisioningInfo.getDsn();
                proxy.requestLogin(ELoginPlatform.WX, productId, dsn, LoginWithTVSActivity.this);
            }
        });

        mLoginButtonQQ.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String productId = mDeviceProvisioningInfo.getProductId();
                final String dsn = mDeviceProvisioningInfo.getDsn();
                proxy.requestLogin(ELoginPlatform.QQOpen, productId, dsn, LoginWithTVSActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        proxy.handleWxIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == -1) {
                proxy.handleQQOpenIntent(requestCode, resultCode, data);
            }
        }
    }

    private void connectCleanState() {
        mConnectButton.setVisibility(View.VISIBLE);
        mConnectProgress.setVisibility(View.GONE);

        mLoginButtonWX.setVisibility(View.GONE);
        mLoginProgressWX.setVisibility(View.GONE);
        mLoginMessage.setVisibility(View.GONE);

        mLoginButtonQQ.setVisibility(View.GONE);
        mLoginProgressQQ.setVisibility(View.GONE);
    }

    private void connectInProgressState() {
        mConnectButton.setVisibility(View.GONE);
        mConnectProgress.setVisibility(View.VISIBLE);
        mConnectProgress.setIndeterminate(true);

        mLoginButtonWX.setVisibility(View.GONE);
        mLoginProgressWX.setVisibility(View.GONE);
        mLoginMessage.setVisibility(View.GONE);

        mLoginButtonQQ.setVisibility(View.GONE);
        mLoginProgressQQ.setVisibility(View.GONE);
    }

    private void connectSuccessState() {
        mConnectButton.setVisibility(View.VISIBLE);
        mConnectProgress.setVisibility(View.GONE);

        mLoginButtonWX.setVisibility(View.VISIBLE);
        mLoginProgressWX.setVisibility(View.GONE);
        mLoginMessage.setVisibility(View.GONE);

        mLoginButtonQQ.setVisibility(View.VISIBLE);
        mLoginProgressQQ.setVisibility(View.GONE);
    }

    private void connectErrorState() {
        mConnectButton.setVisibility(View.GONE);
        mConnectProgress.setVisibility(View.GONE);

        mLoginButtonWX.setVisibility(View.GONE);
        mLoginProgressWX.setVisibility(View.GONE);
        mLoginMessage.setVisibility(View.GONE);

        mLoginButtonQQ.setVisibility(View.GONE);
        mLoginProgressQQ.setVisibility(View.GONE);
    }

    private void loginInProgressState() {
        mConnectButton.setVisibility(View.VISIBLE);
        mConnectProgress.setVisibility(View.GONE);

        mLoginButtonWX.setVisibility(View.GONE);
        mLoginProgressWX.setVisibility(View.VISIBLE);
        mLoginMessage.setVisibility(View.GONE);

        mLoginButtonQQ.setVisibility(View.GONE);
        mLoginProgressQQ.setVisibility(View.VISIBLE);
    }

    private void loginSuccessState() {
        mConnectButton.setVisibility(View.VISIBLE);
        mConnectProgress.setVisibility(View.GONE);

        mLoginButtonWX.setVisibility(View.GONE);
        mLoginProgressWX.setVisibility(View.GONE);
        mLoginMessage.setVisibility(View.VISIBLE);
        mLoginMessage.setText(R.string.success_message);

        mLoginButtonQQ.setVisibility(View.GONE);
        mLoginProgressQQ.setVisibility(View.GONE);
    }

    protected void showAlertDialog(Exception exception) {
        exception.printStackTrace();
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_EXCEPTION, exception);
        dialogFragment.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        dialogFragment.show(fm, "error_dialog");
    }

    @Override
    public void onSuccess(int type) {
        switch (type)
        {
            case AuthorizeListener.AUTH_TYPE:
                loginSuccessState();
                break;
            case AuthorizeListener.REFRESH_TYPE:
                loginSuccessState();
                break;
            case AuthorizeListener.WX_TVSIDRECV_TYPE:
                sendLoginInfoToDevice(ELoginPlatform.WX);
                break;
            case AuthorizeListener.QQOPEN_TVSIDRECV_TYPE:
                sendLoginInfoToDevice(ELoginPlatform.QQOpen);
                break;
            case AuthorizeListener.TOKENVERIFY_TYPE:
                sendLoginInfoToDevice(ELoginPlatform.QQOpen);
                break;
            case AuthorizeListener.USERINFORECV_TYPE:
                UserInfoManager mgr = UserInfoManager.getInstance();
                break;
        }
    }

    @Override
    public void onError(int type) {
        switch (type) {
            case AuthorizeListener.AUTH_TYPE:
                break;
            case AuthorizeListener.REFRESH_TYPE:
                connectSuccessState();
                break;
            case AuthorizeListener.WX_TVSIDRECV_TYPE:
                break;
            case AuthorizeListener.QQOPEN_TVSIDRECV_TYPE:
                break;
            case AuthorizeListener.TOKENVERIFY_TYPE:
                connectSuccessState();
                break;
            case AuthorizeListener.USERINFORECV_TYPE:
                break;
        }
    }

    private void sendLoginInfoToDevice(ELoginPlatform platform) {

        final String clientId = proxy.getClientId(platform);

        if (ConstantValues.INVALID_CLIENTID.equals(clientId)) {
            if (mDeviceProvisioningInfo != null) {
                proxy.requestTokenVerify(ELoginPlatform.WX, mDeviceProvisioningInfo.getProductId(), mDeviceProvisioningInfo.getDsn());
            }
            return;
        }

        final String sessionId = mDeviceProvisioningInfo.getSessionId();

        final CompanionProvisioningInfo companionProvisioningInfo = new CompanionProvisioningInfo(sessionId, clientId, "redirectUri", "authCode");

        new AsyncTask<Void, Void, Void>() {
            private Exception errorInBackground;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mProvisioningClient.postCompanionProvisioningInfo(companionProvisioningInfo);
                } catch (Exception e) {
                    errorInBackground = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (errorInBackground != null) {
                    connectCleanState();
                    showAlertDialog(errorInBackground);
                } else {
                    loginSuccessState();
                }
            }
        }.execute();
    }

    public static class ErrorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            Exception exception = (Exception) args.getSerializable(BUNDLE_KEY_EXCEPTION);
            String message = exception.getMessage();

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.error)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismiss();
                        }
                    })
                    .create();
        }
    }
}