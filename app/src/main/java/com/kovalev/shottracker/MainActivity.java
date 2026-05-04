package com.kovalev.shottracker;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatsModeTitle;
    private TextView tvAccuracyPercent;
    private TextView tvShotsCount;
    private ImageView imgStatsRing;

    private FrameLayout btnJuntos;
    private FrameLayout btnLibres;
    private FrameLayout btnCampo;
    private FrameLayout btnTres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClicks();

        showJuntosStats();
    }

    private void initViews() {
        tvStatsModeTitle = findViewById(R.id.tvStatsModeTitle);
        tvAccuracyPercent = findViewById(R.id.tvAccuracyPercent);
        tvShotsCount = findViewById(R.id.tvShotsCount);
        imgStatsRing = findViewById(R.id.imgStatsRing);

        btnJuntos = findViewById(R.id.btnJuntos);
        btnLibres = findViewById(R.id.btnLibres);
        btnCampo = findViewById(R.id.btnCampo);
        btnTres = findViewById(R.id.btnTres);
    }

    private void setupClicks() {
        btnJuntos.setOnClickListener(v -> showJuntosStats());
        btnLibres.setOnClickListener(v -> showLibresStats());
        btnCampo.setOnClickListener(v -> showCampoStats());
        btnTres.setOnClickListener(v -> showTresStats());
    }

    private void showJuntosStats() {
        tvStatsModeTitle.setText("Juntos");
        tvAccuracyPercent.setText("27%");
        tvShotsCount.setText("270 / 1000");
        imgStatsRing.setImageResource(R.drawable.stats_ring_static);
    }

    private void showLibresStats() {
        tvStatsModeTitle.setText("Tiros libres");
        tvAccuracyPercent.setText("64%");
        tvShotsCount.setText("64 / 100");
        imgStatsRing.setImageResource(R.drawable.stats_ring_static);
    }

    private void showCampoStats() {
        tvStatsModeTitle.setText("Tiros de campo");
        tvAccuracyPercent.setText("42%");
        tvShotsCount.setText("84 / 200");
        imgStatsRing.setImageResource(R.drawable.stats_ring_static);
    }

    private void showTresStats() {
        tvStatsModeTitle.setText("Tiros de tres");
        tvAccuracyPercent.setText("31%");
        tvShotsCount.setText("31 / 100");
        imgStatsRing.setImageResource(R.drawable.stats_ring_static);
    }
}