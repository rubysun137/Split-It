package com.ruby.splitmoney.frienddetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class FriendDetailPresenter implements FriendDetailContract.Presenter {

    private FriendDetailContract.View mView;
    private Friend mFriend;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private List<String> mEventIdList;
    private List<Event> mEventList;
    private List<Double> mMoneyList;


    public FriendDetailPresenter(FriendDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

    @Override
    public void loadEvents(Friend friend) {
        mFriend = friend;
        mEventList = new ArrayList<>();
        mEventIdList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("users").document(mUser.getUid()).collection("friends").document(mFriend.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.contains("events")) {
                    mEventIdList = new ArrayList<>((List<String>) documentSnapshot.get("events"));
                    for (String eventId : mEventIdList) {
                        mFirestore.collection("events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Event event = documentSnapshot.toObject(Event.class);
                                mEventList.add(event);
                                documentSnapshot.getReference().collection("members").document(mFriend.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        double moneyBalance = documentSnapshot.getDouble("owe");
                                        if (documentSnapshot.getDouble("pay") == 0.0) {
                                            //我付錢
                                            mMoneyList.add(moneyBalance);
                                        } else {
                                            //朋友付錢
                                            moneyBalance = 0 - moneyBalance;
                                            mMoneyList.add(moneyBalance);
                                        }
                                        mView.showEvents(mEventList,mMoneyList);
                                        Log.d("EVENT NUMBER", "onComplete: " + mEventList.size());
                                    }
                                });
                            }
                        });
                    }
                    Log.d("EVENT  OUT  NUMBER", "onComplete: " + mEventList.size());
                }
            }
        });
    }
}
