package com.ruby.splitmoney.frienddetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.FriendDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class FriendDetailFragment extends Fragment implements FriendDetailContract.View, View.OnClickListener {

    private FriendDetailContract.Presenter mPresenter;
    private String mFriendName;
    private FriendDetailAdapter mFriendListAdapter;
    private TextView mNameTitle;
    private TextView mNameBig;
    private TextView mClearBalance;
    private TextView mWhoOwe;
    private TextView mOweWho;
    private TextView mOweMoney;
    private List<Friend> mFriendList;
    private Friend mFriend;
    private LinearLayout mBalancedLayout;
    private LinearLayout mNotBalancedLayout;
    private LinearLayout mNoListLayout;

    public FriendDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_detail, container, false);
        mPresenter = new FriendDetailPresenter(this);
        mPresenter.start();
        mFriendName = getArguments().getString("name", "Friend");

        mNameTitle = view.findViewById(R.id.friend_detail_friend_title);
        mNameBig = view.findViewById(R.id.friend_detail_friend_name);
        mClearBalance = view.findViewById(R.id.friend_detail_clear_balance);
        mWhoOwe = view.findViewById(R.id.friend_detail_who_owe);
        mOweWho = view.findViewById(R.id.friend_detail_owe_who);
        mOweMoney = view.findViewById(R.id.friend_detail_own_money);
        mBalancedLayout = view.findViewById(R.id.money_is_balance_linear_layout);
        mNotBalancedLayout = view.findViewById(R.id.money_is_not_balance_linear_layout);
        mNoListLayout = view.findViewById(R.id.no_list_linear_layout);

        mNameTitle.setText(mFriendName);
        mNameBig.setText(mFriendName);
        mWhoOwe.setText(mFriendName);
        mNotBalancedLayout.setVisibility(View.GONE);
        mBalancedLayout.setVisibility(View.GONE);
        mNoListLayout.setVisibility(View.GONE);
        mClearBalance.setVisibility(View.GONE);
        mClearBalance.setOnClickListener(this);


        mFriendList = new ArrayList<>(FriendList.getInstance().getFriendList());
        for (Friend friend : mFriendList) {
            if (friend.getName().equals(mFriendName)) {
                mFriend = friend;
            }
        }
        RecyclerView recyclerView = view.findViewById(R.id.friend_detail_recycler_view);
        mFriendListAdapter = new FriendDetailAdapter(mPresenter, mFriend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFriendListAdapter);
        mPresenter.loadEvents(mFriend);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friend_detail_clear_balance:

                break;
        }
    }

    @Override
    public void setPresenter(FriendDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEvents(List<Event> events, List<Double> moneyList, Map<Event, Double> map) {

        if (events.size() == 0) {
            mNoListLayout.setVisibility(View.VISIBLE);
            mNotBalancedLayout.setVisibility(View.GONE);
            mBalancedLayout.setVisibility(View.GONE);
            mClearBalance.setVisibility(View.GONE);
        } else {
            mFriendListAdapter.setEvents(events, moneyList);
            double balanceMoney = 0;
            for (Double money : moneyList) {
                balanceMoney += money;
            }
            if (balanceMoney > 0) {
                mNoListLayout.setVisibility(View.GONE);
                mNotBalancedLayout.setVisibility(View.VISIBLE);
                mBalancedLayout.setVisibility(View.GONE);
                mWhoOwe.setText(mFriendName);
                mOweWho.setText("你");
                mOweMoney.setText(String.valueOf(balanceMoney));
                mClearBalance.setVisibility(View.VISIBLE);
            } else if (balanceMoney < 0) {
                mNoListLayout.setVisibility(View.GONE);
                mNotBalancedLayout.setVisibility(View.VISIBLE);
                mBalancedLayout.setVisibility(View.GONE);
                mWhoOwe.setText("你");
                mOweWho.setText(mFriendName);
                mOweMoney.setText(String.valueOf(0 - balanceMoney));
                mClearBalance.setVisibility(View.VISIBLE);
            } else {
                mNoListLayout.setVisibility(View.GONE);
                mNotBalancedLayout.setVisibility(View.GONE);
                mBalancedLayout.setVisibility(View.VISIBLE);
                mClearBalance.setVisibility(View.GONE);
            }
        }

    }
}
