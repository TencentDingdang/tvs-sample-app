package com.tencent.dingdang.tvs.ui.graphical;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.tencent.dingdang.tvs.ui.DialogFactory;

public class GraphicalDialogFactory implements DialogFactory {

    private JFrame contentPane;

    GraphicalDialogFactory(JFrame contentPane) {
        this.contentPane = contentPane;
    }

    @Override
    public void showInformationalDialog(String title, String message) {
        JTextArea textMessage = new JTextArea(message);
        textMessage.setEditable(false);
        JOptionPane.showMessageDialog(contentPane, textMessage, title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public int showYesNoDialog(String title, String message) {
        JTextArea textMessage = new JTextArea(message);
        textMessage.setEditable(false);
        return JOptionPane.showConfirmDialog(contentPane, textMessage, title,
                JOptionPane.YES_NO_OPTION
        );
    }
}
