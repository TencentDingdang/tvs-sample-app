package com.tencent.dingdang.tvs.message.response.templateruntime;

import com.tencent.dingdang.tvs.message.Payload;

public class RenderTemplate extends Payload {
    private String type;
    private Title title;
    private String textField;

    public final void setType(String type) {
        this.type = type;
    }

    public final void setTitle(Title title) {
        this.title = title;
    }

    public final void setTextField(String textField) {
        this.textField = textField;
    }

    public final Title getTitle() {
        return title;
    }

    public final String getType() {
        return type;
    }

    public final String getTextField() {
        return textField;
    }
}
