package com.example.something;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private final Context context;
    private List<Vacation> mVacations = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final ActivityResultLauncher<Intent> vacationDetailLauncher;

    // SimpleDateFormat for displaying only dates (no time)
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public VacationAdapter(Context context, ActivityResultLauncher<Intent> launcher) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.vacationDetailLauncher = launcher; // Pass the ActivityResultLauncher from the calling activity
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationTitleView;
        private final TextView vacationHotelView;
        private final TextView vacationStartDateView;
        private final TextView vacationEndDateView;

        private VacationViewHolder(View itemView) {
            super(itemView);
            vacationTitleView = itemView.findViewById(R.id.vacationTitle);
            vacationHotelView = itemView.findViewById(R.id.vacationHotel);
            vacationStartDateView = itemView.findViewById(R.id.vacationStartDate);
            vacationEndDateView = itemView.findViewById(R.id.vacationEndDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mVacations != null && !mVacations.isEmpty()) {
                    final Vacation currentVacation = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetailActivity.class);
                    intent.putExtra("vacationID", currentVacation.getVacationID());
                    intent.putExtra("title", currentVacation.getTitle());
                    intent.putExtra("hotel", currentVacation.getHotel());
                    intent.putExtra("start_date", sdf.format(currentVacation.getStart_date()));  // Format start_date as a date
                    intent.putExtra("end_date", sdf.format(currentVacation.getEnd_date()));  // Format end_date as a date

                    // Use the ActivityResultLauncher to launch VacationDetailActivity
                    vacationDetailLauncher.launch(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (mVacations != null && !mVacations.isEmpty()) {
            Vacation currentVacation = mVacations.get(position);
            holder.vacationTitleView.setText("Title: " + currentVacation.getTitle());
            holder.vacationHotelView.setText("Hotel: " + currentVacation.getHotel());
            holder.vacationStartDateView.setText("Start: " + sdf.format(currentVacation.getStart_date()));  // Display only date
            holder.vacationEndDateView.setText("End: " + sdf.format(currentVacation.getEnd_date()));  // Display only date
        } else {
            holder.vacationTitleView.setText("No Vacation Title");
            holder.vacationHotelView.setText("");
            holder.vacationStartDateView.setText("");
            holder.vacationEndDateView.setText("");
        }
    }

    public void setVacations(List<Vacation> vacations) {
        this.mVacations = (vacations != null) ? vacations : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mVacations != null) ? mVacations.size() : 0;
    }
}
