package com.ruby.splitmoney.frienddetail;

public class FriendDetailPresenter implements FriendDetailContract.Presenter {

    private FriendDetailContract.View mView;


    public FriendDetailPresenter(FriendDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }
}
