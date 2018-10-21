package com.ruby.splitmoney.groupdetail;

import android.content.Context;

import com.ruby.splitmoney.objects.Event;

public class GroupDetailPresenter implements GroupDetailContract.Presenter {

    private GroupDetailContract.View mView;


    public GroupDetailPresenter(GroupDetailContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

    @Override
    public void setEventDetailPage(Event event) {
        mView.showEventDetailPage(event);
    }
}
