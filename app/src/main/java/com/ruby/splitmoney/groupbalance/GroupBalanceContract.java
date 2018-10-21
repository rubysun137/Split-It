package com.ruby.splitmoney.groupbalance;

import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.Map;

public interface GroupBalanceContract {

    interface View extends BaseView<Presenter> {

        void showNoEventSign();

        void updateBalance(Map<String, Double> moneyMap);

    }

    interface Presenter extends BasePresenter {

        void calculateMoney(Group group);
    }
}
