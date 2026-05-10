package com.voxdialer.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.Manifest;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends Activity {

    private ImageButton btnDialer, btnContacts, btnVoice, btnFake, btnSettings;
    private AdView mAdView;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        checkDefaultDialer();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        initViews();
        setupListeners();
        
        // Start with Dialer active
        setTabActive(btnDialer);
    }

    private void initViews() {
        btnDialer = findViewById(R.id.btn_dialer);
        btnContacts = findViewById(R.id.btn_contacts);
        btnVoice = findViewById(R.id.btn_voice_changer);
        btnFake = findViewById(R.id.btn_fake_call);
        btnSettings = findViewById(R.id.btn_settings);
    }

    private void setupListeners() {
        View.OnClickListener navListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNavColors();
                setTabActive((ImageButton) v);
                
                int id = v.getId();
                if (id == R.id.btn_dialer) {
                    Toast.makeText(MainActivity.this, "Dialer Protocol Initialized", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.btn_voice_changer) {
                     Toast.makeText(MainActivity.this, "Voice Modulation Active", Toast.LENGTH_SHORT).show();
                }
                // Fragment switching logic would go here in a fragment-based app.
                // For simplified AIDE project, we manage state in the container.
            }
        };

        btnDialer.setOnClickListener(navListener);
        btnContacts.setOnClickListener(navListener);
        btnVoice.setOnClickListener(navListener);
        btnFake.setOnClickListener(navListener);
        btnSettings.setOnClickListener(navListener);
    }

    private void resetNavColors() {
        int inactiveColor = 0xFF8E8E8E;
        btnDialer.setColorFilter(inactiveColor);
        btnContacts.setColorFilter(inactiveColor);
        btnVoice.setColorFilter(inactiveColor);
        btnFake.setColorFilter(inactiveColor);
        btnSettings.setColorFilter(inactiveColor);
    }

    private void setTabActive(ImageButton btn) {
        btn.setColorFilter(0xFF00E5FF); // Neon Blue
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG
            };

            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
            if (!telecomManager.getDefaultDialerPackage().equals(getPackageName())) {
                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                        .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int res : grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Permissions required for Dialer Protocol", Toast.LENGTH_LONG).show();
            }
        }
    }
}
