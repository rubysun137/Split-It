package com.ruby.splitmoney.split;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.friend.FriendFragment;
import com.ruby.splitmoney.group.GroupFragment;

import java.util.ArrayList;
import java.util.List;


public class SplitFragment extends Fragment implements SplitContract.View,View.OnClickListener {

    private SplitContract.Presenter mPresenter;
    private List<Fragment> mFragmentList;
    private FriendFragment mFriendFragment;
    private GroupFragment mGroupFragment;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private FloatingActionMenu mFab;
    private FloatingActionButton mAddListFab;
    private FloatingActionButton mAddFriendFab;
    private FloatingActionButton mAddGroupFab;


    public SplitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_split, container, false);
        mPresenter = new SplitPresenter(this);
        mPresenter.start();

        mFragmentList = new ArrayList<>();
        mGroupFragment = new GroupFragment();
        mFriendFragment = new FriendFragment();

        mFragmentList.add(mGroupFragment);
        mFragmentList.add(mFriendFragment);

        mPager = view.findViewById(R.id.viewPagerHolder);

        mPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

        mTabLayout = view.findViewById(R.id.tabLayout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mFab = view.findViewById(R.id.fab);
        mFab.setClosedOnTouchOutside(true);

        mAddListFab = view.findViewById(R.id.fab_add_list);
        mAddFriendFab = view.findViewById(R.id.fab_add_friend);
        mAddGroupFab = view.findViewById(R.id.fab_add_group);



        mAddListFab.setOnClickListener(this);
        mAddFriendFab.setOnClickListener(this);
        mAddGroupFab.setOnClickListener(this);

        return view;
    }



    @Override
    public void setPresenter(SplitContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_list:
                mPresenter.transToAddList();
                mFab.close(true);
                break;
            case R.id.fab_add_friend:
                mFab.close(true);
                break;
            case R.id.fab_add_group:
                mFab.close(true);
                break;
        }
    }
}
