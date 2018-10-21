package com.ruby.splitmoney.groupexpense;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Group;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class GroupExpensePresenter implements GroupExpenseContract.Presenter {

    private GroupExpenseContract.View mView;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;


    public GroupExpensePresenter(GroupExpenseContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();

    }


    @Override
    public void start() {

    }
    @Override
    public void setEventDetailPage(Event event) {
        mView.showEventDetailPage(event);
    }

    @Override
    public void setEventList(Group group) {
        mFirestore.collection("events").whereEqualTo("group", group.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("GET EVENT NUMBER!!!", "onEvent: " + queryDocumentSnapshots.size());
                mEventList = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    mEventList.add(snapshot.toObject(Event.class));
                }
                Log.d("EVENT NUMBER!!!", "onEvent: " + mEventList.size());
                mView.showEventList(mEventList);
            }
        });
    }

}
