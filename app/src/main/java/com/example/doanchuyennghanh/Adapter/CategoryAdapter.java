package com.example.doanchuyennghanh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanchuyennghanh.Activity.HerbDetail_Activity;
import com.example.doanchuyennghanh.Activity.HomeActivity;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.Domain.PlantPart;
import com.example.doanchuyennghanh.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<PlantPart> categoryList;
    private Context context;

    public CategoryAdapter(Context context, List<PlantPart> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        PlantPart plantPart = categoryList.get(position);
        holder.catNameTxt.setText(plantPart.getTenBoPhanDung());

        switch (position) {
            case 0:
                holder.imgCat.setBackgroundResource(R.drawable.cat_3_backround);
                holder.imgCat.setImageResource(R.drawable.ic_leaf);
                break;
            case 1:
                holder.imgCat.setBackgroundResource(R.drawable.cat_1_backround);
                holder.imgCat.setImageResource(R.drawable.ic_root);
                break;
            case 2:
                holder.imgCat.setBackgroundResource(R.drawable.cat_2_backround);
                holder.imgCat.setImageResource(R.drawable.ic_bough);
                break;
            case 3:
                holder.imgCat.setBackgroundResource(R.drawable.cat_0_backround);
                holder.imgCat.setImageResource(R.drawable.ic_flower);
                break;
            case 4:
                holder.imgCat.setBackgroundResource(R.drawable.cat_4_backround);
                holder.imgCat.setImageResource(R.drawable.ic_health);
                break;
            case 5:
                holder.imgCat.setBackgroundResource(R.drawable.cat_7_backround);
                holder.imgCat.setImageResource(R.drawable.ic_bark);
                break;
            case 6:
                holder.imgCat.setBackgroundResource(R.drawable.cat_6_backround);
                holder.imgCat.setImageResource(R.drawable.ic_sap);
                break;
            case 7:
                holder.imgCat.setBackgroundResource(R.drawable.cat_5_backround);
                holder.imgCat.setImageResource(R.drawable.ic_tree);
                break;
            default:
                holder.imgCat.setBackgroundResource(R.drawable.cat_3_backround);
                holder.imgCat.setImageResource(R.drawable.ic_leaves);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra("selectedPartId", plantPart.getId());
            Log.d("Mã bộ phận dùng","Mã bộ phận dùng"+plantPart.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCat;
        TextView catNameTxt;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCat = itemView.findViewById(R.id.imgCat);
            catNameTxt = itemView.findViewById(R.id.catNameTxt);
        }
    }
}
