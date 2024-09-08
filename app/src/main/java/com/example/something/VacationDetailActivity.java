package com.example.something;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

public class VacationDetailActivity extends AppCompatActivity {

    private EditText editTitle, editHotel;
    private Button startDateButton, endDateButton;
    private RecyclerView excursionsRecView;
    private String startDate, endDate;
    private int vacationID = -1;
    private Repository repository;
    private ExcursionAdapter excursionAdapter;
    private List<Excursion> excursions = new ArrayList<>();

    // Formatter for MM/DD/YYYY format
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        // Initialize views
        editTitle = findViewById(R.id.editTitleText);
        editHotel = findViewById(R.id.editHotelText);
        startDateButton = findViewById(R.id.start_date_btn);
        endDateButton = findViewById(R.id.end_date_btn);
        excursionsRecView = findViewById(R.id.excursionsRecView);

        repository = new Repository(getApplication());

        // Initialize the adapter with an empty list to prevent null issues
        excursionAdapter = new ExcursionAdapter(this, new ArrayList<>());
        excursionsRecView.setLayoutManager(new LinearLayoutManager(this));
        excursionsRecView.setAdapter(excursionAdapter);

        // Load the intent data
        setupIntentData();
        setupDatePicker();
        setupExcursionClickListener();
    }

    private void setupIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            vacationID = intent.getIntExtra("vacationID", -1);
            String title = intent.getStringExtra("title");
            String hotel = intent.getStringExtra("hotel");
            startDate = intent.getStringExtra("start_date");
            endDate = intent.getStringExtra("end_date");

            if (title != null) editTitle.setText(title);
            if (hotel != null) editHotel.setText(hotel);
            if (startDate != null) startDateButton.setText(startDate);
            if (endDate != null) endDateButton.setText(endDate);

            loadExcursions();  // Load excursions when intent data is set
        }
    }

    private void setupDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        startDateButton.setOnClickListener(v -> {
            new DatePickerDialog(VacationDetailActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                startDate = sdf.format(calendar.getTime());  // Save only the date
                startDateButton.setText(startDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        endDateButton.setOnClickListener(v -> {
            new DatePickerDialog(VacationDetailActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                endDate = sdf.format(calendar.getTime());  // Save only the date
                endDateButton.setText(endDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupExcursionClickListener() {
        excursionAdapter.setOnItemClickListener(excursion -> {
            Intent intent = new Intent(VacationDetailActivity.this, ExcursionDetailActivity.class);
            intent.putExtra("excursionID", excursion.getExcursionID());
            intent.putExtra("title", excursion.getTitle());
            intent.putExtra("excursion_date", excursion.getExcursion_date());
            intent.putExtra("vacationID", excursion.getVacationID());
            startActivity(intent);
        });
    }

    // Load excursions and set them in the adapter
    private void loadExcursions() {
        if (vacationID != -1) {
            repository.getAllExcursionsByVacationId(vacationID).observe(this, loadedExcursions -> {
                if (loadedExcursions != null) {
                    excursions = loadedExcursions;
                    excursionAdapter.setExcursions(loadedExcursions);
                } else {
                    excursionAdapter.setExcursions(new ArrayList<>());  // Set an empty list if no data
                }
                excursionAdapter.notifyDataSetChanged();  // Ensure the RecyclerView is refreshed
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (vacationID == -1) {
            getMenuInflater().inflate(R.menu.menu_add_vacation, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit_vacation, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveVacation) {
            saveVacation();
            return true;
        } else if (id == R.id.deleteVacation) {
            if (vacationID != -1) {
                deleteVacation();
            } else {
                Toast.makeText(this, "Vacation must be saved before deletion.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.setAlarm) {
            setAlarm();
            return true;
        } else if (id == R.id.copyVacationData) {
            copyVacationData();
            return true;
        } else if (id == R.id.addExcursion) {
            addExcursion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveVacation() {
        String title = editTitle.getText().toString();
        String hotel = editHotel.getText().toString();

        if (title.isEmpty() || hotel.isEmpty() || startDate == null || endDate == null) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Convert the string dates back to Date objects
            Date startDateParsed = sdf.parse(startDate);
            Date endDateParsed = sdf.parse(endDate);

            // Check if the start date is after or equal to the end date
            if (startDateParsed.after(endDateParsed)) {
                Toast.makeText(this, "Start date cannot be after end date.", Toast.LENGTH_LONG).show();
                return;
            }

            // Check if any excursions fall outside the vacation's date range
            for (Excursion excursion : excursions) {
                Date excursionDate = excursion.getExcursion_date();
                if (excursionDate.before(startDateParsed) || excursionDate.after(endDateParsed)) {
                    Toast.makeText(this, "Excursion '" + excursion.getTitle() + "' falls outside of the vacation dates.", Toast.LENGTH_LONG).show();
                    return;  // Prevent saving if an excursion is outside the date range
                }
            }

            // If it's a new vacation, don't pass an ID (let Room generate it)
            Vacation vacation = new Vacation(vacationID == -1 ? 0 : vacationID, title, hotel, startDateParsed, endDateParsed);

            Repository.dbExecutor.execute(() -> {
                try {
                    if (vacationID == -1) {
                        // Insert new vacation and get the generated ID
                        Future<Long> futureVacationID = repository.insert(vacation);
                        long newVacationID = futureVacationID.get();  // This will block, but it's safe as it's in a background thread
                        vacationID = (int) newVacationID;
                    } else {
                        // Update existing vacation
                        repository.update(vacation);
                    }

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        String message = vacationID == -1 ? "Vacation updated" : "Vacation saved with ID: " + vacationID;
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        refreshVacationList();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error saving vacation: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format.", Toast.LENGTH_LONG).show();
        }
    }

    private void refreshVacationList() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();  // Close the activity and notify the calling activity
    }

    private void deleteVacation() {
        if (vacationID != -1) {
            // Check if there are any associated excursions
            if (excursions != null && !excursions.isEmpty()) {
                // Show warning and prevent deletion
                runOnUiThread(() -> Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_LONG).show());
                return;  // Prevent deletion
            }

            Repository.dbExecutor.execute(() -> {
                try {
                    repository.deleteVacationById(vacationID);
                    runOnUiThread(() -> showToastAndRefresh("Vacation deleted"));
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Error deleting vacation: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        } else {
            Toast.makeText(this, "Vacation must be saved before deletion.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showToastAndRefresh(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        refreshVacationList();
    }

    private void setAlarm() {
        String title = editTitle.getText().toString();
        try {
            if (startDate != null) {
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(sdf.parse(startDate));  // Convert the string date back to Date
                AlarmUtil.setAlarm(this, startCalendar, true, title);
            }
            if (endDate != null) {
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(sdf.parse(endDate));  // Convert the string date back to Date
                AlarmUtil.setAlarm(this, endCalendar, false, title);
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Error setting alarm: Invalid date", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyVacationData() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String vacationDetails = "Vacation: " + editTitle.getText().toString() +
                "\nHotel: " + editHotel.getText().toString() +
                "\nStart Date: " + startDate +
                "\nEnd Date: " + endDate;

        ClipData clip = ClipData.newPlainText("Vacation Details", vacationDetails);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Vacation details copied", Toast.LENGTH_SHORT).show();
    }

    private void addExcursion() {
        Intent intent = new Intent(VacationDetailActivity.this, ExcursionDetailActivity.class);
        intent.putExtra("vacationID", vacationID);
        startActivity(intent);
    }
}
