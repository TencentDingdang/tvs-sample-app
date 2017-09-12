package com.tencent.dingdang.tvs;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneLineFactory {
    // get the system default microphone
    public TargetDataLine getMicrophone() {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            Mixer m = AudioSystem.getMixer(mixerInfo);
            try {
                m.open();
                m.close();
            } catch (Exception e) {
                continue;
            }

            Line.Info[] lines = m.getTargetLineInfo();
            for (Line.Info li : lines) {
                try {
                    TargetDataLine temp = (TargetDataLine) AudioSystem.getLine(li);
                    if (temp != null) {
                        return temp;
                    }
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
}
