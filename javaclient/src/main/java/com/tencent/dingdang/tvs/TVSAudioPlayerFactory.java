package com.tencent.dingdang.tvs;

public class TVSAudioPlayerFactory {

    public TVSAudioPlayer getAudioPlayer(TVSController controller) {
        return new TVSAudioPlayer(controller);
    }
}
