package com.flip.amiibocharacters.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.flip.amiibocharacters.R;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        ImageView imgAppIcon = (ImageView) findViewById(R.id.idAppIcon);
        animateView(imgAppIcon);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 2500);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), AmbioCharactersListActivity.class);
//        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(),
//                R.anim.right_to_left, R.anim.left_to_right).toBundle();
//        intent.putExtra("anime", bundleAnimation);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        startActivity(intent);
        finish();
    }

    public void animateView(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
        view.startAnimation(shake);
    }


}
