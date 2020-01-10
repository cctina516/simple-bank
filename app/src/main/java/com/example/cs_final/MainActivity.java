package com.example.cs_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView balance = findViewById(R.id.balance);
        TextView name = findViewById(R.id.name);
        Button d = findViewById(R.id.button);
        Button w = findViewById(R.id.button2);
        Button l = findViewById(R.id.button3);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");


        DocumentReference docRef = db.collection("users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int money = Integer.parseInt(document.get("balance").toString());
                        editor.putInt("money", money);
                        name.setText(document.get("name").toString());
                    } else {
                        Log.d("ERROR", "No such document");
                    }
                } else {
                    Log.d("Fail", "get failed with ", task.getException());
                }
            }
        });

        balance.setText(String.valueOf(sharedPreferences.getInt("money",0)));


    }

    public void deposite(View v){
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        EditText deposit = findViewById(R.id.deposite);

        int amount = Integer.parseInt(deposit.getText().toString());

        int newAmount = sharedPreferences.getInt("money",0) + amount;

        editor.putInt("money",newAmount);
        editor.apply();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document("111111");

        docRef.update("balance", String.valueOf(newAmount));



        Log.d("money", String.valueOf(sharedPreferences.getInt("money",0)));




    }

    public void withdraw(View v){
        EditText withdraw = findViewById(R.id.withdraw);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        int amount = Integer.parseInt(withdraw.getText().toString());

        int newAmount = sharedPreferences.getInt("money",0) - amount;

        editor.putInt("money",newAmount);
        editor.apply();
        Log.d("money", String.valueOf(sharedPreferences.getInt("money",0)));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document("111111");

        docRef.update("balance", String.valueOf(newAmount));

    }

    public void logOut(View v){
        Intent intent = new Intent(this, signIn.class);
        startActivity(intent);
    }



}

