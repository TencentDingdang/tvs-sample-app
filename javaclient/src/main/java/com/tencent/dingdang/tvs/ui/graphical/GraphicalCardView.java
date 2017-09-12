package com.tencent.dingdang.tvs.ui.graphical;

import com.tencent.dingdang.tvs.message.response.templateruntime.RenderTemplate;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.tencent.dingdang.tvs.ui.CardUIHandler;

public class GraphicalCardView extends JPanel implements CardUIHandler {

    private static final int CARD_WIDTH = 400;
    private static final int CARD_HEIGHT = 300;
    private static final String CARD_PANEL_NAME = "cardpanel";

    private CardPanel cardPanel;

    GraphicalCardView() {
        super();
        this.cardPanel = new CardPanel();
        JScrollPane cardContainer = new JScrollPane(this.cardPanel);
        cardContainer.setBorder(new LineBorder(Color.BLACK));
        cardContainer.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        this.add(cardContainer);
        this.cardPanel.setName(CARD_PANEL_NAME);
    }

    @Override
    public void onProcessing() {
    }

    @Override
    public void onListening() {
        SwingUtilities.invokeLater(() -> cardPanel.clearCard());
    }

    @Override
    public void onProcessingFinished() {
    }

    @Override
    public void renderCard(RenderTemplate payload, String rawMessage) {
        SwingUtilities.invokeLater(() -> cardPanel.generateCard(payload, rawMessage));
    }

    @Override
    public void renderPlayerInfo(String rawMessage) {
        SwingUtilities.invokeLater(() -> cardPanel.generatePlayerInfo(rawMessage));
    }

}
