package com.tencent.dingdang.tvs.ui.graphical;

import static com.tencent.dingdang.tvs.ui.controllers.DeviceNameViewController.DEVICE_LABEL;
import static com.tencent.dingdang.tvs.ui.controllers.DeviceNameViewController.DSN_LABEL;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tencent.dingdang.tvs.ui.DeviceNameUIHandler;

public class GraphicalDeviceNameView extends JPanel implements DeviceNameUIHandler {

    private static final String DSN_LABEL_NAME = "dsnlabel";
    private static final String PRODUCTID_LABEL_NAME = "productIDlabel";
    private static final String DEVICE_NAME = "devicename";

    GraphicalDeviceNameView(String productId, String dsn) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel productIdLabel = new JLabel(productId);
        JLabel dsnLabel = new JLabel(dsn);
        productIdLabel.setFont(productIdLabel.getFont().deriveFont(Font.PLAIN));
        dsnLabel.setFont(dsnLabel.getFont().deriveFont(Font.PLAIN));
        dsnLabel.setName(DSN_LABEL_NAME);
        productIdLabel.setName(PRODUCTID_LABEL_NAME);
        this.add(new JLabel(DEVICE_LABEL));
        this.add(productIdLabel);
        this.add(Box.createRigidArea(new Dimension(15, 0)));
        this.add(new JLabel(DSN_LABEL));
        this.add(dsnLabel);
        this.add(Box.createRigidArea(new Dimension(15, 0)));
        this.setName(DEVICE_NAME);
    }
}
