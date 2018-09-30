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


public class FriendDetailFragment extends Fragment implements FriendDetailContract.View, View.OnClickListener {

    private FriendDetailContract.Presenter mPresenter;
    private String mFriendName;
    private FriendDetailAdapter mFriendListAdapter;
    private TextView mNameTitle;
    private TextView mNameBig;
    private TextView mWhoOwe;
    private TextView mOweWho;

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

        mNameTitle.setText(mFriendName);
        mNameBig.setText(mFriendName);
        mWhoOwe.setText(mFriendName);

        RecyclerView recyclerView = view.findViewById(R.id.friend_detail_recycler_view);
        mFriendListAdapter = new FriendDetailAdapter(mPresenter);
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
}
