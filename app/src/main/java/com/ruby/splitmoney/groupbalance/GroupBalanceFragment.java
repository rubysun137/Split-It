package com.ruby.splitmoney.groupbalance;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;


public class GroupBalanceFragment extends Fragment implements GroupBalanceContract.View, View.OnClickListener {

    private GroupBalanceContract.Presenter mPresenter;
    private Context mContext;




    public GroupBalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        mPresenter = new GroupBalancePresenter(this, container.getContext());
        mPresenter.start();
        mContext = container.getContext();


        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupBalanceContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
