package com.ruby.splitmoney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.User;
import com.ruby.splitmoney.util.BaseActivity;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private boolean mIsLoading;
    private FirebaseUser mFirebaseUser;
    private ConstraintLayout mSignInLayout;
    private LinearLayout mLoadingLayout;
    private ImageView mProgressBarImage;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mSignInOptions;
    private static int RC_SIGN_IN = 100;
    private Button mGoogleSignInButton;
    private TextView mRegistrationButton;
    private TextView mSendButton;
    private TextView mForgetPassword;
    private TextInputLayout mNameLayout;
    private TextView mRegisterSendButton;
    private TextView mLoginButton;
    private View mDialogView;
    private Dialog mDialog;
    private ImageView mProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        mIsLoading = false;
        mProgressbar = findViewById(R.id.progress_bar_loading);
        Glide.with(this).load(R.drawable.loading).into(mProgressbar);
        mSignInLayout = findViewById(R.id.login_page_container);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mSignInLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        mName = findViewById(R.id.nameText);
        mPassword.setTypeface(Typeface.DEFAULT);
        mPassword.setTransformationMethod(new PasswordTransformationMethod());
        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(this);
        mSignInLayout.setOnClickListener(this);

        mGoogleSignInButton = findViewById(R.id.google_login_button);
        mGoogleSignInButton.setOnClickListener(this);

        mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mSignInOptions)
                .build();

        mRegistrationButton = findViewById(R.id.register_text_view_button);
        mRegistrationButton.setOnClickListener(this);
        mForgetPassword = findViewById(R.id.forget_password);
        mForgetPassword.setOnClickListener(this);

        mNameLayout = findViewById(R.id.name_text_layout);
        mRegisterSendButton = findViewById(R.id.registerSendButton);
        mRegisterSendButton.setOnClickListener(this);
        mLoginButton = findViewById(R.id.login_text_view_button);
        mLoginButton.setOnClickListener(this);


    }

    private void signIn() {
        mIsLoading = true;
        mSignInLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(!mIsLoading) {
            mSignInLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
            mNameLayout.setVisibility(View.GONE);
            mRegisterSendButton.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.GONE);
            mSendButton.setVisibility(View.VISIBLE);
            mForgetPassword.setVisibility(View.VISIBLE);
            mRegistrationButton.setVisibility(View.VISIBLE);
            mGoogleSignInButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && data != null) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        mFirebaseUser.getDisplayName();
                        mFirebaseUser.getEmail();
                        mFirebaseUser.getUid();
                        User user = new User(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(), mFirebaseUser.getUid(), String.valueOf(mFirebaseUser.getPhotoUrl()));
                        FirebaseFirestore.getInstance().collection("users").document(mFirebaseUser.getUid()).set(user);

                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    mIsLoading = false;
                }else{
                    Toast.makeText(LoginActivity.this, "登入失敗，請再登入一次或是更換登入方式", Toast.LENGTH_SHORT).show();
                    mSignInLayout.setVisibility(View.VISIBLE);
                    mLoadingLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                signIn();
                break;
            case R.id.login_page_container:
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                break;
            case R.id.register_text_view_button:
                mNameLayout.setVisibility(View.VISIBLE);
                mRegisterSendButton.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.VISIBLE);
                mSendButton.setVisibility(View.GONE);
                mForgetPassword.setVisibility(View.GONE);
                mRegistrationButton.setVisibility(View.GONE);
                mGoogleSignInButton.setVisibility(View.GONE);
                break;
            case R.id.login_text_view_button:
                mNameLayout.setVisibility(View.GONE);
                mRegisterSendButton.setVisibility(View.GONE);
                mLoginButton.setVisibility(View.GONE);
                mSendButton.setVisibility(View.VISIBLE);
                mForgetPassword.setVisibility(View.VISIBLE);
                mRegistrationButton.setVisibility(View.VISIBLE);
                mGoogleSignInButton.setVisibility(View.VISIBLE);
                break;
            case R.id.forget_password:
                mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null, false);
                mDialog = new AlertDialog.Builder(this)
                        .setView(mDialogView)
                        .show();
                mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                ImageView sendImage = mDialogView.findViewById(R.id.send_friend_email);
                sendImage.setImageResource(R.drawable.send_icon2);
                sendImage.setOnClickListener(this);
                break;
            case R.id.send_friend_email:
                EditText mail = mDialogView.findViewById(R.id.add_friend_email);
                String friendEmail = mail.getText().toString();
                if(friendEmail.equals("")){
                    friendEmail = "wrong";
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(friendEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "已傳送密碼重置信件到 Email", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Email 輸入錯誤", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.sendButton:
                //hide keyboard
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                                    Log.d("Login ", "signInWithEmail:success");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    mSignInLayout.setVisibility(View.VISIBLE);
                                    mLoadingLayout.setVisibility(View.GONE);
                                    Log.w("Login ", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "登入失敗，請再次確認帳號與密碼是否正確!",
                                            Toast.LENGTH_LONG).show();
                                }
                                mIsLoading = false;
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "請輸入帳號與密碼", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.registerSendButton:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                String name = mName.getText().toString();
                if (!"".equals(email) && !"".equals(password) && !"".equals(name)) {
                    if (!mIsLoading) {
                        mSignInLayout.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mIsLoading = true;
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("SighUp ", "signUpWithEmail:success");

                                    addUser();


                                } else {
                                    Log.w("SighUp ", "SighUpWithEmail:failure", task.getException());
                                    mSignInLayout.setVisibility(View.VISIBLE);
                                    mLoadingLayout.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "註冊失敗，請重新註冊!",
                                            Toast.LENGTH_LONG).show();
                                }
                                mIsLoading = false;
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "請輸入帳號、密碼與暱稱", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void addUser() {
        mFirebaseUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString())
                .build();
        mFirebaseUser.updateProfile(profileChange).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                User user = new User(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(), mFirebaseUser.getUid(), null);
                FirebaseFirestore.getInstance().collection("users").document(mFirebaseUser.getUid()).set(user);
            }
        });
    }
}
