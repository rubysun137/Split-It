package com.ruby.splitmoney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruby.splitmoney.util.BaseActivity;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    private LoginContract.Presenter mPresenter;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private boolean isLoading;
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
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            transToMain();
        }
        isLoading = false;
        mProgressbar = findViewById(R.id.progress_bar_loading);
        Glide.with(this).load(R.drawable.loading).into(mProgressbar);
        mSignInLayout = findViewById(R.id.login_page_container);
        mLoadingLayout = findViewById(R.id.loading_layout);
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

        mPresenter = new LoginPresenter(this, this);
        mPresenter.start();
    }

    private void signIn() {
        showLoadingPageUi();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!isLoading) {
            showLoginPageUi();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && data != null) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mPresenter.firebaseAuthWithGoogle(account);
            } else {
                backToLoginPageUi();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("CLICK!!!!", "onClick: ");
        switch (v.getId()) {
            case R.id.google_login_button:
                signIn();
                break;
            case R.id.login_page_container:
                //hide keyboard
                if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                    hideKeyboard();
                }
                break;
            case R.id.register_text_view_button:
                mPresenter.clickRegisterButton();
                break;
            case R.id.login_text_view_button:
                mPresenter.clickLoginButton();
                break;
            case R.id.forget_password:
                showForgetPasswordDialog();
                break;
            case R.id.send_friend_email:
                EditText mail = mDialogView.findViewById(R.id.add_friend_email);
                String userEmail = mail.getText().toString();
                if ("".equals(userEmail)) {
                    userEmail = "wrong";
                }
                mPresenter.sendForgetPasswordEmail(userEmail);
                break;
            case R.id.sendButton:
                Log.d("CLICK!!!!", "SEND! ");
                //hide keyboard
                if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                    hideKeyboard();
                }
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (!isLoading) {
                    mPresenter.clickSendButton(email, password);
                }
                break;
            case R.id.registerSendButton:
                if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                    hideKeyboard();
                }
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                String name = mName.getText().toString();
                if (!isLoading) {
                    mPresenter.clickRegisterSendButton(email, password, name);
                }
                break;
            default:
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
    }

    private void showForgetPasswordDialog() {
        mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null, false);
        mDialog = new AlertDialog.Builder(this)
                .setView(mDialogView)
                .show();
        mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        ImageView sendImage = mDialogView.findViewById(R.id.send_friend_email);
        sendImage.setImageResource(R.drawable.send_icon2);
        sendImage.setOnClickListener(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoginPageUi() {
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

    @Override
    public void transToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        isLoading = false;
    }

    @Override
    public void showLoginGoogleFailMessage(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        showLongToastMessage("登入失敗，請確認連線狀態後再登入一次");
        isLoading = false;
        mSignInLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showRegisterUi() {
        mNameLayout.setVisibility(View.VISIBLE);
        mRegisterSendButton.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.VISIBLE);
        mSendButton.setVisibility(View.GONE);
        mForgetPassword.setVisibility(View.GONE);
        mRegistrationButton.setVisibility(View.GONE);
        mGoogleSignInButton.setVisibility(View.GONE);
    }

    @Override
    public void showShortToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingPageUi() {
        isLoading = true;
        mSignInLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void completeLoading() {
        isLoading = false;
    }

    @Override
    public void backToLoginPageUi() {
        isLoading = false;
        mSignInLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }
}
