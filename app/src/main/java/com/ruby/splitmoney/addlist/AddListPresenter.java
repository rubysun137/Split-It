package com.ruby.splitmoney.addlist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class AddListPresenter implements AddListContract.Presenter {

    private AddListContract.View mView;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;
    private List<Integer> mExtraMoney;
    private List<Integer> mSharedMoney;
    private List<Double> mCountExtra;
    private List<Double> mCountShared;
    private int mSplitType;
    private int mTotalMoney;
    private int mTotalMember;
    private int mFeePercentage;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private String mEventId;
    private Friend mPayFriend;
    private double mMoney;
    private List<String> mEventIdList;
    private List<Event> mEventList;
    private List<Double> mMoneyList;


    public AddListPresenter(AddListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public void start() {

    }

    @Override
    public void addSplitFriendView(int position) {
        mView.showSplitFriendView(position);
    }

    @Override
    public void setPayerSpinner() {

    }

    @Override
    public void getCurrentDate() {
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        String format = String.valueOf(mYear) + "/" + String.valueOf(mMonth + 1) + "/" + String.valueOf(mDay);

        mView.showCurrentDate(format);
    }

    @Override
    public void selectSplitType(int spitType) {
        mSplitType = spitType;
    }

    @Override
    public void addExtraMoneyList(int position, int extraMoney) {
        mExtraMoney.set(position, extraMoney);
    }

    @Override
    public void setListSize(int totalMember) {
        Integer[] members = new Integer[totalMember];

        for (int i = 0; i < totalMember; i++) {
            members[i] = 0;
        }

        mExtraMoney = Arrays.asList(members);
        mSharedMoney = Arrays.asList(members);
    }

    @Override
    public void saveSplitResultToFirebase(String eventName, List<Friend> friends, final String whoPays, final int totalMoney, final int tipPercent, String date) {
        Friend friendMe = new Friend(mUser.getEmail(), mUser.getUid(), "你", 0.0);
        final List<Friend> friendList = new ArrayList<>(friends);
        friendList.add(friendMe);
        final double addTipMoney = totalMoney * (1 + ((double) tipPercent / 100));
        Event event = new Event(eventName, "", "", addTipMoney, date,new Date(System.currentTimeMillis()));
        mFirestore.collection("events").add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                mEventId = task.getResult().getId();
                mFirestore.collection("events").document(mEventId).update("id", mEventId);
                switch (mSplitType) {
                    default:
                    case 0:
                        Log.d("HERE!!!!!", "onComplete: SPLIT TYPE");
                        mMoney = addTipMoney / friendList.size();
                        mMoney = (double) Math.round(mMoney * 100) / 100;
                        Map<String, Double> map = new HashMap<>();
                        for (final Friend friend : friendList) {
                            if (friend.getName().equals(whoPays)) {
                                //付款人
                                map.put("pay", addTipMoney);
                                map.put("owe", mMoney);
                                mPayFriend = friend;
                                mFirestore.collection("events").document(mEventId).collection("members").document(friend.getUid()).set(map);
                            } else {
                                //非付款人
                                map.put("pay", 0.0);
                                map.put("owe", mMoney);
                                mFirestore.collection("events").document(mEventId).collection("members").document(friend.getUid()).set(map);
                            }
                        }

                        for (final Friend f : friendList) {
                            //建 event list
                            if (!f.getName().equals(whoPays)) {

                                mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        Map<String, Object> listMap = new HashMap<>();
                                        List<FieldValue> list = new ArrayList<>();
                                        list.add(FieldValue.arrayUnion(mEventId));
                                        listMap.put("events", list);
                                        if (task.getResult().contains("events")) {
                                            Map<String, Object> friendMap = new HashMap<>();
                                            WriteBatch batch = mFirestore.batch();
//                                            friendMap.put(mPayFriend.getUid() + "/friends/" + f.getUid() + "/event", list);
//                                            friendMap.put(f.getUid() + "/friends/" + mPayFriend.getUid() + "/event", list);
//
                                            mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).update("events", FieldValue.arrayUnion(mEventId));
                                            DocumentReference refPay = mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid());
                                            batch.update(refPay,"events",FieldValue.arrayUnion(mEventId));
                                            DocumentReference refOwe = mFirestore.collection("users").document(f.getUid()).collection("friends").document(mPayFriend.getUid());
                                            batch.update(refOwe,"events",FieldValue.arrayUnion(mEventId));

                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    setTotalMoneyToFirebase(f);
                                                }
                                            });
                                        } else {
                                            mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).update("events", FieldValue.arrayUnion(mEventId), "money", mMoney);
                                            mFirestore.collection("users").document(f.getUid()).collection("friends").document(mPayFriend.getUid()).update("events", FieldValue.arrayUnion(mEventId), "money", (0 - mMoney));
                                        }
                                    }
                                });
                            }
                        }

//                double money = mTotalMoney * (1 + ((double) mFeePercentage / 100)) / mTotalMember;
//                money = (double) Math.round(money * 100) / 100;
                        break;
                    case 1:
//                int moneyUnequal = mTotalMoney;
//                mCountExtra = new ArrayList<>();
//
//                for (int i = 0; i < mTotalMember; i++) {
//                    moneyUnequal -= mExtraMoney.get(i);
//                }
//
//
//                for (int i = 0; i < mTotalMember; i++) {
//                    double math = (((double) moneyUnequal / mTotalMember) + mExtraMoney.get(i)) * (1 + ((double) mFeePercentage / 100));
//                    math = ((double) Math.round(math * 100) / 100);
//                    mCountExtra.add(i, math);
//                }
//

                        break;
                    case 2:
//                mCountShared = new ArrayList<>();
//                int sharedRate = 0;
//
//                for (int i = 0; i < mTotalMember; i++) {
//                    sharedRate += mSharedMoney.get(i);
//                }
//
//                double moneyShared = (double) mTotalMoney / sharedRate;
//
//                for (int i = 0; i < mTotalMember; i++) {
//                    if (mSharedMoney.get(i) != 0) {
//                        double math = ((moneyShared * mSharedMoney.get(i)) * (1 + ((double) mFeePercentage / 100)));
//                        math = ((double) Math.round(math * 100) / 100);
//                        mCountShared.add(i, math);
//                    } else {
//                        mCountShared.add(i, 0.0);
//                    }
//                }

                        break;
                }
            }
        });

    }

    public void setTotalMoneyToFirebase(final Friend friend) {
        mEventList = new ArrayList<>();
        mEventIdList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        //payer and friends

        mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(friend.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.contains("events")) {
                    mEventIdList = new ArrayList<>((List<String>) documentSnapshot.get("events"));
                    for (int i = 0; i < mEventIdList.size(); i++) {
                        mFirestore.collection("events").document(mEventIdList.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Event event = documentSnapshot.toObject(Event.class);
                                mEventList.add(event);
                                    documentSnapshot.getReference().collection("members").document(friend.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                            if (mMoneyList.size() == mEventIdList.size()) {
                                                int balanceMoney = 0;
                                                for (Double money : mMoneyList) {
                                                    balanceMoney += money;
                                                }
                                                mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(friend.getUid()).update("money", balanceMoney);
                                                mFirestore.collection("users").document(friend.getUid()).collection("friends").document(mPayFriend.getUid()).update("money", 0 - balanceMoney);
                                            }
                                        }
                                    });
                                }

                        });
                    }
                }
            }
        });


    }


}
