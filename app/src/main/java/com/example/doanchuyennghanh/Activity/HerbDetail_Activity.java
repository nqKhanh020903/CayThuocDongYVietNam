package com.example.doanchuyennghanh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doanchuyennghanh.Adapter.HerbDetailPagerAdapter;
import com.example.doanchuyennghanh.Adapter.ImageSliderAdapter;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.Domain.Image;
import com.example.doanchuyennghanh.Domain.Medicine;
import com.example.doanchuyennghanh.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HerbDetail_Activity extends AppCompatActivity {

    private Herb selectedMedicine;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private boolean isFavorite = false; // Trạng thái yêu thích
    private ImageView favBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herb_detail);

        selectedMedicine = (Herb) getIntent().getSerializableExtra("selectedMedicine");

        //<------------------ Toolbar ------------------>//

        Toolbar customToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        TextView toolbarTitle = customToolbar.findViewById(R.id.toolbar_herb_title);
        TextView toolbarFamily = customToolbar.findViewById(R.id.toolbar_herb_family);
        toolbarTitle.setText(selectedMedicine.getTenThuoc());
        toolbarFamily.setText(selectedMedicine.getHo());

        customToolbar.setNavigationOnClickListener(v -> finish());
        //<------------------ Toolbar ------------------>//

        ViewPager2 imageSlider = findViewById(R.id.imageSlider);
        TextView medicineTitle = findViewById(R.id.medicineTitle);
        TextView medicineDescription = findViewById(R.id.medicineDescription);
        favBtn = findViewById(R.id.imgFavorite);


        medicineTitle.setText(selectedMedicine.getTenThuoc());
        medicineDescription.setText(selectedMedicine.getTenKhoaHoc());

        if (selectedMedicine.getImageList() != null && !selectedMedicine.getImageList().isEmpty()) {
            ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(this, selectedMedicine.getImageList());
            imageSlider.setAdapter(sliderAdapter);
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        HerbDetailPagerAdapter adapter = new HerbDetailPagerAdapter(this, selectedMedicine);
        viewPager.setAdapter(adapter);

        // Thiết lập TabLayout với ViewPager2 và custom tab view
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
            TextView tabText = ((View) tabView).findViewById(R.id.tab_text);
            ImageView tabIcon = tabView.findViewById(R.id.tab_icon);
            if (position == 0) {
                tabIcon.setImageResource(R.drawable.ic_general_info);
                tabText.setText("Thông tin cơ bản");
            } else if (position == 1) {
                tabText.setText("Bình luận");
                tabIcon.setImageResource(R.drawable.ic_interactions);
            } else if (position == 2) {
                tabText.setText("Thuốc liên quan");
                tabIcon.setImageResource(R.drawable.ic_more);
            }
            tab.setCustomView(tabView);
        }).attach();


        checkFavoriteStatus();

        // Gắn sự kiện click cho nút yêu thích
        favBtn.setOnClickListener(v -> toggleFavorite());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkFavoriteStatus() {

        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("HerbFavorites").child(userId);
        String herbId = String.valueOf(selectedMedicine.getId());


        favRef.child(herbId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                isFavorite = true;
                favBtn.setImageResource(R.drawable.favortited);
            } else {
                isFavorite = false;
                favBtn.setImageResource(R.drawable.favorite);
            }
        });
    }

    private void toggleFavorite() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để lưu yêu thích!", Toast.LENGTH_SHORT).show();
            return;
        }

        isFavorite = !isFavorite;

        if (isFavorite) {
            favBtn.setImageResource(R.drawable.favortited);
            saveFavoriteToFirebase();
        } else {
            favBtn.setImageResource(R.drawable.favorite);
            removeFavoriteFromFirebase();
        }
    }

    private void saveFavoriteToFirebase() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để lưu yêu thích!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("HerbFavorites").child(userId);
        String herbId = String.valueOf(selectedMedicine.getId());

        // Lưu ngày vào key của herbId
        HashMap<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("selectedDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        favRef.child(herbId).setValue(favoriteData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(HerbDetail_Activity.this, "Đã thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HerbDetail_Activity.this, "Lỗi khi lưu yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFavoriteFromFirebase() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("HerbFavorites").child(userId);
        String herbId = String.valueOf(selectedMedicine.getId());

        // Xóa key của herbId trong HerbFavorites
        favRef.child(herbId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(HerbDetail_Activity.this, "Đã xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HerbDetail_Activity.this, "Lỗi khi xóa yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}