package com.example.something;

import static com.example.something.StringDateFormatUtil.sdf;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Future;

public class ExcursionDetailActivity extends AppCompatActivity {

    final Calendar excursionCalendar = Calendar.getInstance();
    int excursionID = -1;  // Initialize as -1 to denote new excursions
    int vacationID;
    String title;
    Date excursionDate;
    EditText editExcursionTitle;
    Button excursionDateBtn;
    Repository repository;
    DatePickerDialog.OnDateSetListener dpdDate;

    TextWatcher txtWatcherTitle = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            title = s.toString();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        // Get data from intent
        title = getIntent().getStringExtra("title");
        vacationID = getIntent().getIntExtra("vacationID", -1);
        excursionID = getIntent().getIntExtra("excursionID", -1);  // Ensure the correct excursionID is retrieved
        excursionDate = (Date) getIntent().getSerializableExtra("excursion_date");

        editExcursionTitle = findViewById(R.id.editExcursionTitle);
        excursionDateBtn = findViewById(R.id.excursionDateBtn);
        repository = new Repository(getApplication());

        // Set initial values if editing
        if (title != null) {
            editExcursionTitle.setText(title);
        }

        if (excursionDate != null) {
            excursionCalendar.setTime(excursionDate);
            excursionDateBtn.setText(sdf.format(excursionDate));
        }

        editExcursionTitle.addTextChangedListener(txtWatcherTitle);

        // Date picker setup
        excursionDateBtn.setOnClickListener(v -> {
            new DatePickerDialog(ExcursionDetailActivity.this,
                    dpdDate,
                    excursionCalendar.get(Calendar.YEAR),
                    excursionCalendar.get(Calendar.MONTH),
                    excursionCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        dpdDate = (view, year, month, dayOfMonth) -> {
            excursionCalendar.set(Calendar.YEAR, year);
            excursionCalendar.set(Calendar.MONTH, month);
            excursionCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            excursionDate = excursionCalendar.getTime();
            excursionDateBtn.setText(sdf.format(excursionDate));
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.saveExcursion) {
            saveExcursion();
            return true;
        }

        if (item.getItemId() == R.id.deleteExcursion) {
            deleteExcursion();
            return true;
        }

        if (item.getItemId() == R.id.setAlarmExcursion) {
            setAlarm();  // Call the setAlarm method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveExcursion() {
        // Ensure the date is selected
        if (excursionDate == null) {
            Toast.makeText(ExcursionDetailActivity.this, "Date must be set before saving.", Toast.LENGTH_LONG).show();
            return;
        }

        // If editing, keep the current excursionID; otherwise, let Room auto-generate the ID
        Excursion excursion = new Excursion(excursionID == -1 ? 0 : excursionID, title, excursionDate, vacationID);

        // Insert or update based on the excursionID
        if (excursionID == -1) {
            // Insert a new excursion in a background thread
            Repository.dbExecutor.execute(() -> {
                try {
                    Future<Long> futureExcursionID = repository.insert(excursion);  // Insert and get the generated ID
                    long newExcursionID = futureExcursionID.get();  // This will block, but it's in a background thread
                    excursionID = (int) newExcursionID;

                    // Update the UI on the main thread
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Excursion added with ID: " + excursionID, Toast.LENGTH_SHORT).show();
                        finish();  // Close the activity
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error adding excursion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    e.printStackTrace();
                }
            });
        } else {
            // Update the existing excursion in a background thread
            Repository.dbExecutor.execute(() -> {
                repository.update(excursion);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Excursion updated with ID: " + excursionID, Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity
                });
            });
        }
    }

    private void deleteExcursion() {
        if (excursionID != -1) {
            repository.delete(new Excursion(excursionID, title, excursionDate, vacationID));
            Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Excursion not saved yet", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to set an alarm for the excursion
    private void setAlarm() {
        if (excursionDate != null) {
            Calendar excursionCalendar = Calendar.getInstance();
            excursionCalendar.setTime(excursionDate);

            // Call a utility function to set the alarm
            AlarmUtil.setAlarm(this, excursionCalendar, true, title);  // Assuming true means it's a start alarm
            Toast.makeText(this, "Alarm set for excursion: " + title, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Excursion date must be set before setting an alarm.", Toast.LENGTH_SHORT).show();
        }
    }
}
