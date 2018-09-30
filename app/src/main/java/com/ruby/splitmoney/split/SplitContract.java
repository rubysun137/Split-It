package com.ruby.splitmoney.split;

import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

public interface SplitContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void transToAddList();
    }
}
