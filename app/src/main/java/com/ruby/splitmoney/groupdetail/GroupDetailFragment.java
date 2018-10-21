package com.ruby.splitmoney.groupdetail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.GroupDetailAdapter;
import com.ruby.splitmoney.friend.FriendFragment;
import com.ruby.splitmoney.group.GroupFragment;
import com.ruby.splitmoney.groupbalance.GroupBalanceFragment;
import com.ruby.splitmoney.groupexpense.GroupExpenseFragment;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.FriendList;
import com.ruby.splitmoney.util.GroupList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;


public class GroupDetailFragment extends Fragment implements GroupDetailContract.View, View.OnClickListener {

    private GroupDetailContract.Presenter mPresenter;
    private Context mContext;
    private String mGroupId;
    private Group mGroup;
    private TextView mGroupName;
    private TextView mMemberName;
    private List<Friend> mGroupMembers;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;
    private TextView mNoEventText;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragmentList;
    private GroupExpenseFragment mExpenseFragment;
    private GroupBalanceFragment mBalanceFragment;


    public GroupDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
        mPresenter = new GroupDetailPresenter(this, container.getContext());
        mPresenter.start();
        mContext = container.getContext();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mGroupName = view.findViewById(R.id.group_detail_name);
        mGroupId = getArguments().getString("id", "");
        for (Group group : GroupList.getInstance().getGroupList()) {
            if (group.getId().equals(mGroupId)) {
                mGroup = group;
        mGroupName.setText(mGroup.getName());
            }
        }


        mMemberName = view.findViewById(R.id.member_list_text);



        mGroupMembers = new ArrayList<>();
//        mEventList = new ArrayList<>();
        Friend memberMe = new Friend(mUser.getEmail(),mUser.getUid(),mUser.getDisplayName(),null,mUser.getPhotoUrl().toString());
        mGroupMembers.add(memberMe);
        for (Friend friend : FriendList.getInstance().getFriendList()) {
            for (String memberId : mGroup.getMembers()) {
                if (friend.getUid().equals(memberId)) {
                    mGroupMembers.add(friend);
                }
            }
        }
        Collections.sort(mGroupMembers, new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (int i = 0; i < mGroupMembers.size(); i++) {
            if(i==0){
                mMemberName.setText(mGroupMembers.get(i).getName());
            }else{
                mMemberName.append(", " + mGroupMembers.get(i).getName());
            }
        }


        mFragmentList = new ArrayList<>();
        mExpenseFragment = new GroupExpenseFragment();
        mExpenseFragment.setGroup(mGroup);
        mBalanceFragment = new GroupBalanceFragment();
        mBalanceFragment.setGroup(mGroup);

        mFragmentList.add(mExpenseFragment);
        mFragmentList.add(mBalanceFragment);

        Log.d("FragmentList", "onCreateView:  " +mFragmentList.size() );
        mPager = view.findViewById(R.id.group_view_pager_holder);

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

        mTabLayout = view.findViewById(R.id.tab_layout);
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


        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onDestroyView() {
        getFragmentManager().beginTransaction().remove(mExpenseFragment).remove(mBalanceFragment).commitAllowingStateLoss();
        super.onDestroyView();
    }
}
