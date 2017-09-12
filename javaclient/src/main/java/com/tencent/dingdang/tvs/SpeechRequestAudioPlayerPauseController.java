package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.TVSAudioPlayer.TVSSpeechListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * This class keeps track of running speech requests and whether the device is listening/speaking to
 * appropriately manage the pause state of the player.
 */
public class SpeechRequestAudioPlayerPauseController
        implements TVSSpeechListener, ExpectSpeechListener {
    private static final Logger log =
            LoggerFactory.getLogger(SpeechRequestAudioPlayerPauseController.class);
    private final TVSAudioPlayer audioPlayer;
    private Optional<CountDownLatch> outstandingDirectiveCount = Optional.empty();
    private Optional<Thread> resumeAudioThread = Optional.empty();
    private Optional<CountDownLatch> tvsSpeaking = Optional.empty();
    private Optional<CountDownLatch> tvsListening = Optional.empty();
    private volatile boolean speechRequestRunning = false;

    public SpeechRequestAudioPlayerPauseController(TVSAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        audioPlayer.registerTVSSpeechListener(this);
    }

    public void startSpeechRequest() {
        log.debug("Speech request started");
        tvsListening = Optional.of(new CountDownLatch(1));
        audioPlayer.interruptAllTVSOutput();
        resumeAudioThread.ifPresent(t -> t.interrupt());
        speechRequestRunning = true;
    }

    /**
     * Called when finished Listening
     */
    public void finishedListening() {
        log.debug("Finished listening to user speech");
        tvsListening.ifPresent(c -> c.countDown());
        if (!speechRequestRunning) {
            audioPlayer.resumeAllTVSOutput();
        }
    }

    /**
     * Called each time a directive is dispatched
     */
    public void dispatchDirective() {
        log.debug("Dispatching directive");
        outstandingDirectiveCount.ifPresent(c -> c.countDown());
    }

    @Override
    public void onTVSSpeechStarted() {
        log.debug("TVS speech started");
        tvsSpeaking = Optional.of(new CountDownLatch(1));
    }

    @Override
    public void onTVSSpeechFinished() {
        log.debug("TVS speech finished");
        tvsSpeaking.ifPresent(c -> c.countDown());
        if (!speechRequestRunning) {
            audioPlayer.resumeAllTVSOutput();
        }
    }

    @Override
    public void onExpectSpeechDirective() {
        tvsListening = Optional.of(new CountDownLatch(1));
    }

    /**
     * A speech request has been finished processing
     *
     * @param directiveCount
     *            the number of outstanding directives that correspond to the speech request that
     *            just finished
     */
    public void speechRequestProcessingFinished(int directiveCount) {
        log.debug("Finished processing speech request");
        resumeAudioThread.ifPresent(t -> t.interrupt());
        outstandingDirectiveCount = Optional.of(new CountDownLatch(directiveCount));
        resumeAudioThread = Optional.of(new Thread() {

            boolean isInterrupted = false;

            @Override
            public void run() {
                log.debug("Started resume audio thread");
                outstandingDirectiveCount.ifPresent(c -> awaitOnLatch(c));
                if (tvsListening.isPresent() || tvsSpeaking.isPresent()) {
                    tvsSpeaking.ifPresent(c -> awaitOnLatch(c));
                    tvsListening.ifPresent(c -> awaitOnLatch(c));
                }
                if (!isInterrupted) {
                    speechRequestRunning = false;
                    log.debug("Resuming all TVS output");
                    audioPlayer.resumeAllTVSOutput();
                }

            }

            private void awaitOnLatch(CountDownLatch latch) {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    // If another speech request is kicked off while we're processing the
                    // current request we expect this thread to be interrupted
                    isInterrupted = true;
                }
            }

        });
        resumeAudioThread.ifPresent(t -> t.start());

    }

}
