package com.example.calculadorabkn;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEditPasswordActivity extends AppCompatActivity {

    private EditText edtSiteName, edtUsername, edtPassword, edtNotes;
    private Button btnSave, btnCancel;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String passwordId = null; // Para identificar si estamos editando o creando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_password);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        edtSiteName = findViewById(R.id.edtSiteName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtNotes = findViewById(R.id.edtNotes);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("passwordId")) {
            passwordId = intent.getStringExtra("passwordId");
            loadPasswordDetails(passwordId);
        }

        btnSave.setOnClickListener(v -> savePassword());

        btnCancel.setOnClickListener(v -> {
        });
    }

    private void loadPasswordDetails(String id) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("passwords").document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        edtSiteName.setText(documentSnapshot.getString("siteName"));
                        edtUsername.setText(documentSnapshot.getString("username"));
                        edtPassword.setText(documentSnapshot.getString("password"));
                        edtNotes.setText(documentSnapshot.getString("notes"));
                    } else {
                        Toast.makeText(this, "No se encontró la contraseña", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show());
    }

    private void savePassword() {
        String siteName = edtSiteName.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String notes = edtNotes.getText().toString().trim();

        if (TextUtils.isEmpty(siteName) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> passwordData = new HashMap<>();
        passwordData.put("siteName", siteName);
        passwordData.put("username", username);
        passwordData.put("password", password);
        passwordData.put("notes", notes);

        if (passwordId == null) {
            db.collection("users").document(userId).collection("passwords")
                    .add(passwordData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Contraseña guardada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar la contraseña", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("users").document(userId).collection("passwords").document(passwordId)
                    .set(passwordData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show());
        }
    }
}
