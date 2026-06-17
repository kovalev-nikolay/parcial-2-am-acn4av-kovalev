package com.kovalev.shottracker;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShotSessionActivity extends AppCompatActivity {

    private TextView tvSessionMode;
    private TextView tvSessionCounter;
    private TextView tvSessionStatus;

    private String mode;
    private String modeTitle;

    private int madeCount = 0;
    private int totalCount = 0;
    private int currentStreak = 0;
    private int maxStreak = 0;

    private long startedAt;
    private long lastShotAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shot_session);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mode = getIntent().getStringExtra(TrainingActivity.EXTRA_MODE);
        modeTitle = getIntent().getStringExtra(TrainingActivity.EXTRA_MODE_TITLE);

        if (mode == null) {
            mode = "juntos";
        }

        if (modeTitle == null) {
            modeTitle = "Juntos";
        }

        startedAt = System.currentTimeMillis();
        lastShotAt = startedAt;

        initViews();
        setupClicks();
        updateCounter();
        startStatusBlink();
    }

    private void initViews() {
        tvSessionMode = findViewById(R.id.tvSessionMode);
        tvSessionCounter = findViewById(R.id.tvSessionCounter);
        tvSessionStatus = findViewById(R.id.tvSessionStatus);

        tvSessionMode.setText(modeTitle);
    }

    private void setupClicks() {
        findViewById(R.id.btnShotMade).setOnClickListener(v -> registerShot(true));
        findViewById(R.id.btnShotMissed).setOnClickListener(v -> registerShot(false));

        findViewById(R.id.btnFinishSession).setOnClickListener(v -> finish());
    }

    private void registerShot(boolean made) {
        totalCount++;
        lastShotAt = System.currentTimeMillis();

        if (made) {
            madeCount++;
            currentStreak++;

            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }
        } else {
            currentStreak = 0;
        }

        updateCounter();
    }

    private void updateCounter() {
        tvSessionCounter.setText(madeCount + " / " + totalCount);
    }

    private void startStatusBlink() {
        AlphaAnimation blinkAnimation = new AlphaAnimation(1.0f, 0.25f);
        blinkAnimation.setDuration(600);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        tvSessionStatus.startAnimation(blinkAnimation);
    }
}