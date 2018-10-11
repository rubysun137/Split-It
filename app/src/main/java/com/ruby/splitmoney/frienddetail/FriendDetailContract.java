package com.ruby.splitmoney.frienddetail;

import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;
import java.util.Map;

public interface FriendDetailContract {

    interface View extends BaseView<Presenter> {

        void showEvents(List<Event> events,List<Double> moneyList,Map<Event,Double> map);

    }

    interface Presenter extends BasePresenter {

        void loadEvents(Friend friend);

    }
}
