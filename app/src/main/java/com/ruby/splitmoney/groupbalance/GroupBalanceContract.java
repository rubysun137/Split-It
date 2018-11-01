package com.ruby.splitmoney.groupbalance;

import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;
import java.util.Map;

public interface GroupBalanceContract {

    interface View extends BaseView<Presenter> {

        void showNoEventSign();

        void updateBalance(Map<String, Double> moneyMap);

        void showSettleUpDialog(int position, List<Friend> friendList, List<Double> moneyList);

    }

    interface Presenter extends BasePresenter {

        void calculateMoney(Group group);

        void settleUp(int position, List<Friend> friendList, List<Double> moneyList);

        void setSettleUpToFirebase(Double settleMoney, Friend payMoneyFriend, Friend getMoneyFriend);
    }
}
