package com.example.atmika;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder> {

    private List<Prediction> predictionList;

    public PredictionAdapter(List<Prediction> predictionList) {
        this.predictionList = predictionList;
    }

    @NonNull
    @Override
    public PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_prediction, parent, false);
        return new PredictionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionViewHolder holder, int position) {
        Prediction prediction = predictionList.get(position);
        holder.locationTextView.setText("Location: " + prediction.getLocation());
        holder.sqftTextView.setText("Sqft: " + prediction.getSqft());
        holder.bhkTextView.setText("BHK: " + prediction.getBhk());
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        holder.priceTextView.setText("Price: " + format.format(prediction.getFinalPrice()));
        holder.dateTextView.setText("Date: " + prediction.getDate());
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }

    public static class PredictionViewHolder extends RecyclerView.ViewHolder {
        public TextView locationTextView, sqftTextView, bhkTextView, priceTextView, dateTextView;

        public PredictionViewHolder(View view) {
            super(view);
            locationTextView = view.findViewById(R.id.locationTextView);
            sqftTextView = view.findViewById(R.id.sqftTextView);
            bhkTextView = view.findViewById(R.id.bhkTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
        }
    }
}
