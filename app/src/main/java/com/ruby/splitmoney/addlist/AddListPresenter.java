package com.ruby.splitmoney.addlist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Friend friendMe = new Friend(mUser.getEmail(), mUser.getUid(), "你",0.0);
        final List<Friend> friendList = new ArrayList<>(friends);
        friendList.add(friendMe);
        final double addTipMoney = totalMoney * (1 + ((double) tipPercent / 100));
        Event event = new Event(eventName, "", "", addTipMoney, date);
        mFirestore.collection("events").add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                mEventId = task.getResult().getId();
                mFirestore.collection("events").document(mEventId).update("id", mEventId);
                switch (mSplitType) {
                    default:
                    case 0:
                        Log.d("HERE!!!!!", "onComplete: SPLIT TYPE");
                        double money = addTipMoney / friendList.size();
                        money = (double) Math.round(money * 100) / 100;
                        Map<String, Double> map = new HashMap<>();
                        for (final Friend friend : friendList) {
                            if (friend.getName().equals(whoPays)) {
                                map.put("pay", addTipMoney);
                                map.put("owe", money);
                                mFirestore.collection("events").document(mEventId).collection("members").document(friend.getUid()).set(map);
                                for (final Friend f : friendList) {
                                    if (!f.getName().equals(whoPays)) {
                                        final Map<String, Object> listMap = new HashMap<>();
                                        List<String> list = new ArrayList<>();
                                        list.add(mEventId);
                                        listMap.put("events", list);

                                        mFirestore.collection("users").document(friend.getUid()).collection("friends").document(f.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.getResult().contains("event")) {
                                                    mFirestore.collection("users").document(friend.getUid()).collection("friends").document(f.getUid()).update(listMap);
                                                    mFirestore.collection("users").document(f.getUid()).collection("friends").document(friend.getUid()).update(listMap);

                                                } else {
                                                    mFirestore.collection("users").document(friend.getUid()).collection("friends").document(f.getUid()).update("events", FieldValue.arrayUnion(mEventId));
                                                    mFirestore.collection("users").document(f.getUid()).collection("friends").document(friend.getUid()).update("events", FieldValue.arrayUnion(mEventId));

                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                map.put("pay", 0.0);
                                map.put("owe", money);
                                mFirestore.collection("events").document(mEventId).collection("members").document(friend.getUid()).set(map);
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

}
