package com.ruby.splitmoney.quicksplit;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSplitPresenter implements QuickSplitContract.Presenter {

    private QuickSplitContract.View mView;
    private List<Integer> mExtraMoney;
    private List<Double> mCountExtra;
    private int mSplitType;
    private int mTotalMoney;
    private int mTotalMember;
    private int mFeePercentage;

    public QuickSplitPresenter(QuickSplitContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }

    @Override
    public void start() {
        toFirstPage();
    }

    @Override
    public void selectSplitType(int spitType) {
        mSplitType = spitType;
    }

    @Override
    public void toFirstPage() {
        mView.showFirstPage();
    }

    @Override
    public void toSecondPage() {
        mView.showSecondPage();
        switch (mSplitType) {
            default:
            case 0:
                double money = mTotalMoney * (1 + ((double) mFeePercentage / 100)) / mTotalMember;
                money = (double) Math.round(money * 100) / 100;
                mView.showEqualResult(money);
                break;
            case 1:
                int moneyUnequal = mTotalMoney;
                mCountExtra = new ArrayList<>();

                for (int i = 0; i < mTotalMember; i++) {
                    moneyUnequal -= mExtraMoney.get(i);
                }


                for (int i = 0; i < mTotalMember; i++) {
                    double math = ((moneyUnequal / mTotalMember) + mExtraMoney.get(i)) * (1 + ((double) mFeePercentage / 100));
                    math = Math.round(math*100)/100;
                    mCountExtra.add(i, math);
                }

                mView.showUnequalResult(mCountExtra);


                break;
            case 2:
                break;
        }
    }

    @Override
    public void addExtraMoneyList(int position, int extraMoney) {
        mExtraMoney.set(position, extraMoney);
        Log.d("LIST SIZE ", "addExtraMoneyList: " + mExtraMoney.size());
    }

    @Override
    public void setListSize(int totalMember) {
        Integer[] members = new Integer[totalMember];

        for (int i = 0; i < totalMember; i++) {
            members[i] = 0;
        }

        mExtraMoney = Arrays.asList(members);
    }

    @Override
    public void passValue(int totalMoney, int totalMember, int feePercentage) {
        mTotalMoney = totalMoney;
        mTotalMember = totalMember;
        mFeePercentage = feePercentage;

    }


}
