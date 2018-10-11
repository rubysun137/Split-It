package com.ruby.splitmoney;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private FirebaseUser mFirebaseUser;
    private ConstraintLayout mConstraintLayout;
    private LinearLayout mSignInLayout;
    private LinearLayout mLoadingLayout;
    private ImageView mProgressBarImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
//            mFirebaseUser.getDisplayName();
//            mFirebaseUser.getEmail();
//            mFirebaseUser.getUid();
//            User user = new User(mFirebaseUser.getEmail(),mFirebaseUser.getDisplayName(),mFirebaseUser.getUid(),"default");
//            FirebaseFirestore.getInstance().collection("users").document(mFirebaseUser.getUid()).set(user);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        mIsLoading = false;
        mSignInLayout = findViewById(R.id.sign_in_layout);
        mSignInLayout.setVisibility(View.VISIBLE);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mLoadingLayout.setVisibility(View.GONE);
        mProgressBarImage = findViewById(R.id.progress_bar_loading);
        mProgressBarImage.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable animationDrawable = (AnimationDrawable) mProgressBarImage.getDrawable();
                if (!animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        mPassword.setTypeface(Typeface.DEFAULT);
        mPassword.setTransformationMethod(new PasswordTransformationMethod());
        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (!"".equals(email) && !"".equals(password)) {
                    if (!mIsLoading) {
                        mSignInLayout.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mIsLoading = true;
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    mFirebaseUser.getDisplayName();
//                                    mFirebaseUser.getEmail();
//                                    mFirebaseUser.getUid();
//                                    User user = new User(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(), mFirebaseUser.getUid(), "default");
//                                    FirebaseFirestore.getInstance().collection("users").document(mFirebaseUser.getUid()).set(user);

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
        mConstraintLayout = findViewById(R.id.container);
        mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mSignInLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }
}
