package com.ruby.splitmoney.quicksplit;

import com.ruby.splitmoney.BasePresenter;
import com.ruby.splitmoney.BaseView;

import java.util.List;

public interface QuickSplitContract {

    interface View extends BaseView<Presenter> {
        void showFirstPage();

        void showSecondPage();

        void showEqualResult(double money);

        void showUnequalResult(List<Double> results);

        void showSharedResult(List<Double> results);

    }

    interface Presenter extends BasePresenter {

        void selectSplitType(int spitType);

        void toFirstPage();

        void toSecondPage();

        void addExtraMoneyList(int position, int extraMoney);

        void addSharedMoneyList(int position, int sharedMoney);

        void setListSize(int totalMember);

        void passValue(int totalMoney,int totalMember, int feePercentage);

        boolean isSharedListEmpty();
    }
}
