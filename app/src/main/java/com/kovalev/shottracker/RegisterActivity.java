package com.kovalev.shottracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editRepeatPassword;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editName = findViewById(R.id.editRegisterName);
        editEmail = findViewById(R.id.editRegisterEmail);
        editPassword = findViewById(R.id.editRegisterPassword);
        editRepeatPassword = findViewById(R.id.editRegisterRepeatPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();
        String repeatPassword = editRepeatPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
            Toast.makeText(this, R.string.register_fields_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, R.string.register_passwords_different, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, R.string.register_password_too_short, Toast.LENGTH_SHORT).show();
            return;
        }

        buttonRegister.setEnabled(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        buttonRegister.setEnabled(true);
                        Toast.makeText(this, R.string.register_auth_error, Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        buttonRegister.setEnabled(true);
                        Toast.makeText(this, R.string.register_auth_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    saveUserProfile(user.getUid(), name, email);
                });
    }

    private void saveUserProfile(String uid, String name, String email) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("nombre", name);
        profile.put("email", email);
        profile.put("comision", "ACN4AV");
        profile.put("proyecto", "BasketShotTracker");
        profile.put("createdAt", FieldValue.serverTimestamp());

        firestore.collection("users").document(uid).set(profile)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(exception -> {
                    buttonRegister.setEnabled(true);
                    Toast.makeText(this, R.string.register_profile_error, Toast.LENGTH_LONG).show();
                });
    }
}
