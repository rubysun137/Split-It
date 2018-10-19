package com.ruby.splitmoney.listdetail;

public class ListDetailPresenter implements ListDetailContract.Presenter {

    private ListDetailContract.View mView;


    public ListDetailPresenter(ListDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }
}
