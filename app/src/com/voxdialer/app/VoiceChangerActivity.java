package com.voxdialer.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class VoiceChangerActivity extends Activity {

    private AudioEngine audioEngine;
    private boolean isMonitoring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_changer);

        audioEngine = new AudioEngine();
        final Button btnLive = findViewById(R.id.btn_live_preview);

        btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMonitoring) {
                    audioEngine.start();
                    btnLive.setText("Stop Modulation");
                    btnLive.setBackgroundColor(0xFFFF3D00); // Red
                    isMonitoring = true;
                } else {
                    audioEngine.stop();
                    btnLive.setText("Start Live Monitor");
                    btnLive.setBackgroundColor(0xFF00E5FF); // Neon Blue
                    isMonitoring = false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioEngine != null) audioEngine.stop();
    }
}
