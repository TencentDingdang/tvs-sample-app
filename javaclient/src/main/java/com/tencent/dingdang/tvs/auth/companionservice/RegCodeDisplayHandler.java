package com.tencent.dingdang.tvs.auth.companionservice;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;

import javax.swing.JOptionPane;

import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.ui.DialogFactory;

public class RegCodeDisplayHandler {

    private final DialogFactory dialogFactory;
    private final DeviceConfig deviceConfig;

    public RegCodeDisplayHandler(DialogFactory dialogFactory, DeviceConfig deviceConfig) {
        this.dialogFactory = dialogFactory;
        this.deviceConfig = deviceConfig;
    }

    public void displayRegCode(String regCode) {
        String title = "Login to Register/Authenticate your Device";
        String regUrl =
            deviceConfig.getCompanionServiceInfo().getServiceUrl() + "/provision/" + regCode;
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            int selected = dialogFactory.showYesNoDialog(title,
                "Please register your device by visiting the following URL in "
                    + "a web browser and follow the instructions:\n" + regUrl
                    + "\n\n Would you like to open the URL automatically in your default browser?");
            if (selected == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().browse(new URI(regUrl));
                } catch (Exception e) {
                    // Ignore and proceed
                }
                title = "Registering/Authenticating Device";
                dialogFactory.showInformationalDialog(title,
                    "If a browser window did not open, please copy and paste the below URL into a "
                        + "web browser, and follow the instructions:\n" + regUrl
                        + "\n\n Continue only once you've authenticated with TVS.");
            } else {
                handleAuthenticationCopyToClipboard(title, regUrl);
            }
        } else {
            handleAuthenticationCopyToClipboard(title, regUrl);
        }
    }

    private void handleAuthenticationCopyToClipboard(String title, String regUrl) {
        int selected =
            dialogFactory.showYesNoDialog(title, "Please register your device by visiting "
                    + "the following URL in a web browser and follow the instructions:\n" + regUrl
                    + "\n\n Would you like the URL copied to your clipboard?");
        if (selected == JOptionPane.YES_OPTION) {
            copyToClipboard(regUrl);
        }
        dialogFactory.showInformationalDialog(title, "Continue once you've authenticated "
                + "with TVS");
    }

    private void copyToClipboard(String text) {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
        systemClipboard.setContents(new StringSelection(text), null);
    }
}
