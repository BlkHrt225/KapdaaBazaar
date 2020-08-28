package com.example.kapdaabazaar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    static String msg = "AuthType";
    FirebaseAuth mAuth;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        frameLayout = findViewById(R.id.mainframe);
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Register.this, DashBoard.class);
            startActivity(intent);
        }
        Intent parent = getIntent();
        String s = parent.getStringExtra(msg);
        //Toast.makeText(this,s,Toast.LENGTH_LONG).show();
        if (s.equals("LOGIN")) {
            Log.d("TAG", "Type Login");
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            setFragment(new LoginFragment());
        }
        if (s.equals("SIGNUP")) {
            setFragment(new SignupFragment());
        }

    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}