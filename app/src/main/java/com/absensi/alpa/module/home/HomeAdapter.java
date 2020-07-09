package com.absensi.alpa.module.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<Item> items;

    {
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_absence, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.tvTitle.setText(item.getTitle());

        if (!item.getTime().equalsIgnoreCase("")) {
            SimpleDateFormat day = new SimpleDateFormat("EEEE", Locale.getDefault());
            SimpleDateFormat date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            holder.tvSubtitleDay.setText(day.format(new Date()));
            holder.tvSubtitleDate.setText(date.format(new Date()));
            try {
                holder.tvTime.setText(timeFormat.format(Objects.requireNonNull(time.parse(item.getTime()))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.llMainView.setVisibility(View.VISIBLE);
            holder.tvNoData.setVisibility(View.GONE);
        } else {
            holder.llMainView.setVisibility(View.GONE);
            holder.tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvSubtitleDay, tvSubtitleDate, tvTime, tvNoData;
        private LinearLayout llMainView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitleDay = itemView.findViewById(R.id.tvSubtitleDay);
            tvSubtitleDate = itemView.findViewById(R.id.tvSubtitleDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNoData = itemView.findViewById(R.id.tvNoData);
            llMainView = itemView.findViewById(R.id.llMainView);
        }
    }

    public static class Item {
        private String title;
        private String time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
