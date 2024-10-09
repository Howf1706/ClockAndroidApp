package com.example.clockdraw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.MessageFormat;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {

    TextView textView;
    Button start, stop, reset;
    int seconds, minutes, milliSeconds;
    long milliseconds, startTime, timeBuff, updateTime = 0L;
    Handler handler;
    private final Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            milliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + milliseconds;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);

            textView.setText(MessageFormat.format("{0}:{1}:{2}", minutes, String.format(Locale.getDefault(),"%02d", seconds), String.format(Locale.getDefault(), "%02d", milliSeconds)));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stopwatch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView3);
        reset = findViewById(R.id.reset);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);

        handler = new Handler(Looper.getMainLooper());

        start.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    reset.setEnabled(false);
                    start.setEnabled(false);
                    stop.setEnabled(true);
             }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += milliseconds;
                handler.removeCallbacks(updateTimerThread);
                reset.setEnabled(true);
                start.setEnabled(true);
                stop.setEnabled(false);
                start.setText("Continue");
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff = 0L;
                startTime = 0L;
                updateTime = 0L;
                milliseconds = 0L;
                seconds = 0;
                minutes = 0;
                milliSeconds = 0;
                textView.setText("00:00:00");
                start.setText("Start");
                reset.setEnabled(false);
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });

        textView.setText("00:00:00");

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.bottom_stopwatch);
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
                return true;
            }
            else if (itemID == R.id.bottom_alarm) {
                startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemID == R.id.bottom_clock) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else return false;
        });
    }
}