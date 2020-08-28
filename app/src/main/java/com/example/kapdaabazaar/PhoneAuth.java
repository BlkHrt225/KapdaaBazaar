package com.example.kapdaabazaar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kapdaabazaar.databinding.ActivityPhoneAuthBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity implements View.OnClickListener {
    ActivityPhoneAuthBinding phoneAuthBinding;
    FirebaseAuth mAuth;
    String pnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneAuthBinding = ActivityPhoneAuthBinding.inflate(getLayoutInflater());

        setContentView(phoneAuthBinding.getRoot());
        phoneAuthBinding.okbutton.setOnClickListener(this);
        phoneAuthBinding.verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okbutton:
                if (phoneAuthBinding.phinput.getText() == null) {
                    Toast.makeText(this, "Enter a Phone Number", Toast.LENGTH_LONG).show();
                } else {
                    PhoneSignUp();
                }
        }

    }

    private void PhoneSignUp() {
        pnumber = phoneAuthBinding.phinput.getText().toString();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                FirebaseSign(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                pnumber,
                120,
                TimeUnit.SECONDS,
                this,
                mCallBacks

        );
    }

    private void FirebaseSign(PhoneAuthCredential phoneAuthCredential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(pnumber).build();
                    FirebaseUser usr = mAuth.getCurrentUser();
                    usr.updateProfile(profileChangeRequest);

                } else {
                    Toast.makeText(PhoneAuth.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}