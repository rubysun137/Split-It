package com.ruby.splitmoney.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;


public class HomeFragment extends Fragment implements HomeContract.View, View.OnClickListener {

    private HomeContract.Presenter mPresenter;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mPresenter = new HomePresenter(this);


        mPresenter.start();



        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
