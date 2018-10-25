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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                Collections.sort(mEventList, new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");
                        int i = 0;
                        try {
                            i = (int) (sdf.parse(o1.getDate()).getTime() - sdf.parse(o2.getDate()).getTime());
                            if (i == 0) {
                                return 0 - (int) (o1.getTime().getTime() - o2.getTime().getTime());
                            } else {
                                return 0 - i;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0 - i;
                    }
                });
                mView.showEventList(mEventList);
            }
        });
    }

}
