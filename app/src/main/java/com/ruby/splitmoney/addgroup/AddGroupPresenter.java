package com.ruby.splitmoney.addgroup;

import com.ruby.splitmoney.objects.Friend;

public class AddGroupPresenter implements AddGroupContract.Presenter {

    private AddGroupContract.View mView;


    public AddGroupPresenter(AddGroupContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

    @Override
    public void addToAddedFriendList(Friend friend,int position) {
        mView.changeToAddedFriendList(friend, position);
    }

    @Override
    public void addToNotAddFriendList(Friend friend, int position) {
        mView.changeNotAddFriendList(friend,position);
    }
}
