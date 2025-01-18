package com.example.doanchuyennghanh.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doanchuyennghanh.Domain.User;
import com.example.doanchuyennghanh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile_Activity extends AppCompatActivity {
    private LinearLayout btnHome;
    private CircleImageView imgUser;
    private TextView edtUsername, edtEmail, edtPhone, edtPassword, edtRole, edtFullName;
    private User user;
    private Button btnSave;
    private Button btnLogout;
    private LinearLayout btnPostFavorites;
    private LinearLayout btnHerbFavorites;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Người dùng chưa đăng nhập, chuyển về màn hình Login
            Intent intent = new Intent(this, Start_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        inits();
        addControl();
        addEvent();
    }

    private void inits(){
        edtUsername=findViewById(R.id.txtUsername);
        edtRole = findViewById(R.id.txtRole);
        imgUser = findViewById(R.id.imgUser);
        edtFullName = findViewById(R.id.txtFullName);
        edtPassword = findViewById(R.id.txtPassword);
        edtEmail = findViewById(R.id.txtEmail);
        edtPhone = findViewById(R.id.txtSDT);


        fetchUserData();
    }

    private void fetchUserData(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Dùng snapshot để khởi tạo đối tượng User
                        user = snapshot.getValue(User.class);

                        if (user != null) {
                            Log.d("UserProfile", "User data: " + user.getImage_user());
                            // Hiển thị dữ liệu lên giao diện
                            Glide.with(UserProfile_Activity.this)
                                    .load(user.getImage_user())
                                    .placeholder(R.drawable.default_image_user)
                                    .into(imgUser);
                            // Các trường khác
                            edtUsername.setText((user.getUsername()));
                            edtFullName.setText(user.getFull_name());
                            edtPassword.setText("*****************");
                            edtEmail.setText(user.getEmail());
                            edtPhone.setText(user.getPhoneNumber());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserProfile", "Lỗi khi lấy dữ liệu: " + error.getMessage());
                }
            });
        }
    }
    public void addControl(){
        btnHome = findViewById(R.id.btnBaiThuoc);
        btnPostFavorites=findViewById(R.id.btnPostFavorites);
        btnHerbFavorites=findViewById(R.id.btnHerbFavorites);
        btnSave = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogOut);
    }

    private void addEvent(){
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile_Activity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        btnPostFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile_Activity.this, FavoritePost_Activity.class);
                startActivity(intent);
            }
        });
        btnHerbFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile_Activity.this, FavoriteHerb_Activity.class);
                startActivity(intent);
            }
        });

        // Sự kiện nhấn nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();

                // Redirect to login screen
                Intent intent = new Intent(UserProfile_Activity.this, Start_Activity.class);
                // Clear all previous activities from the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Close the current activity

            }
        });
    }

    private void saveUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            // Lấy dữ liệu từ giao diện
            String fullName = edtFullName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            // Kiểm tra thông tin nhập liệu
            if ( fullName.isEmpty() || phone.isEmpty()) {
                Log.e("UserProfile", "Thông tin không được để trống.");
                return; // Thoát nếu có trường thông tin rỗng
            }

            // Cập nhật đối tượng User
            user.setFull_name(fullName);
            user.setPhoneNumber(phone);

            // Lưu vào Firebase
            userRef.setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(UserProfile_Activity.this, "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfile_Activity.this, "Lỗi khi cập nhật dữ liệu: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}