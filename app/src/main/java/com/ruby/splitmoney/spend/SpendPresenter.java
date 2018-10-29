package com.ruby.splitmoney.spend;

public class SpendPresenter implements SpendContract.Presenter {

    private SpendContract.View mView;


    public SpendPresenter(SpendContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {



    }
}
