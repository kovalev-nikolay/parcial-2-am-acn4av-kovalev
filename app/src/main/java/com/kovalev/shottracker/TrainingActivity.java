package com.kovalev.shottracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrainingActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_MODE_TITLE = "mode_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupClicks();
    }

    private void setupClicks() {
        findViewById(R.id.btnTrainJuntos).setOnClickListener(v ->
                openShotSession("juntos", "Juntos"));

        findViewById(R.id.btnTrainLibres).setOnClickListener(v ->
                openShotSession("libres", "Tiros libres"));

        findViewById(R.id.btnTrainCampo).setOnClickListener(v ->
                openShotSession("campo", "Tiros de campo"));

        findViewById(R.id.btnTrainTres).setOnClickListener(v ->
                openShotSession("tres", "Tiros de tres"));
    }

    private void openShotSession(String mode, String modeTitle) {
        Intent intent = new Intent(TrainingActivity.this, ShotSessionActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        intent.putExtra(EXTRA_MODE_TITLE, modeTitle);
        startActivity(intent);
    }
}