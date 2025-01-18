package com.example.doanchuyennghanh.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.doanchuyennghanh.Domain.Post;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.doanchuyennghanh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostDetail_Activity extends AppCompatActivity {

    private Post object;
    private boolean isFavorite = false; // Trạng thái yêu thích
    private ImageView favBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getIntentExtra();
        setVariable();
    }



    private void setVariable() {
        ImageView backBtn = findViewById(R.id.backBtn);
        ImageView pic = findViewById(R.id.pic);
        TextView txt_TieuDe = findViewById(R.id.txt_TieuDe);
        TextView txt_TacGia = findViewById(R.id.txt_TacGia);
        TextView txt_ThamVanYKhoa = findViewById(R.id.txt_ThamVanYKhoa);
        TextView txt_Ngay = findViewById(R.id.txt_Ngay);
        TextView txt_MoTa = findViewById(R.id.txt_MoTa);
        TextView txt_Nguon = findViewById(R.id.txt_Nguon);
        TextView txt_Rating = findViewById(R.id.rateTxt);
        RatingBar RatingBar = findViewById(R.id.ratingBar);
        favBtn = findViewById(R.id.favBtn); // Nút yêu thích

        backBtn.setOnClickListener(v -> {
            finish();;
        });

        // Gắn sự kiện cho nút nguồn click
        txt_Nguon.setOnClickListener(v -> {
            String url = txt_Nguon.getText().toString();
            Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // Hiển thị thông tin bài viết
        Glide.with(PostDetail_Activity.this)
                .load(object.getImage())
                .into(pic);
        txt_TieuDe.setText(object.getTieuDe());
        txt_TacGia.setText(object.getTacGia());
        txt_ThamVanYKhoa.setText(object.getThamVanYKhoa());
        txt_Ngay.setText(object.getNgayDang());
        txt_MoTa.setText(object.getTomTatNoiDung());
        txt_Nguon.setText(object.getNguon());

        if(object.getStar()>0.0){
            txt_Rating.setText(String.valueOf(object.getStar()));
            double starValue = object.getStar();
            float starRating = (float) starValue;
            RatingBar.setRating(starRating);
        }else {
            txt_Rating.setVisibility(View.GONE);
            RatingBar.setVisibility(View.GONE);
        }

        // Kiểm tra trạng thái yêu thích từ Firebase
        checkFavoriteStatus();

        // Gắn sự kiện click cho nút Favorite
        favBtn.setOnClickListener(v -> toggleFavorite());
    }

    private void getIntentExtra() {

        object = (Post) getIntent().getSerializableExtra("object");

    }
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("isFavoriteChanged", true);
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

    private void checkFavoriteStatus() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);
        String postId = String.valueOf(object.getId());

        // Kiểm tra xem postId có tồn tại không
        favRef.child(postId).get().addOnCompleteListener(task -> {
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

        isFavorite = !isFavorite; // Đổi trạng thái yêu thích

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

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);
        String postId = String.valueOf(object.getId());

        // Lưu ngày vào key của postId
        HashMap<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("selectedDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        favRef.child(postId).setValue(favoriteData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PostDetail_Activity.this, "Đã thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PostDetail_Activity.this, "Lỗi khi lưu yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFavoriteFromFirebase() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);
        String postId = String.valueOf(object.getId());

        // Xóa key của postId trong Favorites
        favRef.child(postId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PostDetail_Activity.this, "Đã xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PostDetail_Activity.this, "Lỗi khi xóa yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}