package com.voxdialer.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;

public class FakeCallActivity extends Activity {

    private boolean isRinging = true;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This would use a specific layout for incoming call
        setContentView(R.layout.activity_main); // Placeholder layout

        statusText = new TextView(this);
        statusText.setText("INCOMING ENCRYPTED CALL...");
        statusText.setTextColor(0xFF00E5FF);
        statusText.setGravity(17); // CENTER
        
        // Simulating logic
    }
}
