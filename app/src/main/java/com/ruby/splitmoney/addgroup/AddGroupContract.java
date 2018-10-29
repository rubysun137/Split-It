package com.ruby.splitmoney.addgroup;

import android.support.v4.app.FragmentManager;
import android.widget.EditText;

import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface AddGroupContract {

    interface View extends BaseView<Presenter> {

        void changeToAddedFriendList(Friend friend, int position);

        void changeNotAddFriendList(Friend friend, int position);

        void showAddFriendDialog();

        void showNoNameMessage();

        void showNoFriendMessage();

        void popBackStack();

        void showGroupMember();
    }

    interface Presenter extends BasePresenter {

        void addToAddedFriendList(Friend friend, int position);

        void addToNotAddFriendList(Friend friend, int position);

        void saveGroupData(String groupName, List<Friend> friends);

        void clickAddFriendToGroup();

        void clickSaveButton(EditText groupName, List<Friend> addedFriend);
    }
}
