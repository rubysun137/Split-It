package com.ruby.splitmoney;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.User;
import com.ruby.splitmoney.util.Constants;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;


    public LoginPresenter(LoginContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);
        mContext = context;
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void start() {

    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        mFirebaseUser.getDisplayName();
                        mFirebaseUser.getEmail();
                        mFirebaseUser.getUid();
                        User user = new User(mFirebaseUser.getEmail(),
                                mFirebaseUser.getDisplayName(),
                                mFirebaseUser.getUid(),
                                String.valueOf(mFirebaseUser.getPhotoUrl()));
                        FirebaseFirestore.getInstance().collection(Constants.USERS)
                                .document(mFirebaseUser.getUid()).set(user);
                    }
                    mView.transToMain();

                } else {
                    mView.showLoginGoogleFailMessage(task.getException().getLocalizedMessage());

                }

            }
        });
    }

    @Override
    public void clickRegisterButton() {
        mView.showRegisterUi();
    }

    @Override
    public void clickLoginButton() {
        mView.showLoginPageUi();
    }

    @Override
    public void sendForgetPasswordEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mView.showShortToastMessage("已傳送密碼重置信件到 Email");
                        } else {
                            mView.showShortToastMessage("Email 輸入錯誤");
                        }
                    }
                });
    }

    @Override
    public void clickSendButton(String email, String password) {
        if (!"".equals(email) && !"".equals(password)) {
            mView.showLoadingPageUi();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mView.completeLoading();
                    if (task.isSuccessful()) {
                        Log.d("Login ", "signInWithEmail:success");
                        mView.transToMain();
                    } else {
                        mView.backToLoginPageUi();
                        Log.w("Login ", "signInWithEmail:failure",
                                task.getException());
                        mView.showLongToastMessage(task.getException().getLocalizedMessage());
//                        Toast.makeText(LoginActivity.this, "登入失敗，請再次確認帳號與密碼是否正確!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            mView.showShortToastMessage("請輸入帳號與密碼");
        }
    }

    @Override
    public void clickRegisterSendButton(String email, String password, final String name) {
        if (!"".equals(email) && !"".equals(password) && !"".equals(name)) {
            mView.showLoadingPageUi();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mView.completeLoading();
                    if (task.isSuccessful()) {
                        Log.d("SighUp ", "signUpWithEmail:success");
                        addUser(name);
                        mView.showShortToastMessage("註冊成功");
                    } else {
                        if (task.getException() != null) {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            Log.d("SighUp ", "SighUpWithEmail:failure" + errorCode);
                            mView.backToLoginPageUi();
                            if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                mView.showLongToastMessage("Email 已被註冊過");
                            } else if (errorCode.equals("ERROR_WEAK_PASSWORD")) {
                                mView.showLongToastMessage("密碼強度不足，請輸入至少六位數字或英文字母");
                            } else if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                                mView.showLongToastMessage("Email 格式錯誤");
                            } else {
                                mView.showLongToastMessage("註冊失敗，請重新註冊!");
                            }
                        } else {
                            mView.showLongToastMessage("註冊失敗，請重新註冊!");
                        }
                    }
                }
            });

        } else {
            mView.showShortToastMessage("請輸入帳號、密碼與暱稱");
        }
    }

    private void addUser(String name) {
        mFirebaseUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        mFirebaseUser.updateProfile(profileChange).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void voidA) {
                        mView.transToMain();
                        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        User user = new User(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(),
                                mFirebaseUser.getUid(), null);
                        FirebaseFirestore.getInstance().collection(Constants.USERS)
                                .document(mFirebaseUser.getUid()).set(user);
                    }
                });
    }

}
