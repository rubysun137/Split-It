package com.ruby.splitmoney.addlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;


public class AddListFragment extends Fragment implements AddListContract.View {

    private AddListContract.Presenter mPresenter;


    public AddListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);


        mPresenter = new AddListPresenter(this);


        mPresenter.start();

        return view;
    }



    @Override
    public void setPresenter(AddListContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
