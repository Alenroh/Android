package com.example.something;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private List<Excursion> mExcursions;
    private final LayoutInflater mInflater;
    private OnItemClickListener listener;

    public ExcursionAdapter(Context context, List<Excursion> excursions) {
        this.mExcursions = excursions;
        this.mInflater = LayoutInflater.from(context);
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView1;
        private final TextView excursionItemView2;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView1 = itemView.findViewById(R.id.textViewExcursionItemView1);
            excursionItemView2 = itemView.findViewById(R.id.textViewExcursionItemView2);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(mExcursions.get(position));  // Trigger click event with excursion
                }
            });

            // Set long click listener to show the Excursion ID in a toast message
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mExcursions != null) {
                    Excursion excursion = mExcursions.get(position);
                    Toast.makeText(itemView.getContext(), "Excursion ID: " + excursion.getExcursionID(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
        }
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (mExcursions != null && !mExcursions.isEmpty()) {
            Excursion currentExcursion = mExcursions.get(position);
            holder.excursionItemView1.setText(currentExcursion.getTitle());  // Display excursion title

            // Display formatted excursion date in MM/dd/yyyy format
            if (currentExcursion.getExcursion_date() != null) {
                holder.excursionItemView2.setText(sdf.format(currentExcursion.getExcursion_date()));
            } else {
                holder.excursionItemView2.setText("No Date");
            }
        } else {
            holder.excursionItemView1.setText("No Excursion Available");
            holder.excursionItemView2.setText("No Date");
        }
    }

    public void setExcursions(List<Excursion> excursions) {
        this.mExcursions = excursions;
        notifyDataSetChanged();  // Refresh RecyclerView when the list is updated
    }

    @Override
    public int getItemCount() {
        return mExcursions != null ? mExcursions.size() : 0;
    }

    // Define interface for click events
    public interface OnItemClickListener {
        void onItemClick(Excursion excursion);
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
