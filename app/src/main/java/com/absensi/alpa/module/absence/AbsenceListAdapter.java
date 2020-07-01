package com.absensi.alpa.module.absence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;

import java.util.ArrayList;
import java.util.List;

public class AbsenceListAdapter extends RecyclerView.Adapter<AbsenceListAdapter.ViewHolder> {

    private List<Item> items;

    {
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public AbsenceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceListAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.tvDayNumber.setText(item.getDayNumber());
        holder.tvDayWeek.setText(item.getDayWeek());
        holder.tvTimeCheckIn.setText(item.getTimeCheckIn());
        holder.tvTimeCheckOut.setText(item.getTimeCheckOut());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDayNumber, tvDayWeek, tvTimeCheckIn, tvTimeCheckOut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDayNumber = itemView.findViewById(R.id.tvDayNumber);
            tvDayWeek = itemView.findViewById(R.id.tvDayWeek);
            tvTimeCheckIn = itemView.findViewById(R.id.tvCheckIn);
            tvTimeCheckOut = itemView.findViewById(R.id.tvCheckOut);
        }
    }

    public static class Item {
        private String dayNumber;
        private String dayWeek;
        private String timeCheckIn;
        private String timeCheckOut;

        public String getDayNumber() {
            return dayNumber;
        }

        public void setDayNumber(String dayNumber) {
            this.dayNumber = dayNumber;
        }

        public String getDayWeek() {
            return dayWeek;
        }

        public void setDayWeek(String dayWeek) {
            this.dayWeek = dayWeek;
        }

        public String getTimeCheckIn() {
            return timeCheckIn;
        }

        public void setTimeCheckIn(String timeCheckIn) {
            this.timeCheckIn = timeCheckIn;
        }

        public String getTimeCheckOut() {
            return timeCheckOut;
        }

        public void setTimeCheckOut(String timeCheckOut) {
            this.timeCheckOut = timeCheckOut;
        }
    }

    public List<Item> getItems() {
        return items;
    }
}
