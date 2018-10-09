package com.ruby.splitmoney.frienddetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.FriendDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.List;


public class FriendDetailFragment extends Fragment implements FriendDetailContract.View, View.OnClickListener {

    private FriendDetailContract.Presenter mPresenter;
    private String mFriendName;
    private FriendDetailAdapter mFriendListAdapter;
    private TextView mNameTitle;
    private TextView mNameBig;
    private TextView mWhoOwe;
    private TextView mOweWho;
    private TextView mOweMoney;
    private List<Friend> mFriendList;
    private Friend mFriend;

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
        mWhoOwe = view.findViewById(R.id.friend_detail_who_owe);
        mOweWho = view.findViewById(R.id.friend_detail_owe_who);
        mOweMoney = view.findViewById(R.id.friend_detail_own_money);

        mNameTitle.setText(mFriendName);
        mNameBig.setText(mFriendName);
        mWhoOwe.setText(mFriendName);

        mFriendList = new ArrayList<>(FriendList.getInstance().getFriendList());
        for(Friend friend : mFriendList){
            if(friend.getName().equals(mFriendName)){
                mFriend = friend;
            }
        }
        RecyclerView recyclerView = view.findViewById(R.id.friend_detail_recycler_view);
        mFriendListAdapter = new FriendDetailAdapter(mPresenter, mFriend);
        mPresenter.loadEvents(mFriend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFriendListAdapter);


        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(FriendDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEvents(List<Event> events, List<Double> moneyList) {
        mFriendListAdapter.setEvents(events,moneyList);
        int balanceMoney = 0;
        for (Double money : moneyList){
            balanceMoney += money;
        }
        mOweMoney.setText(String.valueOf(balanceMoney));
    }
}
