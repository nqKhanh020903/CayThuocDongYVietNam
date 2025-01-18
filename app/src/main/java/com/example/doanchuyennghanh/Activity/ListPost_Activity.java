package com.example.doanchuyennghanh.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.doanchuyennghanh.Adapter.AllPostAdapter;

import com.example.doanchuyennghanh.Domain.Post;
import com.example.doanchuyennghanh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListPost_Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView itemCount;
    private ArrayList<Post> currentCategoryPost;
    private AllPostAdapter allPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_post);
        currentCategoryPost = new ArrayList<>();

        getIntentExtra();
        initAllPost();
        handleSearch();

    }

    private void initAllPost() {
        RecyclerView postView = findViewById(R.id.postView);
        LottieAnimationView loadingPost = findViewById(R.id.loadingPost);

        mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        Query query = mDatabase.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot issue : snapshot.getChildren()) {
                    currentCategoryPost.add(issue.getValue(Post.class));
                }
                itemCount = findViewById(R.id.itemCount);
                itemCount.setText("Số lượng: " + currentCategoryPost.size());
                if (currentCategoryPost.size() > 0) {
                    postView.setLayoutManager(new GridLayoutManager(ListPost_Activity.this, 2));
                    allPostAdapter = new AllPostAdapter(currentCategoryPost); // Gán vào allPostAdapter
                    postView.setAdapter(allPostAdapter);
                    postView.setVisibility(View.VISIBLE);
                }
                loadingPost.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingPost.setVisibility(View.GONE);
            }
        });
    }

    private void filterPostList(String query) {
        String searchQuery = query.trim().toLowerCase();
        ArrayList<Post> filteredList;

        if (searchQuery.isEmpty()) {
            filteredList = new ArrayList<>(currentCategoryPost);
        } else {
            filteredList = new ArrayList<>();
            for (Post post : currentCategoryPost) {
                if (post.getTieuDe().toLowerCase().contains(searchQuery)) {
                    filteredList.add(post);
                }
            }
        }

        if (allPostAdapter != null) {
            allPostAdapter.updateList(filteredList);
        }

        if (filteredList.isEmpty()) {
            itemCount.setText("Không tìm thấy bài thuốc nào.");
        } else {
            itemCount.setText("Số lượng: " + filteredList.size());
        }
    }

    private void handleSearch(){
        EditText searchBar = findViewById(R.id.searchBar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPostList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void getIntentExtra() {

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            finish();;
        });
    }
}