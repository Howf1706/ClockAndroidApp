package com.example.clockdraw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView displayDate = findViewById(R.id.textView2);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        String fullDate = fullDateFormat.format(calendar.getTime());
        displayDate.setText(fullDate);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.bottom_clock);
        bottomNavView.setOnApplyWindowInsetsListener(null);
        bottomNavView.setPadding(0,0,0,0);
        bottomNavView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.bottom_timer) {
                startActivity(new Intent(getApplicationContext(), TimerActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemID == R.id.bottom_stopwatch) {
                startActivity(new Intent(getApplicationContext(), StopwatchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemID == R.id.bottom_alarm) {
                startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemID == R.id.bottom_clock) {
                return true;
            }
            else return false;
        });
    }
}