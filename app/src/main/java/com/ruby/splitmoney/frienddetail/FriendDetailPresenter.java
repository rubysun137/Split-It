package com.ruby.splitmoney.frienddetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class FriendDetailPresenter implements FriendDetailContract.Presenter {

    private FriendDetailContract.View mView;
    private Friend mFriend;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private List<String> mEventIdList;
    private List<Event> mEventList;
    private List<Double> mMoneyList;
    private Map<Event, Double> mMap;


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
        mMap = new HashMap<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("users").document(mUser.getUid()).collection("friends").document(mFriend.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.contains("events")) {
                    mEventIdList = new ArrayList<>((List<String>) documentSnapshot.get("events"));
                    mFirestore.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                                mEventList = new ArrayList<>();
                                mMoneyList = new ArrayList<>();
                                mMap = new HashMap<>();
                                for(String id :mEventIdList){
                                    if(document.getId().equals(id)){
                                        final Event event = document.toObject(Event.class);
                                        document.getReference().collection("members").document(mFriend.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Log.d("CHANGE!!!!", "onSuccess: WHY???????? mEventList.size()"+mEventList.size());
                                                Log.d("CHANGE!!!!", "onSuccess: WHY???????? mMoneyList.size()"+mMoneyList.size());
                                                double moneyBalance = documentSnapshot.getDouble("owe");
                                                if (documentSnapshot.getDouble("pay") == 0.0) {
                                                    //我付錢
                                                    mEventList.add(event);
                                                    mMoneyList.add(moneyBalance);
                                                    mMap.put(event, moneyBalance);
                                                Log.d("CHANGE!!!!", "onSuccess: EVENT????????"+event.getName());

                                                } else {
                                                    //朋友付錢
                                                    mEventList.add(event);
                                                    moneyBalance = 0 - moneyBalance;
                                                    mMoneyList.add(moneyBalance);
                                                    mMap.put(event, moneyBalance);
                                                }
                                                if (mMoneyList.size() == mEventIdList.size()) {
                                                Log.d("CHANGE!!!!", "onSuccess: GO????????");
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
                                                    mMoneyList = new ArrayList<>();
                                                    for (int j = 0; j < mEventList.size(); j++) {
                                                        mMoneyList.add(j, mMap.get(mEventList.get(j)));
                                                    }
                                                    mView.showEvents(mEventList, mMoneyList, mMap);
                                                    int balanceMoney = 0;
                                                    for (Double money : mMoneyList) {
                                                        balanceMoney += money;
                                                    }
                                                    mFirestore.collection("users").document(mUser.getUid()).collection("friends").document(mFriend.getUid()).update("money", balanceMoney);
                                                    mFirestore.collection("users").document(mFriend.getUid()).collection("friends").document(mUser.getUid()).update("money", 0 - balanceMoney);
                                                }
                                            }
                                        });
                                    }

                                }
                            }

                        }
                    });
//                    for (int i = 0; i < mEventIdList.size(); i++) {
//                        mFirestore.collection("events").document(mEventIdList.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                final Event event = documentSnapshot.toObject(Event.class);
//                                documentSnapshot.getReference().collection("members").document(mFriend.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                        double moneyBalance = documentSnapshot.getDouble("owe");
//                                        if (documentSnapshot.getDouble("pay") == 0.0) {
//                                            //我付錢
//                                            mEventList.add(event);
//                                            mMoneyList.add(moneyBalance);
//                                            mMap.put(event, moneyBalance);
//
//                                        } else {
//                                            //朋友付錢
//                                            mEventList.add(event);
//                                            moneyBalance = 0 - moneyBalance;
//                                            mMoneyList.add(moneyBalance);
//                                            mMap.put(event, moneyBalance);
//                                        }
//                                        if (mMoneyList.size() == mEventIdList.size()) {
//                                            Collections.sort(mEventList, new Comparator<Event>() {
//                                                @Override
//                                                public int compare(Event o1, Event o2) {
//                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");
//                                                    int i = 0;
//                                                    try {
//                                                        i = (int) (sdf.parse(o1.getDate()).getTime() - sdf.parse(o2.getDate()).getTime());
//                                                        if (i == 0) {
//                                                            return 0 - (int) (o1.getTime().getTime() - o2.getTime().getTime());
//                                                        } else {
//                                                            return 0 - i;
//                                                        }
//                                                    } catch (ParseException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    return 0 - i;
//                                                }
//                                            });
//                                            mMoneyList = new ArrayList<>();
//                                            for (int j = 0; j < mEventList.size(); j++) {
//                                                mMoneyList.add(j, mMap.get(mEventList.get(j)));
//                                            }
//                                            mView.showEvents(mEventList, mMoneyList, mMap);
//                                            int balanceMoney = 0;
//                                            for (Double money : mMoneyList) {
//                                                balanceMoney += money;
//                                            }
//                                            mFirestore.collection("users").document(mUser.getUid()).collection("friends").document(mFriend.getUid()).update("money", balanceMoney);
//                                            mFirestore.collection("users").document(mFriend.getUid()).collection("friends").document(mUser.getUid()).update("money", 0 - balanceMoney);
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    }
                } else {

                    mView.showEvents(mEventList, mMoneyList, mMap);
                }
            }
        });
    }
}
