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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private Double mBalanceMoney;
    private Double mSettleMoney;


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
        mFirestore.collection(Constants.USERS).document(mUser.getUid()).collection(Constants.FRIENDS).document(mFriend.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.contains(Constants.EVENTS)) {
                    mEventIdList = new ArrayList<>((List<String>) documentSnapshot.get(Constants.EVENTS));
                    mFirestore.collection(Constants.EVENTS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                mEventList = new ArrayList<>();
                                mMoneyList = new ArrayList<>();
                                mMap = new HashMap<>();
                                for (String id : mEventIdList) {
                                    if (document.getId().equals(id)) {
                                        final Event event = document.toObject(Event.class);
                                        document.getReference().collection(Constants.MEMBERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                boolean payByFriend = false;
                                                double oweMe = 0.0;
                                                double oweFriend = 0.0;

                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    Log.d("CHANGE!!!!", "onSuccess: WHY???????? mEventList.size()" + mEventList.size());
                                                    Log.d("CHANGE!!!!", "onSuccess: WHY???????? mMoneyList.size()" + mMoneyList.size());
                                                    Log.d("CHANGE!!!!", "onSuccess: WHY???????? documentSnapshot" + documentSnapshot.getId());
                                                    documentSnapshot.contains(Constants.MEMBERS);
                                                    double moneyBalance = documentSnapshot.getDouble(Constants.OWE);
                                                    if (documentSnapshot.getId().equals(mFriend.getUid())) {
                                                        if (documentSnapshot.getDouble(Constants.PAY) != 0.0) {
                                                            payByFriend = true;
                                                            oweFriend = moneyBalance;
                                                        } else {
                                                            payByFriend = false;
                                                            oweFriend = moneyBalance;
                                                        }
                                                    } else if (documentSnapshot.getId().equals(mUser.getUid())) {
                                                        oweMe = moneyBalance;
                                                    }
                                                }
                                                if (payByFriend) {
                                                    //朋友付錢
                                                    mEventList.add(event);
                                                    oweMe = 0 - oweMe;
                                                    mMoneyList.add(oweMe);
                                                    mMap.put(event, oweMe);
                                                } else {
                                                    //我付錢
                                                    mEventList.add(event);
                                                    mMoneyList.add(oweFriend);
                                                    mMap.put(event, oweFriend);
                                                    Log.d("CHANGE!!!!", "onSuccess: EVENT????????" + event.getName());


                                                }
                                                if (mMoneyList.size() == mEventIdList.size()) {
                                                    Log.d("CHANGE!!!!", "onSuccess: GO????????");
                                                    Collections.sort(mEventList, new Comparator<Event>() {
                                                        @Override
                                                        public int compare(Event o1, Event o2) {
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");
                                                            long i;
                                                            int index = 0;
                                                            try {
                                                                Log.d("WHAT HAPPEND?", "compare: " + o1.getName() + sdf.parse(o1.getDate()).getTime());
                                                                Log.d("WHAT HAPPEND?", "compare: " + o1.getName() + o1.getTime().getTime());
                                                                Log.d("WHAT HAPPEND?", "compare: " + o2.getName() + sdf.parse(o2.getDate()).getTime());
                                                                Log.d("WHAT HAPPEND?", "compare: " + o2.getName() + o2.getTime().getTime());
                                                                Log.d("WHAT HAPPEND?", "compare: " + (sdf.parse(o1.getDate()).getTime() - sdf.parse(o2.getDate()).getTime()));

                                                                i = (sdf.parse(o1.getDate()).getTime() - sdf.parse(o2.getDate()).getTime());
                                                                if (i > 0) {
                                                                    index = 1;
                                                                } else if (i < 0) {
                                                                    index = -1;
                                                                }
                                                                Log.d("WHAT HAPPEND?", "compare: " + " i = " + i);
                                                                if (i == 0) {
                                                                    return 0 - (int) (o1.getTime().getTime() - o2.getTime().getTime());
                                                                } else {
                                                                    return 0 - index;
                                                                }
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            return 0 - index;
                                                        }
                                                    });
                                                    mMoneyList = new ArrayList<>();
                                                    for (int j = 0; j < mEventList.size(); j++) {
                                                        mMoneyList.add(j, mMap.get(mEventList.get(j)));
                                                    }
                                                    mView.showEvents(mEventList, mMoneyList, mMap);
                                                    double balanceMoney = 0;
                                                    for (Double money : mMoneyList) {
                                                        balanceMoney += money;
                                                    }
                                                    balanceMoney = ((double) Math.round(balanceMoney * 100) / 100);
                                                    mFirestore.collection(Constants.USERS).document(mUser.getUid()).collection(Constants.FRIENDS).document(mFriend.getUid()).update(Constants.MONEY, balanceMoney);
                                                    mFirestore.collection(Constants.USERS).document(mFriend.getUid()).collection(Constants.FRIENDS).document(mUser.getUid()).update(Constants.MONEY, 0 - balanceMoney);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                } else {

                    mView.showEvents(mEventList, mMoneyList, mMap);
                }
            }
        });
    }

    @Override
    public void setSettleUpToFirebase(Double settleMoney, Double balanceMoney) {
        mBalanceMoney = balanceMoney;
        mSettleMoney = settleMoney;
        Calendar calendar = Calendar.getInstance();
        String format = String.valueOf(calendar.get(Calendar.YEAR)) + "/" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String whoPay;
        if (mBalanceMoney > 0) {
            whoPay = mFriend.getName();
        } else {
            whoPay = mUser.getDisplayName();
        }
        Event event = new Event("結清帳務", "", "", mSettleMoney, format, new Date(System.currentTimeMillis()), true, whoPay);
        mFirestore.collection(Constants.EVENTS).add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                WriteBatch batch = mFirestore.batch();
                String eventId = documentReference.getId();
                DocumentReference refEventId = mFirestore.collection(Constants.EVENTS).document(eventId);
                batch.update(refEventId, Constants.ID, eventId);
                DocumentReference refUserMoney = mFirestore.collection(Constants.USERS).document(mUser.getUid()).collection(Constants.FRIENDS).document(mFriend.getUid());
                DocumentReference refFriendMoney = mFirestore.collection(Constants.USERS).document(mFriend.getUid()).collection(Constants.FRIENDS).document(mUser.getUid());
                DocumentReference refEventUser = mFirestore.collection(Constants.EVENTS).document(eventId).collection(Constants.MEMBERS).document(mUser.getUid());
                DocumentReference refEventFriend = mFirestore.collection(Constants.EVENTS).document(eventId).collection(Constants.MEMBERS).document(mFriend.getUid());
                DocumentReference refUserEvent = mFirestore.collection(Constants.USERS).document(mUser.getUid()).collection(Constants.FRIENDS).document(mFriend.getUid());
                DocumentReference refFriendEvent = mFirestore.collection(Constants.USERS).document(mFriend.getUid()).collection(Constants.FRIENDS).document(mUser.getUid());

                if (mBalanceMoney > 0) {
                    //他欠我錢  還錢
                    //update user money
                    batch.update(refUserMoney, Constants.MONEY, (double) Math.round((mBalanceMoney - mSettleMoney) * 100) / 100);
                    batch.update(refFriendMoney, Constants.MONEY, (double) Math.round((mSettleMoney - mBalanceMoney) * 100) / 100);
                    //set event
                    Map<String, Double> map = new HashMap<>();
                    map.put(Constants.OWE, mSettleMoney);
                    map.put(Constants.PAY, 0.0);
                    batch.set(refEventUser, map);
                    map.put(Constants.OWE, 0.0);
                    map.put(Constants.PAY, mSettleMoney);
                    batch.set(refEventFriend, map);
                } else if (mBalanceMoney < 0) {
                    //我欠他錢 還錢
                    //update user money
                    batch.update(refUserMoney, Constants.MONEY, (double) Math.round((mBalanceMoney + mSettleMoney) * 100) / 100);
                    batch.update(refUserMoney, Constants.MONEY, (double) Math.round((0 - (mBalanceMoney + mSettleMoney)) * 100) / 100);
                    //set event
                    Map<String, Double> map = new HashMap<>();
                    map.put(Constants.OWE, 0.0);
                    map.put(Constants.PAY, mSettleMoney);
                    batch.set(refEventUser, map);
                    map.put(Constants.OWE, mSettleMoney);
                    map.put(Constants.PAY, 0.0);
                    batch.set(refEventFriend, map);
                }
                //update user event
                batch.update(refUserEvent, Constants.EVENTS, FieldValue.arrayUnion(eventId));
                batch.update(refFriendEvent, Constants.EVENTS, FieldValue.arrayUnion(eventId));
                batch.commit();
            }
        });
    }

    @Override
    public void transToListDetailPage(Event event) {
        mView.setListDetailPage(event);
    }
}
