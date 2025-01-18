package com.example.doanchuyennghanh.Activity;

import static java.lang.Boolean.TRUE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanchuyennghanh.Domain.User;
import com.example.doanchuyennghanh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Activity extends AppCompatActivity {
    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        edtUsername = findViewById(R.id.editText_TenDangNhap);
        edtEmail = findViewById(R.id.editText_Email);
        edtPassword = findViewById(R.id.editText_MatKhau);
        btnRegister = findViewById(R.id.btn_DangKy);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        // Lấy dữ liệu từ các EditText
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Vui lòng nhập họ tên");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Vui lòng nhập email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        // Đăng ký tài khoản bằng Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lưu thông tin người dùng vào Firebase Realtime Database
                        String userId = mAuth.getCurrentUser().getUid();
                        User user = new User(username, email, "","","","",TRUE,"");


                        mDatabase.child(userId).setValue(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    finish(); // Quay lại màn hình trước đó
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}