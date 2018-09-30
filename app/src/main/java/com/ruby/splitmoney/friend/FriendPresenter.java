package com.ruby.splitmoney.friend;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendPresenter implements FriendContract.Presenter {

    private FriendContract.View mView;


    public FriendPresenter(FriendContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

    @Override
    public void transToFriendDetailPage(String friendName) {
        mView.setFriendDetailPage(friendName);
    }
}
