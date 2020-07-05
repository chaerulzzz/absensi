package com.absensi.alpa.module.request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;

import java.util.ArrayList;
import java.util.List;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.ViewHolder> {

    private List<Item> items;
    private RequestInterface onItemClick;

    public RequestItemAdapter(RequestInterface onItemClick) {
        this.onItemClick = onItemClick;
    }

    {
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RequestItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvStatus.setText(item.getStatus());
        holder.tvPeriod.setText(item.getPeriod());
        holder.tvCreated.setText(item.getCreated());

        holder.itemView.setOnClickListener(view -> onItemClick.onRequestClick(position, item.getId(), item.getTitle()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvPeriod, tvCreated, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvCreated = itemView.findViewById(R.id.tvPerson);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    public static class Item {
        private String id;
        private String title;
        private String period;
        private String created;
        private String status;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public interface RequestInterface {
        void onRequestClick(Integer position, String request_id, String type);
    }
}
