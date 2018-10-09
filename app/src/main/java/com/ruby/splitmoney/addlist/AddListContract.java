package com.ruby.splitmoney.addlist;

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

        void setListSize(int totalMember);

        void saveSplitResultToFirebase(String event, List<Friend> friends,String whoPays, int money, int tipPercent, String date);
    }
}
