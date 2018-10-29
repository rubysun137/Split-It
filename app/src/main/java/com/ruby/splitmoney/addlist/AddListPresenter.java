package com.ruby.splitmoney.addlist;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.GroupList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class AddListPresenter implements AddListContract.Presenter {

    private AddListContract.View mView;
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    private List<Integer> mExtraMoney;
    private List<Integer> mSharedMoney;
    private List<Integer> mFreeMoney;
    private List<Double> mCountExtra;
    private List<Double> mCountShared;
    private List<Double> mCountFree;
    private int mSplitType;
    private int mTotalMoney;
    private int mFeePercentage;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private String mEventId;
    private Friend mPayFriend;
    private double mMoney;
    private List<String> mEventIdList;
    private List<Event> mEventList;
    private List<Double> mMoneyList;
    private List<Friend> mFriendList;
    private double mAddTipMoney;
    private String mWhoPays;
    private Map<String, Double> mPayOweMap;
    private Friend mFriend;
    private List<String> mGroupIdList;
    private List<Group> mGroups;
    private int mGroupPosition;
    private String mGroupId;


    public AddListPresenter(AddListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }


    @Override
    public void start() {
        getCurrentDate();
        getGroups();
        mView.setViewTypeSpinner();
    }

    @Override
    public void addSplitFriendView(int position) {
        mView.showSplitFriendView(position);
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
    public void addSharedMoneyList(int position, int sharedMoney) {
        mSharedMoney.set(position, sharedMoney);
    }

    @Override
    public void addFreeMoneyList(int position, int freeMoney) {
        mFreeMoney.set(position, freeMoney);
    }

    @Override
    public void setListSize(int totalMember) {
        Integer[] members = new Integer[totalMember];

        for (int i = 0; i < totalMember; i++) {
            members[i] = 0;
        }

        mExtraMoney = Arrays.asList(members);
        mSharedMoney = Arrays.asList(members);
        mFreeMoney = Arrays.asList(members);
    }

    @Override
    public boolean isSharedListEmpty() {
        int shareRate = 0;
        for (int i = 0; i < mSharedMoney.size(); i++) {
            shareRate += mSharedMoney.get(i);
        }
        if (shareRate == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int freeTotalMoney() {
        int freeTotal = 0;
        for (int money : mFreeMoney) {
            freeTotal += money;
        }
        return freeTotal;
    }

    @Override
    public void saveSplitResultToFirebase(String eventName, List<Friend> friends, String whoPays, int totalMoney, int tipPercent, String date) {
        mTotalMoney = totalMoney;
        mWhoPays = whoPays;
        //加入自己
        Friend friendMe = new Friend(mUser.getEmail(), mUser.getUid(), "你", 0.0, null);
        mFriendList = new ArrayList<>(friends);
        mFriendList.add(0, friendMe);
        mFeePercentage = tipPercent;
        mAddTipMoney = mTotalMoney * (1 + ((double) mFeePercentage / 100));
        mAddTipMoney = ((double) Math.round(mAddTipMoney * 100) / 100);
        mGroupId = "";
        if (mGroupPosition != 0) {
            mGroupId = mGroups.get(mGroupPosition - 1).getId();
        }
        String payBy;
        if (whoPays.equals("你")) {
            payBy = mUser.getDisplayName();
        } else {
            payBy = whoPays;
        }
        Event event = new Event(eventName, "", mGroupId, mAddTipMoney, date, new Date(System.currentTimeMillis()), false, payBy);
        mFirestore.collection("events").add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                mEventId = task.getResult().getId();
                mFirestore.collection("events").document(mEventId).update("id", mEventId);
                switch (mSplitType) {
                    default:
                    case 0:
                        Log.d("HERE!!!!!", "onComplete: SPLIT TYPE");
                        mMoney = mAddTipMoney / mFriendList.size();
                        mMoney = (double) Math.round(mMoney * 100) / 100;
                        mPayOweMap = new HashMap<>();
                        for (Friend friend : mFriendList) {
                            if (friend.getName().equals(mWhoPays)) {
                                //付款人
                                mPayOweMap.put("pay", mAddTipMoney);
                                mPayOweMap.put("owe", mMoney);
                                mPayFriend = friend;
                                mFirestore.collection("events").document(mEventId).collection("members").document(friend.getUid()).set(mPayOweMap);
                            } else {
                                //非付款人
                                mPayOweMap.put("pay", 0.0);
                                mPayOweMap.put("owe", mMoney);
                                mFirestore.collection("events").document(mEventId).collection("members").document(friend.getUid()).set(mPayOweMap);
                            }
                        }

                        if (mGroupPosition == 0) {
                            for (final Friend f : mFriendList) {
                                //建 event list
                                if (!f.getName().equals(mWhoPays)) {

                                    mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Map<String, Object> listMap = new HashMap<>();
                                            List<FieldValue> list = new ArrayList<>();
                                            list.add(FieldValue.arrayUnion(mEventId));
                                            listMap.put("events", list);
                                            WriteBatch batch = mFirestore.batch();
//
                                            DocumentReference refPay = mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid());
                                            batch.update(refPay, "events", FieldValue.arrayUnion(mEventId));
                                            DocumentReference refOwe = mFirestore.collection("users").document(f.getUid()).collection("friends").document(mPayFriend.getUid());
                                            batch.update(refOwe, "events", FieldValue.arrayUnion(mEventId));

                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    setTotalMoneyToFirebase(f);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        } else {
                            setGroupEvent();
                        }

                        break;
                    case 1:
                        int moneyUnequal = mTotalMoney;
                        mCountExtra = new ArrayList<>();

                        for (int i = 0; i < mFriendList.size(); i++) {
                            moneyUnequal -= mExtraMoney.get(i);
                        }


                        for (int i = 0; i < mFriendList.size(); i++) {
                            double math = (((double) moneyUnequal / mFriendList.size()) + mExtraMoney.get(i)) * (1 + ((double) mFeePercentage / 100));
                            math = ((double) Math.round(math * 100) / 100);
                            mCountExtra.add(i, math);
                        }

                        mPayOweMap = new HashMap<>();
                        for (int i = 0; i < mFriendList.size(); i++) {
                            if (mFriendList.get(i).getName().equals(mWhoPays)) {
                                //付款人
                                mPayOweMap.put("pay", mAddTipMoney);
                                mPayOweMap.put("owe", mCountExtra.get(i));
                                mPayFriend = mFriendList.get(i);
                                mFirestore.collection("events").document(mEventId).collection("members").document(mFriendList.get(i).getUid()).set(mPayOweMap);
                            } else {
                                //非付款人
                                mPayOweMap.put("pay", 0.0);
                                mPayOweMap.put("owe", mCountExtra.get(i));
                                mFirestore.collection("events").document(mEventId).collection("members").document(mFriendList.get(i).getUid()).set(mPayOweMap);
                            }
                        }
                        if (mGroupPosition == 0) {
                            for (final Friend f : mFriendList) {
                                //建 event list
                                if (!f.getName().equals(mWhoPays)) {

                                    mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Map<String, Object> listMap = new HashMap<>();
                                            List<FieldValue> list = new ArrayList<>();
                                            list.add(FieldValue.arrayUnion(mEventId));
                                            listMap.put("events", list);
                                            WriteBatch batch = mFirestore.batch();

                                            DocumentReference refPay = mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid());
                                            batch.update(refPay, "events", FieldValue.arrayUnion(mEventId));
                                            DocumentReference refOwe = mFirestore.collection("users").document(f.getUid()).collection("friends").document(mPayFriend.getUid());
                                            batch.update(refOwe, "events", FieldValue.arrayUnion(mEventId));

                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    setTotalMoneyToFirebase(f);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        } else {
                            setGroupEvent();
                        }


                        break;
                    case 2:
                        mCountShared = new ArrayList<>();
                        int sharedRate = 0;

                        for (int i = 0; i < mFriendList.size(); i++) {
                            sharedRate += mSharedMoney.get(i);
                        }

                        double moneyShared = (double) mTotalMoney / sharedRate;

                        for (int i = 0; i < mFriendList.size(); i++) {
                            if (mSharedMoney.get(i) != 0) {
                                double math = ((moneyShared * mSharedMoney.get(i)) * (1 + ((double) mFeePercentage / 100)));
                                math = ((double) Math.round(math * 100) / 100);
                                mCountShared.add(i, math);
                            } else {
                                mCountShared.add(i, 0.0);
                            }
                        }

                        mPayOweMap = new HashMap<>();
                        for (int i = 0; i < mFriendList.size(); i++) {
                            if (mFriendList.get(i).getName().equals(mWhoPays)) {
                                //付款人
                                mPayOweMap.put("pay", mAddTipMoney);
                                mPayOweMap.put("owe", mCountShared.get(i));
                                mPayFriend = mFriendList.get(i);
                                mFirestore.collection("events").document(mEventId).collection("members").document(mFriendList.get(i).getUid()).set(mPayOweMap);
                            } else {
                                //非付款人
                                mPayOweMap.put("pay", 0.0);
                                mPayOweMap.put("owe", mCountShared.get(i));
                                mFirestore.collection("events").document(mEventId).collection("members").document(mFriendList.get(i).getUid()).set(mPayOweMap);
                            }
                        }

                        if (mGroupPosition == 0) {
                            for (final Friend f : mFriendList) {
                                //建 event list
                                if (!f.getName().equals(mWhoPays)) {

                                    mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Map<String, Object> listMap = new HashMap<>();
                                            List<FieldValue> list = new ArrayList<>();
                                            list.add(FieldValue.arrayUnion(mEventId));
                                            listMap.put("events", list);
                                            WriteBatch batch = mFirestore.batch();

                                            DocumentReference refPay = mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid());
                                            batch.update(refPay, "events", FieldValue.arrayUnion(mEventId));
                                            DocumentReference refOwe = mFirestore.collection("users").document(f.getUid()).collection("friends").document(mPayFriend.getUid());
                                            batch.update(refOwe, "events", FieldValue.arrayUnion(mEventId));

                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    setTotalMoneyToFirebase(f);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        } else {
                            setGroupEvent();
                        }

                        break;
                    case 3:
                        mCountFree = new ArrayList<>();
                        for (int i = 0; i < mFriendList.size(); i++) {
                            if (mFreeMoney.get(i) != 0) {
                                double math = ((double) mSharedMoney.get(i) * (1 + ((double) mFeePercentage / 100)));
                                math = ((double) Math.round(math * 100) / 100);
                                mCountFree.add(i, math);
                            } else {
                                mCountFree.add(i, 0.0);
                            }
                        }


                        mPayOweMap = new HashMap<>();
                        for (int i = 0; i < mFriendList.size(); i++) {
                            if (mFriendList.get(i).getName().equals(mWhoPays)) {
                                //付款人
                                mPayOweMap.put("pay", mAddTipMoney);
                                mPayOweMap.put("owe", mCountFree.get(i));
                                mPayFriend = mFriendList.get(i);
                                mFirestore.collection("events").document(mEventId).collection("members").document(mFriendList.get(i).getUid()).set(mPayOweMap);
                            } else {
                                //非付款人
                                mPayOweMap.put("pay", 0.0);
                                mPayOweMap.put("owe", mCountFree.get(i));
                                mFirestore.collection("events").document(mEventId).collection("members").document(mFriendList.get(i).getUid()).set(mPayOweMap);
                            }
                        }

                        if (mGroupPosition == 0) {

                            for (final Friend f : mFriendList) {
                                //建 event list
                                if (!f.getName().equals(mWhoPays)) {

                                    mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Map<String, Object> listMap = new HashMap<>();
                                            List<FieldValue> list = new ArrayList<>();
                                            list.add(FieldValue.arrayUnion(mEventId));
                                            listMap.put("events", list);
                                            WriteBatch batch = mFirestore.batch();

                                            DocumentReference refPay = mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(f.getUid());
                                            batch.update(refPay, "events", FieldValue.arrayUnion(mEventId));
                                            DocumentReference refOwe = mFirestore.collection("users").document(f.getUid()).collection("friends").document(mPayFriend.getUid());
                                            batch.update(refOwe, "events", FieldValue.arrayUnion(mEventId));

                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    setTotalMoneyToFirebase(f);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        } else {
                            setGroupEvent();
                        }
                        break;
                }
            }
        });
        mView.popBackStack();
    }

    private void setGroupEvent() {
        mFirestore.collection("groups").document(mGroupId).update("events", FieldValue.arrayUnion(mEventId));
    }

    @Override
    public void getGroups() {
        mFirestore.collection("users").document(mUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.contains("groups")) {
                    mGroups = new ArrayList<>();
                    mGroupIdList = new ArrayList<>((List<String>) documentSnapshot.get("groups"));
                    mFirestore.collection("groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                for (String id : mGroupIdList) {
                                    if (snapshot.getId().equals(id)) {
                                        Group group = snapshot.toObject(Group.class);
                                        mGroups.add(group);
                                    }
                                }
                            }
                            mView.setGroupList(mGroups);
                            GroupList.getInstance().setGroupList(mGroups);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void selectGroup(int position) {
        mGroupPosition = position;
    }

    @Override
    public void dateClicked() {
        mView.showDateDialog();
    }

    public void setTotalMoneyToFirebase(Friend friend) {
        mFriend = friend;
        mEventList = new ArrayList<>();
        mEventIdList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        //payer and friends

        mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(mFriend.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                                documentSnapshot.getReference().collection("members").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        boolean payByFriend = false;
                                        double owePayer = 0.0;
                                        double oweFriend = 0.0;

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            double moneyBalance = documentSnapshot.getDouble("owe");
                                            if (documentSnapshot.getId().equals(mFriend.getUid())) {
                                                if (documentSnapshot.getDouble("pay") != 0.0) {
                                                    payByFriend = true;
                                                    oweFriend = moneyBalance;
                                                } else {
                                                    payByFriend = false;
                                                    oweFriend = moneyBalance;
                                                }
                                            } else if (documentSnapshot.getId().equals(mPayFriend.getUid())) {
                                                owePayer = moneyBalance;
                                            }
                                        }
                                        if (payByFriend) {
                                            //朋友付錢
                                            owePayer = 0.0 - owePayer;
                                            mMoneyList.add(owePayer);
                                        } else {
                                            //我付錢
                                            mMoneyList.add(oweFriend);
                                        }
                                        if (mMoneyList.size() == mEventIdList.size()) {
                                            double balanceMoney = 0;
                                            for (Double money : mMoneyList) {
                                                balanceMoney += money;
                                            }
                                            balanceMoney = ((double) Math.round(balanceMoney * 100) / 100);
                                            mFirestore.collection("users").document(mPayFriend.getUid()).collection("friends").document(mFriend.getUid()).update("money", balanceMoney);
                                            mFirestore.collection("users").document(mFriend.getUid()).collection("friends").document(mPayFriend.getUid()).update("money", 0 - balanceMoney);
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
