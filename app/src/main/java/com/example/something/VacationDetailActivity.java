package com.example.something;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VacationDetailActivity extends AppCompatActivity {

    private VacationViewModel vacationViewModel;
    private EditText titleEditText;
    private EditText hotelEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private Vacation currentVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        // Initialize views
        titleEditText = findViewById(R.id.edit_text_title);
        hotelEditText = findViewById(R.id.edit_text_hotel);
        startDateEditText = findViewById(R.id.edit_text_start_date);
        endDateEditText = findViewById(R.id.edit_text_end_date);

        // Initialize calendars
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        // Date picker for start date
        startDateEditText.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                    VacationDetailActivity.this, startDateListener,
                    startDateCalendar.get(Calendar.YEAR),
                    startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        // Date picker for end date
        endDateEditText.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(
                    VacationDetailActivity.this, endDateListener,
                    endDateCalendar.get(Calendar.YEAR),
                    endDateCalendar.get(Calendar.MONTH),
                    endDateCalendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        // RecyclerView for excursions
        RecyclerView recyclerView = findViewById(R.id.recycler_view_excursions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ExcursionAdapter adapter = new ExcursionAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel setup
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Retrieve vacation data (if editing an existing vacation)
        Intent intent = getIntent();
        int vacationId = intent.getIntExtra("vacation_id", -1);
        if (vacationId != -1) {
            vacationViewModel.getVacationById(vacationId).observe(this, vacation -> {
                if (vacation != null) {
                    currentVacation = vacation;
                    titleEditText.setText(vacation.getTitle());
                    hotelEditText.setText(vacation.getHotel());
                    startDateEditText.setText(vacation.getStartDate());
                    endDateEditText.setText(vacation.getEndDate());

                    // Load excursions
                    vacationViewModel.getExcursionsForVacation(vacationId).observe(this, adapter::setExcursions);
                }
            });
        }

        // Save button
        findViewById(R.id.button_save).setOnClickListener(v -> saveVacation());

        // Delete button
        findViewById(R.id.button_delete).setOnClickListener(v -> {
            if (currentVacation != null) {
                vacationViewModel.delete(currentVacation);
                finish();
            }
        });

        // Add Excursion button
        findViewById(R.id.button_add_excursion).setOnClickListener(v -> {
            Intent addExcursionIntent = new Intent(VacationDetailActivity.this, ExcursionDetailActivity.class);
            addExcursionIntent.putExtra("vacation_id", vacationId);
            startActivity(addExcursionIntent);
        });
    }

    // Date listeners
    private final DatePickerDialog.OnDateSetListener startDateListener = (view, year, monthOfYear, dayOfMonth) -> {
        startDateCalendar.set(Calendar.YEAR, year);
        startDateCalendar.set(Calendar.MONTH, monthOfYear);
        startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel(startDateEditText, startDateCalendar);
    };

    private final DatePickerDialog.OnDateSetListener endDateListener = (view, year, monthOfYear, dayOfMonth) -> {
        endDateCalendar.set(Calendar.YEAR, year);
        endDateCalendar.set(Calendar.MONTH, monthOfYear);
        endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel(endDateEditText, endDateCalendar);
    };

    // Update the EditText with the selected date
    private void updateLabel(EditText editText, Calendar calendar) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    // Save vacation method
    private void saveVacation() {
        String title = titleEditText.getText().toString().trim();
        String hotel = hotelEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(hotel) ||
                !isDateValid(startDate) || !isDateValid(endDate) ||
                !isEndDateAfterStartDate(startDate, endDate)) {
            Toast.makeText(this, "Please provide valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentVacation == null) {
            currentVacation = new Vacation();
        }

        currentVacation.setTitle(title);
        currentVacation.setHotel(hotel);
        currentVacation.setStartDate(startDate);
        currentVacation.setEndDate(endDate);

        if (currentVacation.getId() == 0) {
            vacationViewModel.insert(currentVacation);
        } else {
            vacationViewModel.update(currentVacation);
        }

        setNotification(title, startDate, true);
        setNotification(title, endDate, false);

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

    private boolean isEndDateAfterStartDate(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return sdf.parse(endDate).after(sdf.parse(startDate));
        } catch (Exception e) {
            return false;
        }
    }

    // Set notifications for the vacation start and end dates
    private void setNotification(String title, String date, boolean isStart) {
        Intent intent = new Intent(this, VacationNotificationReceiver.class);
        intent.putExtra("vacation_title", title);
        intent.putExtra("message", isStart ? "Vacation starting: " + title : "Vacation ending: " + title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, isStart ? 1 : 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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