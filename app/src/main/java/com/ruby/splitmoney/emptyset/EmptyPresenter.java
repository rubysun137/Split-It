package com.ruby.splitmoney.emptyset;

import android.content.Context;

public class EmptyPresenter implements EmptyContract.Presenter {

    private EmptyContract.View mView;


    public EmptyPresenter(EmptyContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

}
