package com.tencent.dingdang.tvs.message;

public class DialogRequestIdHeader extends MessageIdHeader {

    private String dialogRequestId;

    public DialogRequestIdHeader() {
        // For Jackson
    }

    public DialogRequestIdHeader(String namespace, String name, String dialogRequestId) {
        super(namespace, name);
        this.dialogRequestId = dialogRequestId;
    }

    public final void setDialogRequestId(String dialogRequestId) {
        this.dialogRequestId = dialogRequestId;
    }

    public final String getDialogRequestId() {
        return dialogRequestId;
    }

    @Override
    public String toString() {
        return String.format("%1$s dialogRequestId:%2$s", super.toString(), dialogRequestId);
    }
}
