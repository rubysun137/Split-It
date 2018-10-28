package com.ruby.splitmoney.quicksplit;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSplitPresenter implements QuickSplitContract.Presenter {

    private QuickSplitContract.View mView;
    private List<Integer> mExtraMoney;
    private List<Integer> mSharedMoney;
    private List<Double> mCountExtra;
    private List<Double> mCountShared;
    private List<String> mMemberNames;
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
            case 1:
                double money = mTotalMoney * (1 + ((double) mFeePercentage / 100)) / mTotalMember;
                money = (double) Math.round(money * 100) / 100;
                mView.showEqualResult(money);
                break;
            case 2:
                int moneyUnequal = mTotalMoney;
                mCountExtra = new ArrayList<>();

                for (int i = 0; i < mTotalMember; i++) {
                    moneyUnequal -= mExtraMoney.get(i);
                }


                for (int i = 0; i < mTotalMember; i++) {
                    double math = (((double) moneyUnequal / mTotalMember) + mExtraMoney.get(i)) * (1 + ((double) mFeePercentage / 100));
                    math = ((double) Math.round(math * 100) / 100);
                    mCountExtra.add(i, math);
                }

                mView.showUnequalResult(mMemberNames, mCountExtra);


                break;
            case 3:
                mCountShared = new ArrayList<>();
                int sharedRate = 0;

                for (int i = 0; i < mTotalMember; i++) {
                    sharedRate += mSharedMoney.get(i);
                }

                double moneyShared = (double) mTotalMoney / sharedRate;

                for (int i = 0; i < mTotalMember; i++) {
                    if (mSharedMoney.get(i) != 0) {
                        double math = ((moneyShared * mSharedMoney.get(i)) * (1 + ((double) mFeePercentage / 100)));
                        math = ((double) Math.round(math * 100) / 100);
                        mCountShared.add(i, math);
                    } else {
                        mCountShared.add(i, 0.0);
                    }
                }

                mView.showSharedResult(mMemberNames, mCountShared);

                break;
        }
    }

    @Override
    public void addMemberNameList(int position, String member) {
        mMemberNames.set(position, member);
    }

    @Override
    public void addExtraMoneyList(int position, int extraMoney) {
        mExtraMoney.set(position, extraMoney);
        Log.d("LIST SIZE ", "addExtraMoneyList: " + mExtraMoney.size());
    }

    @Override
    public void addSharedMoneyList(int position, int sharedMoney) {
        mSharedMoney.set(position, sharedMoney);
    }

    @Override
    public void setListSize(int totalMember) {
        Integer[] members = new Integer[totalMember];

        for (int i = 0; i < totalMember; i++) {
            members[i] = 0;
        }

        mExtraMoney = Arrays.asList(members);
        mSharedMoney = Arrays.asList(members);

        mMemberNames = new ArrayList<>();
        for (int i = 0; i < totalMember; i++) {
            mMemberNames.add(i, "成員" + (i + 1));
        }
    }

    @Override
    public void passValue(int totalMoney, int totalMember, int feePercentage) {
        mTotalMoney = totalMoney;
        mTotalMember = totalMember;
        mFeePercentage = feePercentage;

    }

    @Override
    public boolean isSharedListEmpty() {
        int shareRate = 0;
        for (int i = 0; i < mSharedMoney.size(); i++) {
            shareRate += mSharedMoney.get(i);
        }
        if (shareRate == 0) {
            return true;
        } else {
            return false;
        }
    }


}
