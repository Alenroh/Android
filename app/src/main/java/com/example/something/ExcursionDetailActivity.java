package com.example.something;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExcursionDetailActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText dateEditText;
    private Excursion currentExcursion;
    private VacationViewModel vacationViewModel;
    private Calendar dateCalendar;
    private int vacationId;
    private String vacationStartDate;
    private String vacationEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        titleEditText = findViewById(R.id.edit_text_title);
        dateEditText = findViewById(R.id.edit_text_date);

        dateCalendar = Calendar.getInstance();

        dateEditText.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                    ExcursionDetailActivity.this, dateSetListener,
                    dateCalendar.get(Calendar.YEAR),
                    dateCalendar.get(Calendar.MONTH),
                    dateCalendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        Intent intent = getIntent();
        vacationId = intent.getIntExtra("vacation_id", -1);
        vacationStartDate = intent.getStringExtra("vacation_start_date");
        vacationEndDate = intent.getStringExtra("vacation_end_date");

        int excursionId = intent.getIntExtra("excursion_id", -1);
        if (excursionId != -1) {
            vacationViewModel.getExcursionById(excursionId).observe(this, excursion -> {
                if (excursion != null) {
                    currentExcursion = excursion;
                    titleEditText.setText(excursion.getTitle());
                    dateEditText.setText(excursion.getDate());
                }
            });
        }

        findViewById(R.id.button_save_excursion).setOnClickListener(v -> saveExcursion());
        findViewById(R.id.button_delete_excursion).setOnClickListener(v -> deleteExcursion());
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
        dateCalendar.set(Calendar.YEAR, year);
        dateCalendar.set(Calendar.MONTH, monthOfYear);
        dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDateLabel();
    };

    private void updateDateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        dateEditText.setText(sdf.format(dateCalendar.getTime()));
    }

    private void saveExcursion() {
        String title = titleEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please enter a title and date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDateValid(date)) {
            Toast.makeText(this, "Please enter a valid date in the format yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDateWithinVacation(date)) {
            Toast.makeText(this, "Excursion date must be within the vacation period", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentExcursion == null) {
            currentExcursion = new Excursion();
            currentExcursion.setVacationId(vacationId);
        }

        currentExcursion.setTitle(title);
        currentExcursion.setDate(date);

        if (currentExcursion.getId() == 0) {
            vacationViewModel.insertExcursion(currentExcursion);
        } else {
            vacationViewModel.updateExcursion(currentExcursion);
        }

        setNotification(title, date);

        finish();
    }

    private void deleteExcursion() {
        if (currentExcursion != null) {
            vacationViewModel.deleteExcursion(currentExcursion);
        }
        finish();
    }

    private boolean isDateValid(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDateWithinVacation(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return !sdf.parse(date).before(sdf.parse(vacationStartDate)) && !sdf.parse(date).after(sdf.parse(vacationEndDate));
        } catch (Exception e) {
            return false;
        }
    }

    private void setNotification(String title, String date) {
        Intent intent = new Intent(this, ExcursionNotificationReceiver.class);
        intent.putExtra("excursion_title", title);
        intent.putExtra("message", "Excursion: " + title + " is scheduled for today");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            calendar.setTime(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
