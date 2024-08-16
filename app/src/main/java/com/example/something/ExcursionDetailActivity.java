package com.example.something;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ExcursionDetailActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private Excursion currentExcursion;
    private VacationViewModel vacationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        descriptionEditText = findViewById(R.id.edit_text_description);

        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Retrieve excursion data if editing an existing excursion
        Intent intent = getIntent();
        int excursionId = intent.getIntExtra("excursion_id", -1);
        if (excursionId != -1) {
            vacationViewModel.getExcursionById(excursionId).observe(this, excursion -> {
                if (excursion != null) {
                    currentExcursion = excursion;
                    descriptionEditText.setText(excursion.getDescription());
                }
            });
        }

        // Save button logic
        findViewById(R.id.button_save_excursion).setOnClickListener(v -> saveExcursion());
    }

    private void saveExcursion() {
        String description = descriptionEditText.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentExcursion == null) {
            currentExcursion = new Excursion();
        }

        currentExcursion.setDescription(description);
        currentExcursion.setVacationId(getIntent().getIntExtra("vacation_id", -1));

        if (currentExcursion.getId() == 0) {
            vacationViewModel.insertExcursion(currentExcursion);
        } else {
            vacationViewModel.updateExcursion(currentExcursion);
        }

        finish();
    }
}
