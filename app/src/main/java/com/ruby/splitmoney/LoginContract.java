package com.ruby.splitmoney;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showLoginPageUi();

        void transToMain();

        void showLoginGoogleFailMessage(String errorMessage);

        void showRegisterUi();

        void showShortToastMessage(String message);

        void showLongToastMessage(String message);

        void showLoadingPageUi();

        void completeLoading();

        void backToLoginPageUi();
    }

    interface Presenter extends BasePresenter {

        void firebaseAuthWithGoogle(GoogleSignInAccount account);

        void clickRegisterButton();

        void clickLoginButton();

        void sendForgetPasswordEmail(String email);

        void clickSendButton(String email, String password);

        void clickRegisterSendButton(String email, String password,String name);

    }
}
