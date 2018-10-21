package com.ruby.splitmoney;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ruby.splitmoney.addgroup.AddGroupFragment;
import com.ruby.splitmoney.addlist.AddListFragment;
import com.ruby.splitmoney.frienddetail.FriendDetailFragment;
import com.ruby.splitmoney.groupdetail.GroupDetailFragment;
import com.ruby.splitmoney.home.HomeFragment;
import com.ruby.splitmoney.listdetail.ListDetailFragment;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.quicksplit.QuickSplitFragment;
import com.ruby.splitmoney.spend.SpendFragment;
import com.ruby.splitmoney.split.SplitFragment;
import com.ruby.splitmoney.util.Constants;

public class MainPresenter implements MainContract.Presenter {

    private FragmentManager mFragmentManager;
    private MainContract.View mView;

    private HomeFragment mHomeFragment;
    private SpendFragment mSpendFragment;
    private SplitFragment mSplitFragment;
    private QuickSplitFragment mQuickSplitFragment;
    private FriendDetailFragment mFriendDetailFragment;
    private AddListFragment mAddListFragment;
    private AddGroupFragment mAddGroupFragment;
    private ListDetailFragment mListDetailFragment;
    private GroupDetailFragment mGroupDetailFragment;

    public MainPresenter(MainContract.View view, FragmentManager fragmentManager) {
        mView = view;
        mView.setPresenter(this);
        mFragmentManager = fragmentManager;
        checkFragment();
    }

    private void checkFragment() {
        if (mFragmentManager.findFragmentByTag(Constants.HOME) != null)
            mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(Constants.HOME);
        if (mFragmentManager.findFragmentByTag(Constants.SPEND) != null)
        mSpendFragment = (SpendFragment) mFragmentManager.findFragmentByTag(Constants.SPEND);
        if (mFragmentManager.findFragmentByTag(Constants.SPLIT) != null)
        mSplitFragment = (SplitFragment) mFragmentManager.findFragmentByTag(Constants.SPLIT);
        if (mFragmentManager.findFragmentByTag(Constants.QUICK) != null)
        mQuickSplitFragment = (QuickSplitFragment) mFragmentManager.findFragmentByTag(Constants.QUICK);
        if (mFragmentManager.findFragmentByTag(Constants.FRIEND_DETAIL) != null)
        mFriendDetailFragment = (FriendDetailFragment) mFragmentManager.findFragmentByTag(Constants.FRIEND_DETAIL);
        if (mFragmentManager.findFragmentByTag(Constants.ADD_LIST) != null)
        mAddListFragment = (AddListFragment) mFragmentManager.findFragmentByTag(Constants.ADD_LIST);
        if (mFragmentManager.findFragmentByTag(Constants.LIST_DETAIL) != null)
        mListDetailFragment = (ListDetailFragment) mFragmentManager.findFragmentByTag(Constants.LIST_DETAIL);
        if (mFragmentManager.findFragmentByTag(Constants.ADD_GROUP) != null)
        mAddGroupFragment = (AddGroupFragment) mFragmentManager.findFragmentByTag(Constants.ADD_GROUP);
        if (mFragmentManager.findFragmentByTag(Constants.GROUP_DETAIL) != null)
        mGroupDetailFragment = (GroupDetailFragment) mFragmentManager.findFragmentByTag(Constants.GROUP_DETAIL);
    }

    @Override
    public void start() {
        transToHome();
    }

    @Override
    public void transToHome() {
        mView.setToolBarTitle("首頁");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mAddListFragment != null) {
            transaction.remove(mAddListFragment);
            mFragmentManager.popBackStack();
        }
        if (mFriendDetailFragment != null) {
            transaction.remove(mFriendDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mListDetailFragment != null) {
            transaction.remove(mListDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mSpendFragment != null) transaction.hide(mSpendFragment);
        if (mSplitFragment != null) transaction.hide(mSplitFragment);
        if (mQuickSplitFragment != null) transaction.hide(mQuickSplitFragment);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            transaction.add(R.id.main_activity_placeholder, mHomeFragment, Constants.HOME);
        } else {
            transaction.show(mHomeFragment);
        }
        transaction.commit();

    }

    @Override
    public void transToSpend() {
        mView.setToolBarTitle("記帳");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mAddListFragment != null) {
            transaction.remove(mAddListFragment);
            mFragmentManager.popBackStack();
        }
        if (mFriendDetailFragment != null) {
            transaction.remove(mFriendDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mListDetailFragment != null) {
            transaction.remove(mListDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mHomeFragment != null) transaction.hide(mHomeFragment);
        if (mSplitFragment != null) transaction.hide(mSplitFragment);
        if (mQuickSplitFragment != null) transaction.hide(mQuickSplitFragment);
        if (mSpendFragment == null) {
            mSpendFragment = new SpendFragment();
            transaction.add(R.id.main_activity_placeholder, mSpendFragment, Constants.SPEND);
        } else {
            transaction.show(mSpendFragment);
        }
        transaction.commit();
    }

    @Override
    public void transToSplit(boolean transToFriend) {
        mView.setToolBarTitle("拆帳");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mAddListFragment != null) {
            transaction.remove(mAddListFragment);
            mFragmentManager.popBackStack();
        }
        if (mFriendDetailFragment != null) {
            transaction.remove(mFriendDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mListDetailFragment != null) {
            transaction.remove(mListDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mHomeFragment != null) transaction.hide(mHomeFragment);
        if (mSpendFragment != null) transaction.hide(mSpendFragment);
        if (mQuickSplitFragment != null) transaction.hide(mQuickSplitFragment);
        if (mSplitFragment == null) {
            mSplitFragment = new SplitFragment();
            transaction.add(R.id.main_activity_placeholder, mSplitFragment, Constants.SPLIT);
        } else {
            transaction.show(mSplitFragment);
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("friendPage", transToFriend);
        mSplitFragment.setArguments(bundle);
        transaction.commit();
    }

    @Override
    public void transToQuickSplit() {
        mView.setToolBarTitle("快速拆帳");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mAddListFragment != null) {
            transaction.remove(mAddListFragment);
            mFragmentManager.popBackStack();
        }
        if (mFriendDetailFragment != null) {
            transaction.remove(mFriendDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mListDetailFragment != null) {
            transaction.remove(mListDetailFragment);
            mFragmentManager.popBackStack();
        }

        if (mHomeFragment != null) transaction.hide(mHomeFragment);
        if (mSpendFragment != null) transaction.hide(mSpendFragment);
        if (mSplitFragment != null) transaction.hide(mSplitFragment);
        if (mQuickSplitFragment == null) {
            mQuickSplitFragment = new QuickSplitFragment();
            transaction.add(R.id.main_activity_placeholder, mQuickSplitFragment, Constants.QUICK);
        } else {
            transaction.show(mQuickSplitFragment);
        }
        transaction.commit();
    }

    @Override
    public void changeTheme() {
        mView.showNewTheme();
    }

    @Override
    public void transToFriendDetailPage(String friendName) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mHomeFragment != null && !mHomeFragment.isHidden()) {
            transaction.hide(mHomeFragment);
            transaction.addToBackStack(Constants.HOME);
        }
        if (mSpendFragment != null && !mSpendFragment.isHidden()) {
            transaction.hide(mSpendFragment);
            transaction.addToBackStack(Constants.SPEND);
        }
        if (mSplitFragment != null && !mSplitFragment.isHidden()) {
            transaction.hide(mSplitFragment);
            transaction.addToBackStack(Constants.SPLIT);
        }
        if (mQuickSplitFragment != null && !mQuickSplitFragment.isHidden()) {
            transaction.hide(mQuickSplitFragment);
            transaction.addToBackStack(Constants.QUICK);
        }

        mFriendDetailFragment = new FriendDetailFragment();
        Bundle args = new Bundle();
        args.putString("name", friendName);
        mFriendDetailFragment.setArguments(args);
        transaction.add(R.id.fullPagePlaceHolder, mFriendDetailFragment, Constants.FRIEND_DETAIL);
        transaction.commit();

    }

    @Override
    public void transToAddListPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mHomeFragment != null && !mHomeFragment.isHidden()) {
            transaction.hide(mHomeFragment);
            transaction.addToBackStack(Constants.HOME);
        }
        if (mSpendFragment != null && !mSpendFragment.isHidden()) {
            transaction.hide(mSpendFragment);
            transaction.addToBackStack(Constants.SPEND);
        }
        if (mSplitFragment != null && !mSplitFragment.isHidden()) {
            transaction.hide(mSplitFragment);
            transaction.addToBackStack(Constants.SPLIT);
        }
        if (mQuickSplitFragment != null && !mQuickSplitFragment.isHidden()) {
            transaction.hide(mQuickSplitFragment);
            transaction.addToBackStack(Constants.QUICK);
        }


        mAddListFragment = new AddListFragment();
        transaction.add(R.id.fullPagePlaceHolder, mAddListFragment, Constants.ADD_LIST);
        transaction.commit();
    }

    @Override
    public void transToAddGroupPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mHomeFragment != null && !mHomeFragment.isHidden()) {
            transaction.hide(mHomeFragment);
            transaction.addToBackStack(Constants.HOME);
        }
        if (mSpendFragment != null && !mSpendFragment.isHidden()) {
            transaction.hide(mSpendFragment);
            transaction.addToBackStack(Constants.SPEND);
        }
        if (mSplitFragment != null && !mSplitFragment.isHidden()) {
            transaction.hide(mSplitFragment);
            transaction.addToBackStack(Constants.SPLIT);
        }
        if (mQuickSplitFragment != null && !mQuickSplitFragment.isHidden()) {
            transaction.hide(mQuickSplitFragment);
            transaction.addToBackStack(Constants.QUICK);
        }


        mAddGroupFragment = new AddGroupFragment();
        transaction.add(R.id.fullPagePlaceHolder, mAddGroupFragment, Constants.ADD_GROUP);
        transaction.commit();
    }

    @Override
    public void transToListDetailPage(Event event) {
        mView.setToolBarTitle("");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mHomeFragment != null && !mHomeFragment.isHidden()) {
            transaction.hide(mHomeFragment);
            transaction.addToBackStack(Constants.HOME);
        }
        if (mSpendFragment != null && !mSpendFragment.isHidden()) {
            transaction.hide(mSpendFragment);
            transaction.addToBackStack(Constants.SPEND);
        }
        if (mSplitFragment != null && !mSplitFragment.isHidden()) {
            transaction.hide(mSplitFragment);
            transaction.addToBackStack(Constants.SPLIT);
        }
        if (mQuickSplitFragment != null && !mQuickSplitFragment.isHidden()) {
            transaction.hide(mQuickSplitFragment);
            transaction.addToBackStack(Constants.QUICK);
        }
        if (mFriendDetailFragment != null && !mFriendDetailFragment.isHidden()) {
            transaction.hide(mFriendDetailFragment);
            transaction.addToBackStack(Constants.FRIEND_DETAIL);
        }


        mListDetailFragment = new ListDetailFragment();
        mListDetailFragment.setEvent(event);
        transaction.add(R.id.fullPagePlaceHolder, mListDetailFragment, Constants.LIST_DETAIL);
        transaction.commit();
    }

    @Override
    public void transToGroupDetailPage(String groupId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mHomeFragment != null && !mHomeFragment.isHidden()) {
            transaction.hide(mHomeFragment);
            transaction.addToBackStack(Constants.HOME);
        }
        if (mSpendFragment != null && !mSpendFragment.isHidden()) {
            transaction.hide(mSpendFragment);
            transaction.addToBackStack(Constants.SPEND);
        }
        if (mSplitFragment != null && !mSplitFragment.isHidden()) {
            transaction.hide(mSplitFragment);
            transaction.addToBackStack(Constants.SPLIT);
        }
        if (mQuickSplitFragment != null && !mQuickSplitFragment.isHidden()) {
            transaction.hide(mQuickSplitFragment);
            transaction.addToBackStack(Constants.QUICK);
        }

        mGroupDetailFragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putString("id", groupId);
        mGroupDetailFragment.setArguments(args);
        transaction.add(R.id.fullPagePlaceHolder, mGroupDetailFragment, Constants.GROUP_DETAIL);
        transaction.commit();
    }

}
