package com.ruby.splitmoney;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ruby.splitmoney.quicksplit.QuickSplitFragment;
import com.ruby.splitmoney.quicksplit.QuickSplitPresenter;
import com.ruby.splitmoney.util.Constants;

public class MainPresenter implements MainContract.Presenter {

    private FragmentManager mFragmentManager;
    private MainContract.View mView;

    private QuickSplitFragment mQuickSplitFragment;
    private QuickSplitPresenter mQuickSplitPresenter;

    public MainPresenter(MainContract.View view, FragmentManager fragmentManager) {
        mView = view;
        mView.setPresenter(this);
        mFragmentManager = fragmentManager;
    }

    @Override
    public void start() {
        transToQuickSplit();
    }

    @Override
    public void transToMain() {

    }

    @Override
    public void transToQuickSplit() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        mQuickSplitFragment = new QuickSplitFragment();
//        mQuickSplitPresenter = new SplitPresenter();
        transaction.add(R.id.main_activity_placeholder, mQuickSplitFragment, Constants.QUICK);
        transaction.commit();
    }
}
