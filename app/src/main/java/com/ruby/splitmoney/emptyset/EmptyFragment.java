package com.ruby.splitmoney.emptyset;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;


public class EmptyFragment extends Fragment implements EmptyContract.View, View.OnClickListener {

    private EmptyContract.Presenter mPresenter;
    private Context mContext;


    public EmptyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new EmptyPresenter(this, container.getContext());
        mPresenter.start();
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        mContext = container.getContext();
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(EmptyContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
