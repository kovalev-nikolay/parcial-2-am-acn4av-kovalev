package com.kovalev.shottracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private ProgressBar profileProgress;
    private LinearLayout profileContent;
    private TextView textProfileName;
    private TextView textProfileEmail;
    private TextView textProfileCommission;
    private TextView textProfileProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);
        profileProgress = findViewById(R.id.profileProgress);
        profileContent = findViewById(R.id.profileContent);
        textProfileName = findViewById(R.id.textProfileName);
        textProfileEmail = findViewById(R.id.textProfileEmail);
        textProfileCommission = findViewById(R.id.textProfileCommission);
        textProfileProject = findViewById(R.id.textProfileProject);

        loadProfile(user.getUid());
    }

    private void loadProfile(String uid) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(this::showProfile)
                .addOnFailureListener(exception -> showLoadError());
    }

    private void showProfile(DocumentSnapshot document) {
        profileProgress.setVisibility(View.GONE);
        if (!document.exists()) {
            showLoadError();
            return;
        }

        textProfileName.setText(valueOrEmpty(document.getString("nombre")));
        textProfileEmail.setText(valueOrEmpty(document.getString("email")));
        textProfileCommission.setText(valueOrEmpty(document.getString("comision")));
        textProfileProject.setText(valueOrEmpty(document.getString("proyecto")));
        profileContent.setVisibility(View.VISIBLE);
    }

    private String valueOrEmpty(String value) {
        return value == null || value.trim().isEmpty()
                ? getString(R.string.profile_empty_value)
                : value;
    }

    private void showLoadError() {
        profileProgress.setVisibility(View.GONE);
        Toast.makeText(this, R.string.profile_load_error, Toast.LENGTH_LONG).show();
    }
}