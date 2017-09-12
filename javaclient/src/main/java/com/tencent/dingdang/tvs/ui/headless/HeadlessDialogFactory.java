package com.tencent.dingdang.tvs.ui.headless;

import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;

import com.tencent.dingdang.tvs.ui.DialogFactory;

public class HeadlessDialogFactory implements DialogFactory {

    private BlockingQueue<String> userInputs;

    public HeadlessDialogFactory(BlockingQueue<String> userInputs) {
        this.userInputs = userInputs;
    }

    @Override
    public void showInformationalDialog(String title, String message) {
        System.out.println(title.toUpperCase() + ": " + message);
        String input;
        try {
            while ((input = userInputs.take()) != null) {
                switch (input) {
                    case "enter":
                        System.out.println("OK");
                        return;
                    case "exit":
                    case "quit":
                        System.exit(0);
                    default:
                        System.out.println("Invalid input. Please just press enter to continue");
                }
            }
        } catch (InterruptedException e) {
            System.exit(0);
        }
    }

    @Override
    public int showYesNoDialog(String title, String message) {
        System.out.println(title.toUpperCase() + ": " + message + " [y/n/exit]");
        String input;
        try {
            while ((input = userInputs.take()) != null) {
                switch (input) {
                    case "y":
                        return JOptionPane.YES_OPTION;
                    case "n":
                        return JOptionPane.NO_OPTION;
                    case "exit":
                    case "quit":
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please type y, n, or exit");
                }
            }
        } catch (InterruptedException e) {
            System.exit(0);
        }
        return -1;
    }
}
