package com.ruby.splitmoney.addlist;

import android.support.v4.app.FragmentManager;

import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface AddListContract {

    interface View extends BaseView<Presenter> {

        void showSplitFriendView(int position);

        void showCurrentDate(String date);

        void setGroupList(List<Group> groupList);

        void setViewTypeSpinner();

        void showDateDialog();

        void popBackStack();

        void showToastMessage(String message);

        void saveData();

        void setSplitTypeDialog(int position);

        void changeToEvenSplitType();

        void dismissDialog();

    }

    interface Presenter extends BasePresenter {

        void addSplitFriendView(int position);

        void getCurrentDate();

        void selectSplitType(int spitType);

        void addExtraMoneyList(int position, int extraMoney);

        void addSharedMoneyList(int position, int sharedMoney);

        void addFreeMoneyList(int position, int freeMoney);

        void setListSize(int totalMember);

        boolean isSharedListEmpty();

        int getFreeTotalMoney();

        void saveSplitResultToFirebase(String event, List<Friend> friends, int whoPays, int money, int tipPercent, String date);

        void getGroups();

        void selectGroup(int position);

        void clickDate();

        void clickSaveButton(int friendSize, int totalMoney, String eventName);

        void clickDialogCorrectButton(int spinnerPosition, int totalMoney);

        void changeSplitType(String money, int friendSize);
    }
}
