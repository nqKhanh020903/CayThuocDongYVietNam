package com.example.doanchuyennghanh.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.doanchuyennghanh.Adapter.AllPostAdapter;

import com.example.doanchuyennghanh.Domain.Post;
import com.example.doanchuyennghanh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;


public class FavoritePost_Activity extends AppCompatActivity {
    private DatabaseReference favoriteRef, postRef;
    private TextView itemCount, title;
    private ArrayList<Post> favoritePosts;
    private AllPostAdapter allPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_post);


        title = findViewById(R.id.titleTxt);
        title.setText("Bài thuốc yêu thích");

        itemCount = findViewById(R.id.itemCount);

        favoritePosts = new ArrayList<>();
        initFavoritePost();
        getIntentExtra();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getBooleanExtra("isFavoriteChanged", false)) {
            initFavoritePost();
        }
    }
    private void getIntentExtra() {
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
    private void initFavoritePost() {
        RecyclerView postView = findViewById(R.id.postView);
        LottieAnimationView loadingPost = findViewById(R.id.loadingPost);

        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Log.e("FavoritePost", "UserID is null, returning");
            return;
        }

        favoriteRef = FirebaseDatabase.getInstance()
                .getReference("Favorites")
                .child(userId);

        postRef = FirebaseDatabase.getInstance().getReference("Post");

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoritePosts.clear();
                for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                    int postId = Integer.parseInt(favoriteSnapshot.getKey());
                    String selectedDate = favoriteSnapshot.child("selectedDate").getValue(String.class);
                    loadPostDetail(postId, selectedDate);
                }
                loadingPost.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingPost.setVisibility(View.GONE);
            }
        });
    }

    private void loadPostDetail(int postId, String selectedDate) {
        postRef.orderByChild("Id").equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Post post = postSnapshot.getValue(Post.class);
                        if (post != null) {

                            favoritePosts.add(post);
                        }
                    }
                    updateRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading post: " + error.getMessage());
            }
        });
    }

    private void updateRecyclerView() {
        RecyclerView postView = findViewById(R.id.postView);
        if (allPostAdapter == null) {
            allPostAdapter = new AllPostAdapter(favoritePosts);
            postView.setLayoutManager(new GridLayoutManager(this, 2));
            postView.setAdapter(allPostAdapter);
        } else {
            allPostAdapter.notifyDataSetChanged();
        }
        itemCount.setText("Số lượng: " + favoritePosts.size());
    }
}


