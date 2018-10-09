package com.ruby.splitmoney.friend;

import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface FriendContract {

    interface View extends BaseView<Presenter> {

        void setFriendDetailPage(String friendName);

        void showFriendList();

    }

    interface Presenter extends BasePresenter {

        void transToFriendDetailPage(String friendName);

        void deleteFriendDialog(String friendUid);
    }
}
