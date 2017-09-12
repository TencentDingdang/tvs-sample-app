package com.tencent.dingdang.tvs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Since VLC's offsets are not accurate, this class manages calculating the correct offset using
 * System.nanoTime().
 */
public class AudioPlayerTimer {
    private static final Logger log = LoggerFactory.getLogger(AudioPlayerTimer.class);

    private long startNano;
    private long elapsedTimeMs;
    private long totalStreamLength;
    private boolean isPlaying = false;

    public synchronized void start() {
        log.debug("Starting AudioPlayerTimer");
        startNano = System.nanoTime();
        isPlaying = true;
    }

    public synchronized void stop() {
        log.debug("Stopping AudioPlayerTimer");
        if (isPlaying) {
            elapsedTimeMs += getCurrentOffsetInMilliseconds();
            isPlaying = false;
        }
    }

    public synchronized long getOffsetInMilliseconds() {
        long offset = elapsedTimeMs + (isPlaying ? getCurrentOffsetInMilliseconds() : 0);
        if (totalStreamLength > 0) {
            // If we know the length of the song, we don't want to send offsets that are beyond the
            // length. This is to correct the minor discrepancies between what VLC thinks is the end
            // of the song and what Java's internal timer thinks is the end of the song.
            offset = Math.min(totalStreamLength, offset);
        }
        log.debug("Getting offset. Offset: " + offset);
        return offset;
    }

    public void reset() {
        reset(0);
    }

    public void reset(long startPosition) {
        reset(startPosition, -1);
    }

    public synchronized void reset(long startPosition, long maxPosition) {
        log.debug("Resetting AudioPlayerTimer");
        elapsedTimeMs = startPosition;
        isPlaying = false;
        startNano = System.nanoTime();
        totalStreamLength = maxPosition;
    }

    private long getCurrentOffsetInMilliseconds() {
        return TimeUnit.MILLISECONDS.convert(System.nanoTime() - startNano, TimeUnit.NANOSECONDS);
    }
}
