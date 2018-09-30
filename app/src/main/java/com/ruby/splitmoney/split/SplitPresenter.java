package com.ruby.splitmoney.split;

import android.support.v4.app.FragmentTransaction;

public class SplitPresenter implements SplitContract.Presenter {

    private SplitContract.View mView;


    public SplitPresenter(SplitContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

    @Override
    public void transToAddList() {
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
    }
}
