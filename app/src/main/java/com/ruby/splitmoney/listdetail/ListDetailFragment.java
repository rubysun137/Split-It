package com.ruby.splitmoney.listdetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.ListDetailAdapter;
import com.ruby.splitmoney.objects.Event;


public class ListDetailFragment extends Fragment implements ListDetailContract.View, View.OnClickListener {

    private ListDetailContract.Presenter mPresenter;
    private Event mEvent;
    private TextView mTitle;
    private TextView mTotalMoney;
    private TextView mDate;
    private ListDetailAdapter mListDetailAdapter;

    public void setEvent(Event event){
        mEvent = event;
    }
    public ListDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_detail, container, false);
        mPresenter = new ListDetailPresenter(this);
        mPresenter.start();

        mTitle = view.findViewById(R.id.list_detail_name);
        mTotalMoney = view.findViewById(R.id.list_detail_total_money);
        mDate = view.findViewById(R.id.list_detail_date);

        mTitle.setText(mEvent.getName());
        mTotalMoney.setText(String.valueOf(mEvent.getMoney()));
        mDate.setText(mEvent.getDate());


        RecyclerView recyclerView = view.findViewById(R.id.list_detail_recycler_view);
        mListDetailAdapter = new ListDetailAdapter(mEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mListDetailAdapter);




        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(ListDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
