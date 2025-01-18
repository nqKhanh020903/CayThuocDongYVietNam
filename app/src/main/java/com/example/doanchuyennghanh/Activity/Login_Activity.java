package com.example.doanchuyennghanh.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanchuyennghanh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    private Button btn_DangNhap;
    private EditText edt_Username, edt_Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các view
        btn_DangNhap = (Button) findViewById(R.id.btn_DangNhap);
        edt_Username = (EditText) findViewById(R.id.editText_TenDangNhap);
        edt_Password = (EditText) findViewById(R.id.editText_MatKhau);

        btn_DangNhap.setOnClickListener(v -> {
            String username = edt_Username.getText().toString().trim();
            String password = edt_Password.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            // Tìm kiếm email dựa trên username
            usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String email = snapshot.child("email").getValue(String.class);

                            if (email != null) {
                                // Sử dụng email và password để đăng nhập qua Firebase Authentication
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Login_Activity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Login_Activity.this, UserProfile_Activity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                                                Toast.makeText(Login_Activity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(Login_Activity.this, "Không tìm thấy email tương ứng!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(Login_Activity.this, "Tên đăng nhập không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Login_Activity.this, "Lỗi truy vấn dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}