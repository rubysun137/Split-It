package com.ruby.splitmoney.groupexpense;

import android.content.Context;

import com.ruby.splitmoney.objects.Event;

public class GroupExpensePresenter implements GroupExpenseContract.Presenter {

    private GroupExpenseContract.View mView;


    public GroupExpensePresenter(GroupExpenseContract.View view, Context context) {
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
