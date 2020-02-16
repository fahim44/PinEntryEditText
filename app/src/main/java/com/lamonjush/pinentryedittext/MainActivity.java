package com.lamonjush.pinentryedittext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PinEntryEditText pinEntryEditText = findViewById(R.id.pinEntryEditText);
        pinEntryEditText.setPinEntryListener(
                pin -> Toast.makeText(this, pin, Toast.LENGTH_SHORT).show());
    }
}
