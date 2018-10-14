package com.ruby.splitmoney.home;

import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void showTotal(int lentMoney, int borrowedMoney);
    }

    interface Presenter extends BasePresenter {

        void showTotal();

    }
}
