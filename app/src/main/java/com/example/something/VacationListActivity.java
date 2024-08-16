package com.example.something;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VacationListActivity extends AppCompatActivity {

    private VacationViewModel vacationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_vacations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Setup adapter
        VacationAdapter adapter = new VacationAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel setup
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        vacationViewModel.getAllVacations().observe(this, adapter::setVacations);

        // Handle item clicks for editing a vacation
        adapter.setOnItemClickListener(vacation -> {
            Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
            intent.putExtra("vacation_id", vacation.getId());
            startActivity(intent);
        });

        // Handle "Add Vacation" button click
        findViewById(R.id.button_add_vacation).setOnClickListener(v -> {
            Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
            startActivity(intent);
        });
    }
}
