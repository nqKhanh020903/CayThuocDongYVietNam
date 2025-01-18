package com.example.doanchuyennghanh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.doanchuyennghanh.Activity.PostDetail_Activity;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.Domain.Post;
import com.example.doanchuyennghanh.R;

import java.util.ArrayList;

public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.viewholder> {
    ArrayList<Post> items;
    Context context;

    public AllPostAdapter(ArrayList<Post> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AllPostAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_listpost,parent,false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPostAdapter.viewholder holder, int position) {
        holder.txt_TieuDe.setText(items.get(position).getTieuDe());
        holder.txt_TacGia.setText("Tác giả: " + items.get(position).getTacGia());
        holder.txt_ThamVanYKhoa.setText("Tham vấn y khoa: " + items.get(position).getThamVanYKhoa());
        holder.txt_Ngay.setText("Ngày: " + items.get(position).getNgayDang());

        Glide.with(context)
                .load(items.get(position).getImage())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetail_Activity.class);
            intent.putExtra("object",items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void updateList(ArrayList<Post> filteredItems) {
        this.items = filteredItems;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView txt_TieuDe, txt_TacGia, txt_ThamVanYKhoa, txt_Ngay;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            txt_TieuDe = itemView.findViewById(R.id.txt_TieuDe);
            txt_TacGia = itemView.findViewById(R.id.txt_TacGia);
            txt_ThamVanYKhoa = itemView.findViewById(R.id.txt_ThamVanYKhoa);
            txt_Ngay = itemView.findViewById(R.id.txt_Ngay);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
