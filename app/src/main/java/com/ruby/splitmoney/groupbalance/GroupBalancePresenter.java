package com.ruby.splitmoney.groupbalance;

import android.content.Context;

public class GroupBalancePresenter implements GroupBalanceContract.Presenter {

    private GroupBalanceContract.View mView;


    public GroupBalancePresenter(GroupBalanceContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

}
