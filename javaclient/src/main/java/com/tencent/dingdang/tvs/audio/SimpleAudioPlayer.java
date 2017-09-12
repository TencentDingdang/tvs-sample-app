package com.tencent.dingdang.tvs.audio;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import javazoom.jl.player.Player;

public class SimpleAudioPlayer {
    private static final Logger log = LoggerFactory.getLogger(SimpleAudioPlayer.class);

    public SimpleAudioPlayer() {
    }

    /**
     * Play the given input stream. Note: This method blocks.
     *
     * @param inputStream
     *            MP3 Audio bytes to play
     */
    public void play(InputStream inputStream) {
        try {
            Player player = new Player(inputStream);
            player.play();
        } catch (Exception e) {
            log.error("An error occurred while trying to play audio", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
