package com.ruby.splitmoney;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRegistrar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.User;
import com.ruby.splitmoney.util.BaseActivity;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mSendButton;
    private boolean mIsLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
//            firebaseUser.getDisplayName();
//            firebaseUser.getEmail();
//            firebaseUser.getUid();
//            User user = new User(firebaseUser.getEmail(),firebaseUser.getDisplayName(),firebaseUser.getUid(),"default");
//            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid()).set(user);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        mIsLoading = false;
        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (!"".equals(email) && !"".equals(password)) {
                    if (!mIsLoading) {
                        mIsLoading = true;
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Login ", "signInWithEmail:success");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.w("Login ", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Fail to sign in. Please check your email and password again!",
                                            Toast.LENGTH_LONG).show();
                                }
                                mIsLoading = false;
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Email or Password is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
