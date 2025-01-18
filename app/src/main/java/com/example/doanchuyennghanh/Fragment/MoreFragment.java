package com.example.doanchuyennghanh.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.doanchuyennghanh.Adapter.HerbListAdapter;
import com.example.doanchuyennghanh.Domain.HashTag;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MoreFragment extends Fragment {

    private Herb currentHerb;
    private RecyclerView relatedHerbsRecyclerView;
    private HerbListAdapter relatedHerbsAdapter;
    private ArrayList<Herb> relatedHerbsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        // Initialize RecyclerView
        relatedHerbsRecyclerView = view.findViewById(R.id.relatedHerbsRecyclerView);
        relatedHerbsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        relatedHerbsAdapter = new HerbListAdapter(getContext(), relatedHerbsList);
        relatedHerbsRecyclerView.setAdapter(relatedHerbsAdapter);

        // Retrieve currentHerb from arguments (passed from HerbDetailActivity)
        if (getArguments() != null) {
            currentHerb = (Herb) getArguments().getSerializable("medicine");
            if (currentHerb != null) {
                fetchRelatedHerbs();
            }
        }

        return view;
    }

    private void fetchRelatedHerbs() {
        if (currentHerb == null || currentHerb.getHashTagId() == null) return;

        DatabaseReference herbsRef = FirebaseDatabase.getInstance().getReference("CayThuoc");

        // In thêm log để kiểm tra dữ liệu
        Log.d("MoreFragment", "Fetching related herbs for: " + currentHerb.getTenThuoc());

        // Duyệt qua tất cả các herbs
        herbsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> currentHerbTagIds = currentHerb.getHashTagId();
                Log.d("MoreFragment", "Current herb hashTagIds: " + currentHerbTagIds);

                List<Herb> fetchedHerbs = new ArrayList<>();
                for (DataSnapshot herbSnapshot : snapshot.getChildren()) {
                    Herb herb = herbSnapshot.getValue(Herb.class);
                    if (herb != null && herb.getHashTagId() != null) {
                        Log.d("MoreFragment", "Checking herb: " + herb.getTenThuoc());
                        // Kiểm tra nếu herb có ít nhất 1 HashTagId trùng với currentHerb
                        for (Integer tagId : herb.getHashTagId()) {
                            if (currentHerbTagIds.contains(tagId)) {
                                if (herb.getId() != currentHerb.getId()) {
                                    Log.d("MoreFragment", "Match found for herb: " + herb.getTenThuoc());
                                    fetchedHerbs.add(herb);
                                }
                                break;
                            }
                        }
                    }
                }

                // Cập nhật RecyclerView với danh sách các herbs liên quan
                Log.d("MoreFragment", "Fetched herbs size: " + fetchedHerbs.size());

                relatedHerbsList.clear();
                relatedHerbsList.addAll(fetchedHerbs);
                relatedHerbsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
                Log.e("MoreFragment", "Error fetching herbs: " + error.getMessage());
            }
        });
    }
}
