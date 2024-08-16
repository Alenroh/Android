package com.example.something;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationHolder> {

    private List<Vacation> vacations = new ArrayList<>();

    @NonNull
    @Override
    public VacationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_item, parent, false);
        return new VacationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationHolder holder, int position) {
        Vacation currentVacation = vacations.get(position);
        holder.textViewTitle.setText(currentVacation.getTitle());
        holder.textViewHotel.setText(currentVacation.getHotel());
        holder.textViewStartDate.setText(currentVacation.getStartDate());
        holder.textViewEndDate.setText(currentVacation.getEndDate());
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
        notifyDataSetChanged();
    }

    class VacationHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewHotel;
        private TextView textViewStartDate;
        private TextView textViewEndDate;

        public VacationHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewHotel = itemView.findViewById(R.id.text_view_hotel);
            textViewStartDate = itemView.findViewById(R.id.text_view_start_date);
            textViewEndDate = itemView.findViewById(R.id.text_view_end_date);
        }
    }
}
