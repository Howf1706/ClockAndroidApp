package com.example.clockdraw;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import java.util.Locale;

public class TimerActivity2 extends AppCompatActivity {
    TextView text_display;
    Button btnPause_Con, btnReset;
    boolean TimerRunning;
    private CountDownTimer mCountDownTimer;

    private long mTimeLeftMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TimerRunning = true;
        text_display = findViewById(R.id.countdownTextView);
        btnReset = findViewById(R.id.btn_Reset);
        btnPause_Con = findViewById(R.id.btnPause_Con);
        Intent intent = getIntent();
        mTimeLeftMillis = intent.getLongExtra("time", 0);
        mCountDownTimer = new CountDownTimer(mTimeLeftMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                TimerRunning = false;
                finish();
            }
        }.start();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPause_Con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimerRunning)
                {
                    pauseTimer();
                }else{
                    ContinueTime();
                }
            }
        });
    }

    private void ContinueTime() {
        mCountDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish()
            {
                TimerRunning = false;
                finish();
            }
        }.start();
        TimerRunning = true;
        btnPause_Con.setText("Pause");
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        TimerRunning = false;
        btnPause_Con.setText("Resume");
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftMillis / 1000 / 3600);
        int minutes = (int) ((mTimeLeftMillis / 1000) % 3600) / 60;
        int seconds = (int) ((mTimeLeftMillis / 1000) % 60);
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
        text_display.setText(timeLeftFormatted);
    }
}