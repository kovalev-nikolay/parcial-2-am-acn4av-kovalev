package com.kovalev.shottracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {

    private ProgressBar profileProgress;
    private ProgressBar sessionsProgress;
    private LinearLayout profileContent;
    private LinearLayout recentSessionsContainer;
    private TextView textProfileName;
    private TextView textProfileEmail;

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
        sessionsProgress = findViewById(R.id.sessionsProgress);
        profileContent = findViewById(R.id.profileContent);
        recentSessionsContainer = findViewById(R.id.recentSessionsContainer);
        textProfileName = findViewById(R.id.textProfileName);
        textProfileEmail = findViewById(R.id.textProfileEmail);

        loadBasketballImage();
        loadProfile(user.getUid());
        loadRecentSessions(user.getUid());
    }

    private void loadBasketballImage() {
        ImageView profileImage = findViewById(R.id.profileBasketballImage);
        Glide.with(this)
                .load(getString(R.string.profile_basketball_image_url))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(profileImage);
    }

    private void loadProfile(String uid) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(this::showProfile)
                .addOnFailureListener(exception -> showLoadError());
    }

    private void loadRecentSessions(String uid) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("sessions")
                .orderBy("endedAt", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(this::showRecentSessions)
                .addOnFailureListener(exception -> showSessionsError());
    }

    private void showProfile(DocumentSnapshot document) {
        profileProgress.setVisibility(View.GONE);
        if (!document.exists()) {
            showLoadError();
            return;
        }

        textProfileName.setText(valueOrEmpty(document.getString("nombre")));
        textProfileEmail.setText(valueOrEmpty(document.getString("email")));
        profileContent.setVisibility(View.VISIBLE);
    }

    private void showRecentSessions(QuerySnapshot snapshot) {
        sessionsProgress.setVisibility(View.GONE);
        recentSessionsContainer.removeAllViews();

        if (snapshot.isEmpty()) {
            addSessionsMessage(R.string.profile_recent_sessions_empty);
            return;
        }

        for (QueryDocumentSnapshot document : snapshot) {
            addSessionRow(document);
        }
    }

    private void addSessionRow(DocumentSnapshot document) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setBackgroundResource(R.drawable.bg_card_dark);
        int padding = getResources().getDimensionPixelSize(R.dimen.profile_session_row_padding);
        row.setPadding(padding, padding, padding, padding);

        TextView title = new TextView(this);
        title.setText(valueOrDefault(
                document.getString("title"),
                R.string.profile_session_title_default
        ));
        title.setTextColor(getColor(R.color.color_white));
        title.setTextSize(
                android.util.TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.profile_session_row_title_size)
        );
        title.setTypeface(title.getTypeface(), android.graphics.Typeface.BOLD);
        row.addView(title);

        int madeCount = numberValue(document, "madeCount").intValue();
        int totalAttempts = numberValue(document, "totalAttempts").intValue();
        int accuracy = (int) Math.round(numberValue(document, "accuracy").doubleValue());

        TextView result = new TextView(this);
        result.setText(getString(
                R.string.profile_session_result_format,
                madeCount,
                totalAttempts,
                accuracy
        ));
        result.setTextColor(getColor(R.color.color_profile_session_result));
        result.setTextSize(
                android.util.TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.profile_session_row_result_size)
        );
        LinearLayout.LayoutParams resultParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        resultParams.topMargin = getResources().getDimensionPixelSize(
                R.dimen.profile_session_row_result_margin
        );
        row.addView(result, resultParams);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.topMargin = getResources().getDimensionPixelSize(
                R.dimen.profile_session_row_margin
        );
        recentSessionsContainer.addView(row, rowParams);
    }

    private Number numberValue(DocumentSnapshot document, String field) {
        Object value = document.get(field);
        return value instanceof Number ? (Number) value : 0;
    }

    private void addSessionsMessage(int stringResource) {
        TextView message = new TextView(this);
        message.setText(stringResource);
        message.setTextColor(getColor(R.color.color_gray));
        message.setTextSize(
                android.util.TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.profile_value_size)
        );
        int topMargin = getResources().getDimensionPixelSize(R.dimen.profile_session_row_margin);
        message.setPadding(0, topMargin, 0, 0);
        recentSessionsContainer.addView(message);
    }

    private String valueOrEmpty(String value) {
        return valueOrDefault(value, R.string.profile_empty_value);
    }

    private String valueOrDefault(String value, int defaultResource) {
        return value == null || value.trim().isEmpty()
                ? getString(defaultResource)
                : value;
    }

    private void showLoadError() {
        profileProgress.setVisibility(View.GONE);
        Toast.makeText(this, R.string.profile_load_error, Toast.LENGTH_LONG).show();
    }

    private void showSessionsError() {
        sessionsProgress.setVisibility(View.GONE);
        addSessionsMessage(R.string.profile_sessions_load_error);
        Toast.makeText(this, R.string.profile_sessions_load_error, Toast.LENGTH_SHORT).show();
    }
}
