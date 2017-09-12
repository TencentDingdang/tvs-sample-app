package com.tencent.dingdang.tvs.ui;

import com.tencent.dingdang.tvs.auth.AccessTokenListener;

public interface LoginLogoutUIHandler extends AccessTokenListener {

    void startLogin();

    void startLogout();
}
