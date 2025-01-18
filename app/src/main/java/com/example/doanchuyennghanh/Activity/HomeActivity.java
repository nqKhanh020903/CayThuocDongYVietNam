package com.example.doanchuyennghanh.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanchuyennghanh.Adapter.HashTagAdapter;
import com.example.doanchuyennghanh.Adapter.HerbListAdapter;
import com.example.doanchuyennghanh.Domain.HashTag;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.R;
import com.example.doanchuyennghanh.databinding.ActivityHomeBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ActivityHomeBinding binding;
    private RecyclerView recyclerView;
    private TextView itemCount;
    private HerbListAdapter herbAdapter;
    private ArrayList<Herb> herbsList;
    private ArrayList<Herb> currentCategoryHerbs;
    private Spinner spinner; // Đảm bảo Spinner vẫn giữ cho các logic sau này
    private ImageView filterIcon;
    private HashSet<Integer> selectedHashTagIds = new HashSet<>();
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filterIcon = findViewById(R.id.ic_filter);


        filterIcon.setOnClickListener(v -> showFilterPopup());

        herbsList = new ArrayList<>();
        currentCategoryHerbs = new ArrayList<>();
        getIntentExtra();
        initializeFirebase();

        int selectedPartId = getIntent().getIntExtra("selectedPartId", -1);
        Log.d("HomeActivity", "Received selectedPartId: " + selectedPartId);

        if (selectedPartId != -1) {
            loadHerbsByPart(selectedPartId);
        } else {
            Log.d("HomeActivity", "No selectedPartId received. Loading all herbs.");
            loadAllHerbs();
        }
        handleSearch();

    }
    private void filterHerbsByHashTags() {


        ArrayList<Herb> sourceList = new ArrayList<>(currentCategoryHerbs);

        if (selectedHashTagIds.isEmpty()) {
            herbAdapter.updateList(sourceList);
            itemCount.setText("Số lượng: " + sourceList.size());
            return;
        }

        ArrayList<Herb> filteredHerbs = new ArrayList<>();
        if (!selectedHashTagIds.isEmpty()) {
            for (Herb herb : sourceList) {
                if (herb.getHashTagId() != null) {
                    for (Integer hashTagId : herb.getHashTagId()) {
                        if (selectedHashTagIds.contains(hashTagId)) {
                            filteredHerbs.add(herb);
                            break;
                        }
                    }
                }
            }
        } else {
            filteredHerbs = sourceList;
        }

        herbAdapter.updateList(filteredHerbs);

        if (filteredHerbs.isEmpty()) {
            itemCount.setText("Không có cây thuốc nào.");
        } else {
            itemCount.setText("Số lượng: " + filteredHerbs.size());
        }
    }


    private void showFilterPopup() {
        DatabaseReference hashTagsRef = FirebaseDatabase.getInstance().getReference("HashTag");

        hashTagsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d(TAG, "No hashtags found in the database.");
                    return;
                }


                List<HashTag> hashTags = new ArrayList<>();
                for (DataSnapshot hashTagSnapshot : snapshot.getChildren()) {
                    HashTag hashTag = hashTagSnapshot.getValue(HashTag.class);
                    if (hashTag != null) {
                        hashTags.add(hashTag);
                    }
                }

                showHashTagDialog(hashTags);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching hashtags: " + error.getMessage());
            }
        });
    }

    private void showHashTagDialog(List<HashTag> hashTags) {
        // Inflate the custom dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_popup, null);
        RecyclerView hashTagRecyclerView = dialogView.findViewById(R.id.hashTagRecyclerView);
        hashTagRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter
        HashTagAdapter hashTagAdapter = new HashTagAdapter(this, hashTags, selectedHashTagIds);
        hashTagRecyclerView.setAdapter(hashTagAdapter);

        //hashTagRecyclerView.post(() -> hashTagRecyclerView.invalidateItemDecorations());

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {

                    filterHerbsByHashTags();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance();
    }

    private void loadAllHerbs() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CayThuoc");
        herbsList = new ArrayList<>();
        currentCategoryHerbs = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<Herb> list = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Herb herb = issue.getValue(Herb.class);
                        if (herb != null) {
                            herbsList.add(herb);
                            currentCategoryHerbs.add(herb);
                            list.add(herb);
                        }
                    }
                    updateRecyclerView(list);
                } else {
                    itemCount.setText("Không có cây thuốc nào.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoadAllHerbs", "Database query cancelled: " + error.getMessage());
            }
        });
    }
    private void loadHerbsByPart(int selectedPartId) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CayThuoc");
        currentCategoryHerbs = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Herb> filteredList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Herb herb = issue.getValue(Herb.class);
                        if (herb != null) {
                            List<Integer> boPhanDungIds = herb.getDanhMucId();
                            if (boPhanDungIds != null) {
                                for (Number id : boPhanDungIds) {
                                    if (id.intValue() == selectedPartId) {
                                        currentCategoryHerbs.add(herb);
                                        filteredList.add(herb);
                                        break;
                                    }
                                }
                            }
                        } else {
                            Log.e("DebugHerb", "Failed to parse Herb object.");
                        }
                    }

                    if (!filteredList.isEmpty()) {
                        updateRecyclerView(filteredList);
                    } else {
                        itemCount.setText("Không có cây thuốc nào cho bộ phận này.");
                    }
                } else {
                    itemCount.setText("Không có cây thuốc nào.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoadHerbs", "Database query cancelled: " + error.getMessage());
            }
        });
    }
    private void getIntentExtra() {
        itemCount = findViewById(R.id.itemCount);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hide the default title as we have a custom title in the layout
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        // Initialize RecyclerView for herbs list
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void updateRecyclerView(ArrayList<Herb> list) {
        itemCount.setText("Số lượng: " + list.size());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        herbAdapter = new HerbListAdapter(this, list);
        binding.recyclerView.setAdapter(herbAdapter);
    }

    private void handleSearch(){
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
        ArrayList<Herb> filteredList;

        if (searchQuery.isEmpty()) {
            filteredList = new ArrayList<>(currentCategoryHerbs);
        } else {
            // Filter herbs within the current category
            filteredList = new ArrayList<>();
            for (Herb herb : currentCategoryHerbs) {
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
