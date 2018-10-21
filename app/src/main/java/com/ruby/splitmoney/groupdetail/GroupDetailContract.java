package com.ruby.splitmoney.groupdetail;

import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

public interface GroupDetailContract {

    interface View extends BaseView<Presenter> {

        void showEventDetailPage(Event event);
    }

    interface Presenter extends BasePresenter {

        void setEventDetailPage(Event event);
    }
}
