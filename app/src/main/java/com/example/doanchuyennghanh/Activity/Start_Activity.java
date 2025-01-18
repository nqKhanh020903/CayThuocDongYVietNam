package com.example.doanchuyennghanh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.interaction.DragInteraction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.doanchuyennghanh.R;

public class Start_Activity extends AppCompatActivity {

    Button btn_DangNhap,btn_DangKy;
    ImageView imgback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        /*<----------- Call Func ----------->*/
        addControl();
        addEvent();
    }


    void addControl()
    {
        btn_DangNhap = findViewById(R.id.btn_DangNhap);
        btn_DangKy = findViewById(R.id.btn_DangKy);
        imgback = findViewById(R.id.img_back);
    }

    void addEvent()
    {
        btn_DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

        btn_DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start_Activity.this, Signup_Activity.class);
                startActivity(intent);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start_Activity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}