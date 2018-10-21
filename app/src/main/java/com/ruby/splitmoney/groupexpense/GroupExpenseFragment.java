package com.ruby.splitmoney.groupexpense;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.GroupDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Group;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class GroupExpenseFragment extends Fragment implements GroupExpenseContract.View, View.OnClickListener {

    private GroupExpenseContract.Presenter mPresenter;
    private Context mContext;
    private GroupDetailAdapter mGroupDetailAdapter;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;
    private TextView mNoEventText;
    private Group mGroup;


    public GroupExpenseFragment() {
        // Required empty public constructor
    }

    public void setGroup(Group group) {
        mGroup = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_expense, container, false);
        mPresenter = new GroupExpensePresenter(this, container.getContext());
        mPresenter.start();
        mContext = container.getContext();
        mFirestore = FirebaseFirestore.getInstance();

        mNoEventText = view.findViewById(R.id.no_event_text);

        RecyclerView recyclerView = view.findViewById(R.id.group_detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mGroupDetailAdapter = new GroupDetailAdapter(mPresenter);
        recyclerView.setAdapter(mGroupDetailAdapter);

        mPresenter.setEventList(mGroup);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupExpenseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEventDetailPage(Event event) {
        ((MainActivity)getActivity()).showListDetailPage(event);
    }

    @Override
    public void showEventList(List<Event> eventList) {
        mEventList = new ArrayList<>(eventList);
        mGroupDetailAdapter.setEvents(mEventList);
        if (mEventList.size() == 0) {
            mNoEventText.setVisibility(View.VISIBLE);
        } else {
            mNoEventText.setVisibility(View.GONE);
        }
    }

    public void setEvent(){
        mFirestore.collection("events").whereEqualTo("group", mGroup.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("GET EVENT NUMBER!!!", "onEvent: " + queryDocumentSnapshots.size());
                mEventList = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    mEventList.add(snapshot.toObject(Event.class));
                }
                Log.d("EVENT NUMBER!!!", "onEvent: " + mEventList.size());
                mGroupDetailAdapter.setEvents(mEventList);
                if (mEventList.size() == 0) {
                    mNoEventText.setVisibility(View.VISIBLE);
                } else {
                    mNoEventText.setVisibility(View.GONE);
                }
            }
        });
    }

}
