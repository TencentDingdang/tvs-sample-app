package com.tencent.dingdang.tvs;

import java.util.UUID;

/**
 * DialogRequestIdAuthority creates and keeps track of the active dialogRequestId.
 */
public class DialogRequestIdAuthority {

    private static final DialogRequestIdAuthority instance;

    static {
        instance = new DialogRequestIdAuthority();
    }

    private String currentDialogRequestId;

    private DialogRequestIdAuthority() {
    }

    public static DialogRequestIdAuthority getInstance() {
        return instance;
    }

    public String createNewDialogRequestId() {
        currentDialogRequestId = UUID.randomUUID().toString();
        return currentDialogRequestId;
    }

    public boolean isCurrentDialogRequestId(String candidateRequestId) {
        return currentDialogRequestId != null && currentDialogRequestId.equals(candidateRequestId);
    }
}
