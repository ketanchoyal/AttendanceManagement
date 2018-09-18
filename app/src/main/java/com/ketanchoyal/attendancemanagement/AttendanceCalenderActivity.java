package com.ketanchoyal.attendancemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendanceCalenderActivity extends AppCompatActivity {

    private CalendarView mAttendanceHistoryCalender;
    private String Enroll_coll = "Enrollment No Loading..";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_calender);

        Enroll_coll = getIntent().getStringExtra("enroll");

        mAttendanceHistoryCalender = findViewById(R.id.attendance_calender);

        mAttendanceHistoryCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {

                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(year-1900,month,dayOfMonth));
                String Month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date(year-1900,month,dayOfMonth));
                String Year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date(year-1900,month,dayOfMonth));

                Toast.makeText(getApplicationContext(), Month+" "+Year, Toast.LENGTH_LONG).show();

                Intent historyIntent = new Intent(AttendanceCalenderActivity.this,AttendanceHistoryActivity.class);
                historyIntent.putExtra("date",date);
                historyIntent.putExtra("Month",Month);
                historyIntent.putExtra("Year",Year);
                historyIntent.putExtra("enroll",Enroll_coll);
                startActivity(historyIntent);

            }
        });
    }
}
