package com.kovalev.shottracker;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseSessionRepository {

    private final Context context;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    public FirebaseSessionRepository(Context context) {
        this.context = context.getApplicationContext();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void saveSession(
            String mode,
            String title,
            long startedAt,
            long endedAt,
            int madeCount,
            int totalAttempts,
            int accuracy,
            int maxStreak,
            double avgTimePerShot
    ) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            showSyncError();
            return;
        }

        Map<String, Object> session = new HashMap<>();
        session.put("mode", mode);
        session.put("title", title);
        session.put("startedAt", startedAt);
        session.put("endedAt", endedAt);
        session.put("madeCount", madeCount);
        session.put("totalAttempts", totalAttempts);
        session.put("accuracy", accuracy);
        session.put("maxStreak", maxStreak);
        session.put("avgTimePerShot", avgTimePerShot);

        firestore.collection("users")
                .document(user.getUid())
                .collection("sessions")
                .add(session)
                .addOnSuccessListener(reference ->
                        Toast.makeText(
                                context,
                                R.string.session_cloud_synced,
                                Toast.LENGTH_SHORT
                        ).show())
                .addOnFailureListener(exception -> showSyncError());
    }

    private void showSyncError() {
        Toast.makeText(context, R.string.session_cloud_error, Toast.LENGTH_SHORT).show();
    }
}
