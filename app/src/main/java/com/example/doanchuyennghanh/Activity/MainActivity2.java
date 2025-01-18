package com.example.doanchuyennghanh.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.doanchuyennghanh.Adapter.CategoryAdapter;
import com.example.doanchuyennghanh.Adapter.PostAdapter;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.Domain.PlantPart;
import com.example.doanchuyennghanh.Domain.Post;
import com.example.doanchuyennghanh.R;
import com.example.doanchuyennghanh.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView categoryView;
    private RecyclerView postView;
    private CategoryAdapter categoryAdapter;
    private PostAdapter postAdapter;
    private List<PlantPart> categoryList;
    private LottieAnimationView loadingPost, loadingHerbs;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryView = findViewById(R.id.categoryView);
        postView = findViewById(R.id.postView);
        loadingPost = findViewById(R.id.loadingPost);
        loadingHerbs = findViewById(R.id.loadingHerbs);
        TextView xemTatCaTextView = findViewById(R.id.tvXemTatCaCayThuoc);

        xemTatCaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Set GridLayoutManager với 4 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        categoryView.setLayoutManager(gridLayoutManager);

        categoryList = new ArrayList<>();

        // Khởi tạo Adapter
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryView.setAdapter(categoryAdapter);

        initPlantParts();
        initPost();
        event();
    }

    private void event(){

        TextView xemTatCaBaiThuoc = findViewById(R.id.tvXemTatCa);

        xemTatCaBaiThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, ListPost_Activity.class);
                startActivity(intent);
            }
        });

        TextView txtchaomung = findViewById(R.id.textView9);

        txtchaomung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, Start_Activity.class);
                startActivity(intent);
            }
        });

        ImageView imageView3 = findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, UserProfile_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void initPlantParts() {

        mDatabase = FirebaseDatabase.getInstance().getReference("DanhMuc");


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryList.clear();  // Xóa dữ liệu cũ
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlantPart plantPart = snapshot.getValue(PlantPart.class);
                    categoryList.add(plantPart);
                }

                // Thông báo adapter cập nhật dữ liệu mới
                categoryAdapter.notifyDataSetChanged();

                loadingHerbs.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingHerbs.setVisibility(View.GONE);
            }
        });
    }

    private void initPost() {

        mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        ArrayList<Post> list = new ArrayList<>();
        Query query = mDatabase.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot issue : snapshot.getChildren()){
                    list.add(issue.getValue(Post.class));

                }
                if(list.size()>0){
                    postView.setLayoutManager(new LinearLayoutManager(MainActivity2.this,LinearLayoutManager.HORIZONTAL,false));
                    RecyclerView.Adapter adapter = new PostAdapter(list);
                    postView.setVisibility(View.VISIBLE);
                    postView.setAdapter(adapter);
                }
                loadingPost.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingPost.setVisibility(View.GONE);
            }
        });

    }
}
