package com.ruby.splitmoney.group;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupPresenter implements GroupContract.Presenter {

    private GroupContract.View mView;


    public GroupPresenter(GroupContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }
}
