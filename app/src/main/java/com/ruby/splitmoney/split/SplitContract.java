package com.ruby.splitmoney.split;

import android.content.Context;

import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

public interface SplitContract {

    interface View extends BaseView<Presenter> {

        void closeAddFriendDialog(String name);

    }

    interface Presenter extends BasePresenter {

        void searchForFriend(String email, Context context);
    }
}
