package naillibip.firstapp.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import naillibip.firstapp.weather.R;
import naillibip.firstapp.weather.TempHistoryCard;

public class TempHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TempHistoryRecyclerViewAdapter.TempHistoryViewHolder> {

    private ArrayList<TempHistoryCard> data = new ArrayList<>();
    private Context context;

    public TempHistoryRecyclerViewAdapter(ArrayList<TempHistoryCard> data) {
        if(data != null) {
            this.data = data;
        }
    }

    @NonNull
    @Override
    public TempHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.temp_history_item, parent, false);
        return new TempHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempHistoryViewHolder holder, int position) {
        holder.dateTextView.setText(data.get(position).date);
        holder.tempTextView.setText(String.valueOf(data.get(position).temp).concat("°C"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TempHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView tempTextView;

        TempHistoryViewHolder(View view) {
            super(view);
            dateTextView = itemView.findViewById(R.id.dateOnCard);
            tempTextView = itemView.findViewById(R.id.tempOnCard);
        }
    }
}
