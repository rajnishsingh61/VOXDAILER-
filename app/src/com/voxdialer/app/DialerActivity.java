package com.voxdialer.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DialerActivity extends Activity {

    private TextView phoneDisplay;
    private StringBuilder currentNumber = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        phoneDisplay = findViewById(R.id.phone_display);
        ImageButton btnCall = findViewById(R.id.btn_call);
        ImageButton btnDelete = findViewById(R.id.btn_delete);

        // Bind all Buttons in the layout to keypad listener
        bindKeypad(getWindow().getDecorView());

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeCall();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNumber.length() > 0) {
                    currentNumber.deleteCharAt(currentNumber.length() - 1);
                    phoneDisplay.setText(currentNumber.toString());
                }
            }
        });

        btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentNumber.setLength(0);
                phoneDisplay.setText("");
                return true;
            }
        });
    }

    private void bindKeypad(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                bindKeypad(v.getChildAt(i));
            }
        } else if (view instanceof Button) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentNumber.append(((Button)v).getText().toString());
                    phoneDisplay.setText(currentNumber.toString());
                }
            });
        }
    }

    private void placeCall() {
        String number = currentNumber.toString();
        if (number.isEmpty()) return;

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            // Start dialer intent instead as fallback
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + number));
            startActivity(dialIntent);
        }
    }
}
