package com.example.doanchuyennghanh.Activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanchuyennghanh.Adapter.HerbListAdapter;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.R;
import com.example.doanchuyennghanh.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class FavoriteHerb_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HerbListAdapter herbAdapter;
    private ArrayList<Herb> favoriteHerbsList;
    private TextView itemCount;
    private FirebaseDatabase database;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();


        itemCount = binding.itemCount;
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        favoriteHerbsList = new ArrayList<>();
        loadFavoriteHerbs();


        handleSearch();

        // Cài đặt toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    private void loadFavoriteHerbs() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Log.e("FavoriteHerb", "UserID is null, returning");
            return;
        }

        DatabaseReference favoriteRef = FirebaseDatabase.getInstance()
                .getReference("HerbFavorites")
                .child(userId);

        DatabaseReference herbRef = FirebaseDatabase.getInstance().getReference("CayThuoc");

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteHerbsList.clear();
                int index = 0;
                for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                    if (favoriteSnapshot.exists() && favoriteSnapshot.getValue() != null) {
                        // Sử dụng chỉ số (index) làm herbId
                        int herbId = index;
                        String selectedDate = favoriteSnapshot.child("selectedDate").getValue(String.class);



                        loadHerbDetail(herbId, selectedDate);
                    }
                    index++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error loading favorites: " + error.getMessage());
            }
        });
    }

    private void loadHerbDetail(int herbId, String selectedDate) {
        DatabaseReference herbRef = FirebaseDatabase.getInstance().getReference("CayThuoc");

        herbRef.orderByChild("Id").equalTo(herbId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot herbSnapshot : snapshot.getChildren()) {
                        Herb herb = herbSnapshot.getValue(Herb.class);
                        if (herb != null) {
                            favoriteHerbsList.add(herb);
                        }
                    }
                    updateRecyclerView(favoriteHerbsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error loading herb detail: " + error.getMessage());
            }
        });
    }



    private void updateRecyclerView(ArrayList<Herb> list) {
        itemCount.setText("Số lượng: " + list.size());
        if (herbAdapter == null) {
            herbAdapter = new HerbListAdapter(this, list);
            recyclerView.setAdapter(herbAdapter);
        } else {
            herbAdapter.updateList(list);
        }
    }

    private void handleSearch() {
        EditText searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHerbList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterHerbList(String query) {
        String searchQuery = query.trim().toLowerCase();
        ArrayList<Herb> filteredList = new ArrayList<>();

        if (searchQuery.isEmpty()) {
            filteredList = new ArrayList<>(favoriteHerbsList);
        } else {
            for (Herb herb : favoriteHerbsList) {
                if (herb.getTenThuoc().toLowerCase().contains(searchQuery)) {
                    filteredList.add(herb);
                }
            }
        }

        herbAdapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            itemCount.setText("Không tìm thấy cây thuốc nào.");
        } else {
            itemCount.setText("Số lượng: " + filteredList.size());
        }
    }
}
