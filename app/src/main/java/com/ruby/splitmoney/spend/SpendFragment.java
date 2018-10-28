package com.ruby.splitmoney.spend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;


public class SpendFragment extends Fragment implements SpendContract.View {

    private SpendContract.Presenter mPresenter;


    public SpendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spend, container, false);


        mPresenter = new SpendPresenter(this);


        mPresenter.start();

        return view;
    }


    @Override
    public void setPresenter(SpendContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
