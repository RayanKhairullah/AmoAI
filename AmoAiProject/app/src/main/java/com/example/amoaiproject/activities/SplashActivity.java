package com.example.amoaiproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amoaiproject.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 2000; // 2 detik

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivLogo = findViewById(R.id.ivLogoSplash);

        // Animasi fade in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000); // 1 detik
        fadeIn.setFillAfter(true);
        ivLogo.startAnimation(fadeIn);

        // Handler untuk pindah ke MainActivity setelah delay
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);
    }
}
