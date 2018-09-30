package com.ruby.splitmoney.home;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;


    public HomePresenter(HomeContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }
}
