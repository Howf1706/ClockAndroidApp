package com.example.clockdraw;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alarm), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.bottom_alarm);
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

        FloatingActionButton addAlarmButton = (FloatingActionButton) findViewById(R.id.addButton);
        RecyclerView mRecyclerview = (RecyclerView) findViewById(R.id.alarmList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<Model> dataHolder = new ArrayList<Model>();                                               //Array list to add reminders and display in recyclerview
        myAdapter adapter;

        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);
            }
        });
        Cursor cursor = new dbManager(getApplicationContext()).readallreminders();                  //Cursor To Load data From the database
        while (cursor.moveToNext()) {
            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            dataHolder.add(model);
        }

        adapter = new myAdapter(dataHolder);
        mRecyclerview.setAdapter(adapter);
    }

//    private void showDialog() {
//        // Show dialog to add alarm
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.bottom_dialog);
//
//        TextView cancelButton = dialog.findViewById(R.id.cancelButton);
//        TextView doneButton = dialog.findViewById(R.id.doneButton);
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        doneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Toast toast = Toast.makeText(AlarmActivity.this, "Alarm added", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 650);
//                toast.show();
//
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//    }
}