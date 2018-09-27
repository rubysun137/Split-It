package com.ruby.splitmoney.split;

public class SplitPresenter implements SplitContract.Presenter {

    private SplitContract.View mView;


    public SplitPresenter(SplitContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }
}
