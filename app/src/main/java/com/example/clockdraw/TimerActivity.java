package com.example.clockdraw;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;



public class TimerActivity extends AppCompatActivity {

    NumberPicker num_hour, num_min, num_second;
    Button btnSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.bottom_timer);
        bottomNavView.setOnApplyWindowInsetsListener(null);
        bottomNavView.setPadding(0, 0, 0, 0);
        bottomNavView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.bottom_timer) {
                return true;
            } else if (itemID == R.id.bottom_stopwatch) {
                startActivity(new Intent(getApplicationContext(), StopwatchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemID == R.id.bottom_alarm) {
                startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemID == R.id.bottom_clock) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else return false;
        });

        num_hour = findViewById(R.id.NP_hour);
        num_min = findViewById(R.id.NP_minute);
        num_second = findViewById(R.id.NP_second);
        num_hour.setMaxValue(23);
        num_hour.setMinValue(0);
        num_hour.setValue(0);
        num_min.setMaxValue(59);
        num_min.setMinValue(0);
        num_min.setValue(0);
        num_second.setMaxValue(59);
        num_second.setMinValue(1);
        num_second.setValue(1);

        btnSet = findViewById(R.id.btnSet);
        btnSet.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long hours = num_hour.getValue();
                long minutes = num_min.getValue();
                long seconds = num_second.getValue();
                long millisInput = seconds * 1000 + minutes * 60000 + hours * 3600000;
                Intent intent = new Intent(TimerActivity.this, TimerActivity2.class);
                intent.putExtra("time",millisInput);
                startActivity(intent);
            }
        });
    }
}