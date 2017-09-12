package com.tencent.dingdang.tvs.ui.graphical;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.config.DeviceConfig;

import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tencent.dingdang.tvs.ui.LocaleUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.LocaleViewController;

public class GraphicalLocaleView extends JPanel implements LocaleUIHandler {

    private static final String LOCALE_LABEL = "Locale:";
    private static final String LOCALE_NAME = "Locale";

    private LocaleViewController localeViewController;

    GraphicalLocaleView(DeviceConfig config, TVSController controller) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        localeViewController = new LocaleViewController(config, controller);
        JLabel localeLabel = new JLabel(LOCALE_LABEL);
        this.add(localeLabel);
        localeLabel.setName(LOCALE_NAME);
        JComboBox<Object> localeSelector =
                new JComboBox<>(config.getSupportedLocalesLanguageTag().toArray());
        localeSelector.setSelectedItem(config.getLocale().toLanguageTag());
        localeSelector.addActionListener(e -> {
            Locale locale = Locale.forLanguageTag(localeSelector.getSelectedItem().toString());
            handleLocaleChange(locale);
        });
        this.add(localeSelector);
    }

    @Override
    public void handleLocaleChange(Locale locale) {
        localeViewController.handleLocaleChange(locale);
    }
}
