package com.ruby.splitmoney.friend;

import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface FriendContract {

    interface View extends BaseView<Presenter> {
        void setFriendDetailPage(String friendName);

    }

    interface Presenter extends BasePresenter {

        void transToFriendDetailPage(String friendName);
    }
}
