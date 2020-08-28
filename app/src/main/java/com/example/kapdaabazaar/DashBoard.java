package com.example.kapdaabazaar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashBoard extends AppCompatActivity {
    TextView uName;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Button signout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        uName = findViewById(R.id.Uname);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("Users");
        signout = findViewById(R.id.SignOut);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

            }
        });


        uName.setText(mAuth.getCurrentUser().getDisplayName().toString());

    }
}