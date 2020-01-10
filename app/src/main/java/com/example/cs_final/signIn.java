package com.example.cs_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class signIn extends AppCompatActivity {

    String password1="";
    String userInput="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button logIn = findViewById(R.id.logIn);


    }

    public void logInClicked(View v){

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(username.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        password1 = document.get("password").toString();
                    } else {
                        Log.d("ERROR", "No such document");
                    }
                } else {
                    Log.d("Fail", "get failed with ", task.getException());
                }
            }
        });

        if (password.getText().toString().equals(password1)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", username.getText().toString());
            startActivity(intent);
        }

    }
}






