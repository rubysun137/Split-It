package com.ruby.splitmoney.addlist;

public class AddListPresenter implements AddListContract.Presenter {

    private AddListContract.View mView;


    public AddListPresenter(AddListContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }
}
