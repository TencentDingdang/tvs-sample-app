package com.tencent.dingdang.tvs.message.response.templateruntime;

public interface CardHandler {
    public void renderCard(RenderTemplate payload, String rawMessage);

    public void renderPlayerInfo(String rawMessage);
}
