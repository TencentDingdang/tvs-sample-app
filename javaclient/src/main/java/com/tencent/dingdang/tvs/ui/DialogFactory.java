package com.tencent.dingdang.tvs.ui;

/**
 * Factory providing prompts to the user telling them information or asking a Yes/No question.
 */
public interface DialogFactory {

    /**
     * Tell the user some information. The user must acknowledge this information before continuing.
     *
     * @param title
     *         Description of the type of message.
     * @param message
     *         Content of the message.
     */
    void showInformationalDialog(String title, String message);

    /**
     * Ask the user a Yes/No question.
     *
     * @param title
     *         Description of the type of message.
     * @param message
     *         Question text.
     * @return 0 if the user pressed yes, 1 if the user pressed no. Matches JOptionPane results.
     */
    int showYesNoDialog(String title, String message);
}
