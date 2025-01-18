package com.example.doanchuyennghanh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanchuyennghanh.Activity.HerbDetail_Activity;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.R;

import java.util.ArrayList;

public class HerbListAdapter extends RecyclerView.Adapter<HerbListAdapter.ViewHolder> {

    ArrayList<Herb> items;
    Context context;

    public HerbListAdapter(Context context, ArrayList<Herb> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Herb herb = items.get(position);
        holder.herbTitle.setText(herb.getTenThuoc());
        holder.herbSubTitle.setText(herb.getCongDung());
        holder.herbTemperature.setText(herb.getTenKhoaHoc());
        Log.d("HerbListAdapter", "Binding herb: " + herb.getTenThuoc());
        // Sử dụng Glide để load ảnh từ URL
        Glide.with(context)
                .load(herb.getImage()) // URL của ảnh
                .into(holder.herbImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HerbDetail_Activity.class);
            intent.putExtra("selectedMedicine", herb);
            context.startActivity(intent);
        });

    }

    public void updateList(ArrayList<Herb> filteredItems) {
        this.items = filteredItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView herbTitle, herbSubTitle, herbTemperature;
        ImageView herbImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            herbTitle = itemView.findViewById(R.id.herbTitle);
            herbSubTitle = itemView.findViewById(R.id.herbSubTitle);
            herbTemperature = itemView.findViewById(R.id.herbTemperature);
            herbImage = itemView.findViewById(R.id.herbImage);
        }
    }
}
