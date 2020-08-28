package com.example.kapdaabazaar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;
    String uname;
    String mail;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText name;
    private EditText email;
    private EditText pass;
    private EditText confpass;
    private DatabaseReference mDatabase;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.NameInput);
        email = view.findViewById(R.id.EmailInput);
        pass = view.findViewById(R.id.PassInput);
        confpass = view.findViewById(R.id.ConfPass);
        Button signupbtn = view.findViewById(R.id.signupbtn);
        mAuth = FirebaseAuth.getInstance();
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pass.getText().toString().equals(confpass.getText().toString())) {
                    Toast.makeText(getContext(), "Password Mismatch!", Toast.LENGTH_LONG).show();
                } else {
                    String name = email.getText().toString();
                    String password = pass.getText().toString();
                    StartSignup(name, password);
                }
            }
        });
    }

    private void StartSignup(String toString, String toString1) {
        mAuth.createUserWithEmailAndPassword(toString, toString1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Account Created Successfully!", Toast.LENGTH_LONG).show();
                    uname = name.getText().toString();
                    mail = email.getText().toString();
                    FirebaseUser usr = mAuth.getCurrentUser();
                    UserProfileChangeRequest profchange = new UserProfileChangeRequest.Builder().setDisplayName(uname).build();
                    assert usr != null;
                    usr.updateProfile(profchange);
                    DbEntry();
                    OpenDashBoard();

                } else {
                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void DbEntry() {
        String uid;
        uid = mAuth.getUid();
        User user = new User();


        mDatabase = FirebaseDatabase.getInstance().getReference();

        user.setUsername(uname);
        user.setEmail(mail);
        user.setAcctype("Email");
        user.setDatecreated("00");
        assert uid != null;
        mDatabase = mDatabase.child("Users").child(uid);
        mDatabase.setValue(user);

    }

    private void OpenDashBoard() {
        Intent intent = new Intent(getContext(), DashBoard.class);
        startActivity(intent);
    }
}