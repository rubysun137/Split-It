package com.ruby.splitmoney.addlist;

import android.support.v4.app.FragmentManager;

import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface AddListContract {

    interface View extends BaseView<Presenter> {

        void showSplitFriendView(int position);

        void showCurrentDate(String date);

    }

    interface Presenter extends BasePresenter {

        void addSplitFriendView(int position);

        void setPayerSpinner();

        void getCurrentDate();

        void selectSplitType(int spitType);

        void addExtraMoneyList(int position, int extraMoney);

        void addSharedMoneyList(int position, int sharedMoney);

        void addFreeMoneyList(int position, int freeMoney);

        void setListSize(int totalMember);

        boolean isSharedListEmpty();

        int freeTotalMoney();

        void saveSplitResultToFirebase(String event, List<Friend> friends, String whoPays, int money, int tipPercent, String date, FragmentManager fragmentManager);
    }
}
