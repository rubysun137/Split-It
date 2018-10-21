package com.ruby.splitmoney.groupexpense;

import com.ruby.splitmoney.groupdetail.GroupDetailContract;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface GroupExpenseContract {

    interface View extends BaseView<Presenter> {

        void showEventDetailPage(Event event);

        void showEventList(List<Event> eventList);
    }

    interface Presenter extends BasePresenter {

        void setEventDetailPage(Event event);

        void setEventList(Group group);
    }
}
