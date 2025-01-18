package com.example.doanchuyennghanh.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanchuyennghanh.R;

public class Intro_Activity extends AppCompatActivity {

    ImageView logo;
    TextView heading;
    Animation fadeIn;
    private static final int SPLASH_SCREEN_TIMEOUT = 3000; // timming 3s
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        addControl();
        addEvent();

    }

    void addControl()
    {
        logo = findViewById(R.id.imageView);
        heading = findViewById(R.id.txt_Heading02);
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
    }

    void addEvent()
    {

        logo.startAnimation(fadeIn);
        heading.startAnimation(fadeIn);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intro_Activity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}