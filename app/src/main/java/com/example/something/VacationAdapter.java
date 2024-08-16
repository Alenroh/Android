package com.example.something;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacations = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation currentVacation = vacations.get(position);
        holder.textViewTitle.setText(currentVacation.getTitle());
        holder.textViewHotel.setText(currentVacation.getHotel());
        holder.textViewStartDate.setText(currentVacation.getStartDate());
        holder.textViewEndDate.setText(currentVacation.getEndDate());

        // Set up click listener for the Edit button
        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(currentVacation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vacations != null ? vacations.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
        notifyDataSetChanged();
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewHotel;
        private TextView textViewStartDate;
        private TextView textViewEndDate;
        private Button buttonEdit;

        public VacationViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewHotel = itemView.findViewById(R.id.text_view_hotel);
            textViewStartDate = itemView.findViewById(R.id.text_view_start_date);
            textViewEndDate = itemView.findViewById(R.id.text_view_end_date);
            buttonEdit = itemView.findViewById(R.id.button_edit_vacation);

            // Handle itemView click for entire row
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(vacations.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Vacation vacation);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
