package com.ruby.splitmoney;

public interface MainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void transToMain();

        void transToQuickSplit();

    }
}
