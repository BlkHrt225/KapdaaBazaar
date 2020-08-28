package com.example.kapdaabazaar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kapdaabazaar.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private ActivityMainBinding mainBinding;
    private String msg = "AuthType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mainBinding.button.setOnClickListener(this);
        mainBinding.button3.setOnClickListener(this);
        mainBinding.Gsignin.setOnClickListener(this);
        mainBinding.phonesignup.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                LoginStart();

                break;
            case R.id.button3:
                SignUpStart();
                Toast.makeText(this, "Clicked SignUp", Toast.LENGTH_LONG).show();
                break;
            case R.id.Gsignin:
                GSignin();
            case R.id.phonesignup:
                Intent phoneintent = new Intent(MainActivity.this, PhoneAuth.class);
                startActivity(phoneintent);
        }
    }

    private void GSignin() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("283190405868-vbkd9qkaapg2evgrbeilggn22vv4q7b7.apps.googleusercontent.com")
                .build();
        GLoginStart();

    }

    private void GLoginStart() {
        GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(this, gso);
        Intent GsignIn = mGoogleClient.getSignInIntent();
        startActivityForResult(GsignIn, 1);
    }

    private void SignUpStart() {
        Intent SignupIntent = new Intent(MainActivity.this, Register.class);
        SignupIntent.putExtra(msg, "SIGNUP");
        startActivity(SignupIntent);
    }

    private void LoginStart() {
        Intent loginintent = new Intent(MainActivity.this, Register.class);
        loginintent.putExtra(msg, "LOGIN");
        startActivity(loginintent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthGoogle" + account.getId());
                firebaseGAuth(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Failed", e);
            }
        }
    }

    private void firebaseGAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredentials:Success");
                            DbEntry();
                            OpenDashBboard();


                        }
                    }
                });
    }

    private void OpenDashBboard() {
        Intent dash = new Intent(MainActivity.this, DashBoard.class);
        startActivity(dash);
    }

    private void DbEntry() {

        String uid;
        uid = mAuth.getUid();
        User user = new User();
        user.setUsername(mAuth.getCurrentUser().getDisplayName());
        user.setEmail(mAuth.getCurrentUser().getEmail());
        user.setAcctype("Google");
        user.setDatecreated("00");
        mDatabase = mDatabase.child("Users").child(uid);
        mDatabase.setValue(user);

    }
}