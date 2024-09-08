package com.example.something;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Collections;
import java.util.List;

public class VacationListActivity extends AppCompatActivity {

    private Repository repository;
    private VacationAdapter vacationAdapter;

    // Launcher to handle result when returning from VacationDetailActivity
    private final ActivityResultLauncher<Intent> vacationDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Vacation was added or updated, refresh the list
                    repository.getAllVacations().observe(this, vacations -> {
                        vacationAdapter.setVacations(vacations);
                        vacationAdapter.notifyDataSetChanged();
                    });
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        // Initialize repository and adapter
        repository = new Repository(getApplication());

        // Pass both context and ActivityResultLauncher to the adapter
        vacationAdapter = new VacationAdapter(this, vacationDetailLauncher);

        // Setup FloatingActionButton for adding a new vacation
        FloatingActionButton fab = findViewById(R.id.vacationListFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
                intent.putExtra("vacationID", -1);  // Signal Add Vacation
                vacationDetailLauncher.launch(intent);  // Start activity for result
            }
        });

        // Setup RecyclerView and adapter
        RecyclerView recyclerView = findViewById(R.id.vacationListRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vacationAdapter);

        // Observe LiveData from the repository and update the adapter when data changes
        repository.getAllVacations().observe(this, new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                if (vacations != null) {
                    Log.d("VacationListActivity", "Number of vacations: " + vacations.size());
                    vacationAdapter.setVacations(vacations);  // Update the adapter with new data
                } else {
                    vacationAdapter.setVacations(Collections.emptyList());  // Clear the adapter if the list is null
                }
                vacationAdapter.notifyDataSetChanged();   // Notify the adapter to refresh the list
            }
        });

        // Handle window insets (for modern UI layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
