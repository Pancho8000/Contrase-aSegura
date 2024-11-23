package com.example.calculadorabkn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PasswordAdapterActivity passwordAdapterActivity;
    private List<PasswordActivity> passwordActivityList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewPasswords);
        FloatingActionButton fabAddPassword = findViewById(R.id.fabAddPassword);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        passwordActivityList = new ArrayList<>();
        passwordAdapterActivity = new PasswordAdapterActivity(passwordActivityList);
        recyclerView.setAdapter(passwordAdapterActivity);


        loadPasswords();

        fabAddPassword.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordListActivity.this, AddEditPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loadPasswords() {
        String userId = mAuth.getCurrentUser().getUid();
        CollectionReference passwordsRef = db.collection("users").document(userId).collection("passwords");

        passwordsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                passwordActivityList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PasswordActivity passwordActivity = document.toObject(PasswordActivity.class);
                    passwordActivity.setId(document.getId());
                    passwordActivityList.add(passwordActivity);
                }
                passwordAdapterActivity.notifyDataSetChanged();
            } else {
                Toast.makeText(PasswordListActivity.this, "Error al cargar contraseñas: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
