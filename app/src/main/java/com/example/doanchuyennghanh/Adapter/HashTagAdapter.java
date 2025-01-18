package com.example.doanchuyennghanh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanchuyennghanh.Domain.HashTag;
import com.example.doanchuyennghanh.R;

import java.util.HashSet;
import java.util.List;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolder> {

    private Context context;
    private List<HashTag> hashTagList;
    private HashSet<Integer> selectedHashTagIds;

    public HashTagAdapter(Context context, List<HashTag> hashTagList, HashSet<Integer> selectedHashTagIds) {
        this.context = context;
        this.hashTagList = hashTagList;
        this.selectedHashTagIds = selectedHashTagIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_hashtag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashTag hashTag = hashTagList.get(position);

        // Set hashtag name and checkbox state
        holder.hashTagText.setText(hashTag.getTenHashTag());
        holder.hashTagCheckBox.setChecked(selectedHashTagIds.contains(hashTag.getId()));

        // Handle checkbox toggle logic
        holder.hashTagCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedHashTagIds.add(hashTag.getId());
            } else {
                selectedHashTagIds.remove(hashTag.getId());
            }
        });

        // Ensure clicks on the row also toggle the checkbox
        holder.itemView.setOnClickListener(v -> {
            boolean currentState = holder.hashTagCheckBox.isChecked();
            holder.hashTagCheckBox.setChecked(!currentState); // This triggers the listener above
        });
    }

    @Override
    public int getItemCount() {
        return hashTagList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hashTagText;
        CheckBox hashTagCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hashTagText = itemView.findViewById(R.id.hashTagText);
            hashTagCheckBox = itemView.findViewById(R.id.hashTagCheckBox);
        }
    }
}