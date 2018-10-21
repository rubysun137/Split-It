package com.ruby.splitmoney.groupdetail;

import android.content.Context;

public class GroupDetailPresenter implements GroupDetailContract.Presenter {

    private GroupDetailContract.View mView;


    public GroupDetailPresenter(GroupDetailContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

}
