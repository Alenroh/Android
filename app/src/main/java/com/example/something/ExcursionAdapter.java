package com.example.something;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionHolder> {

    private List<Excursion> excursions = new ArrayList<>();

    @NonNull
    @Override
    public ExcursionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_item, parent, false);
        return new ExcursionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionHolder holder, int position) {
        Excursion currentExcursion = excursions.get(position);
        holder.textViewTitle.setText(currentExcursion.getTitle());
        holder.textViewDate.setText(currentExcursion.getDate());
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }

    class ExcursionHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDate;

        public ExcursionHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDate = itemView.findViewById(R.id.text_view_date);
        }
    }
}
