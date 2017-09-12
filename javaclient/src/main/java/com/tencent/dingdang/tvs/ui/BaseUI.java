package com.tencent.dingdang.tvs.ui;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.auth.AuthSetup;
import com.tencent.dingdang.tvs.config.DeviceConfig;

/**
 * The base class that encompasses all UI behavior.
 */
public abstract class BaseUI {

    // Set by this base class
    protected DeviceConfig config;
    protected TVSController controller;
    protected AuthSetup authSetup;

    // Should be set by subclasses
    protected LocaleUIHandler localeView;
    protected DeviceNameUIHandler deviceNameView;
    protected CardUIHandler cardView;
    protected AccessTokenUIHandler bearerTokenView;
    protected NotificationsUIHandler notificationsView;
    protected ListenUIHandler listenView;
    protected PlaybackControlsUIHandler playbackControlsView;
    protected UserSpeechVisualizerUIHandler visualizerView;
    protected LoginLogoutUIHandler loginLogoutView;
    protected MainUIHandler mainView;

    protected BaseUI(TVSController controller, AuthSetup authSetup, DeviceConfig config)
            throws Exception {
        this.authSetup = authSetup;
        this.controller = controller;
        this.config = config;
        createViews(config);
        addListeners();
        init(config);
        startAuthentication();
    }

    /**
     * Make sure required components are registered with each other before initialization.
     */
    private void addListeners() {
        listenView.addSpeechStateChangeListener(playbackControlsView);
        listenView.addSpeechStateChangeListener(visualizerView);
        listenView.addSpeechStateChangeListener(cardView);
        authSetup.addAccessTokenListener(listenView);
        authSetup.addAccessTokenListener(playbackControlsView);
        authSetup.addAccessTokenListener(loginLogoutView);
        authSetup.addAccessTokenListener(bearerTokenView);
        authSetup.addAccessTokenListener(controller);
    }

    /**
     * Initializes the controller, and any other initialization tasks that subclasses require.
     */
    private void init(DeviceConfig config) {
        initialize(config);
        controller.init(listenView, notificationsView, cardView);
        controller.startHandlingDirectives();
    }

    /**
     * Triggers the authentication flow for the specific UI.
     */
    protected abstract void startAuthentication();

    /**
     * Any views for interacting/displaying UI elements should be created here. As a result of
     * calling this method, all UIHandlers within BaseUI should be non-null.
     */
    protected abstract void createViews(DeviceConfig config);

    /**
     * Any initialization logic that a subclass needs to do should be done here. This occurs
     * after views are created, but before authentication.
     */
    protected abstract void initialize(DeviceConfig config);
}
