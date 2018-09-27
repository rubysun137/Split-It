package com.ruby.splitmoney.split;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;


public class SplitFragment extends Fragment implements SplitContract.View {

    private SplitContract.Presenter mPresenter;


    public SplitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_split, container, false);


        mPresenter = new SplitPresenter(this);


        mPresenter.start();

        return view;
    }



    @Override
    public void setPresenter(SplitContract.Presenter presenter) {

    }
}
