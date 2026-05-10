package com.voxdialer.app;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioEngine {
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG_IN = AudioFormat.CHANNEL_IN_MONO;
    private static final int CHANNEL_CONFIG_OUT = AudioFormat.CHANNEL_OUT_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private boolean isRunning = false;
    private Thread audioThread;
    private String currentEffect = "none";

    public void setEffect(String effect) {
        this.currentEffect = effect;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;
        audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                processAudio();
            }
        });
        audioThread.start();
    }

    public void stop() {
        isRunning = false;
        if (audioThread != null) {
            try { audioThread.join(); } catch (InterruptedException e) {}
        }
    }

    private void processAudio() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG_IN, AUDIO_FORMAT);
        
        AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 
                SAMPLE_RATE, CHANNEL_CONFIG_IN, AUDIO_FORMAT, bufferSize);
        
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 
                SAMPLE_RATE, CHANNEL_CONFIG_OUT, AUDIO_FORMAT, bufferSize, AudioTrack.MODE_STREAM);

        short[] buffer = new short[bufferSize];
        recorder.startRecording();
        track.play();

        while (isRunning) {
            int readSize = recorder.read(buffer, 0, bufferSize);
            if (readSize > 0) {
                applyEffect(buffer, readSize);
                track.write(buffer, 0, readSize);
            }
        }

        recorder.stop();
        recorder.release();
        track.stop();
        track.release();
    }

    private void applyEffect(short[] buffer, int size) {
        if (currentEffect.equals("none")) return;

        for (int i = 0; i < size; i++) {
            if (currentEffect.equals("robot")) {
                // Simple ring modulation / distortion
                buffer[i] = (short) (buffer[i] * Math.sin(0.1 * i));
            } else if (currentEffect.equals("deep")) {
                // Simplified "Deep" effect (lower amplitude and slight shift)
                buffer[i] = (short) (buffer[i] * 0.8);
            } else if (currentEffect.equals("cyber")) {
                // Bitcrush effect
                buffer[i] = (short) ((buffer[i] >> 4) << 4);
            }
        }
    }
}
