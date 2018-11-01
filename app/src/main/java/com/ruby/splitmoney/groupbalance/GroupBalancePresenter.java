package com.ruby.splitmoney.groupbalance;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.ruby.splitmoney.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class GroupBalancePresenter implements GroupBalanceContract.Presenter {


    private GroupBalanceContract.View mView;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;
    private Group mGroup;
    List<String> mEventIdList;
    List<String> mMemberIdList;
    Map<String, Double> mBalanceMoney;
    private Context mContext;


    public GroupBalancePresenter(GroupBalanceContract.View view, Context context) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();

    }


    @Override
    public void start() {

    }

    @Override
    public void calculateMoney(Group group) {
        mGroup = group;
        mMemberIdList = new ArrayList<>(mGroup.getMembers());
        mBalanceMoney = new HashMap<>();

        mFirestore.collection(Constants.EVENTS).whereEqualTo(Constants.GROUP, mGroup.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.size() == 0) {
                    mView.showNoEventSign();
                }
                Log.d("QUERY GROUP_PAGE ", "onSuccess: " + queryDocumentSnapshots.size());
                for (String id : mMemberIdList) {
                    final String memberId = id;
                    mBalanceMoney.put(memberId, 0.0);
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        snapshot.getReference().collection(Constants.MEMBERS).document(memberId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    double money = 0.0;
                                    money = money + documentSnapshot.getDouble(Constants.PAY) - documentSnapshot.getDouble(Constants.OWE);
                                    money = (double) Math.round((mBalanceMoney.get(memberId) + money) * 100) / 100;
                                    mBalanceMoney.put(memberId, money);
//                                    Log.d("MAP!!!!!", "onSuccess: " + mBalanceMoney.get(memberId));
//                                    Log.d("MAP!!!!!", "onSuccess: " + memberId);
                                    mView.updateBalance(mBalanceMoney);
//                                    mBalanceMoney.put(memberId, mBalanceMoney.get(memberId) + documentSnapshot.getDouble("pay"));
//                                    mBalanceMoney.put(memberId, mBalanceMoney.get(memberId) - documentSnapshot.getDouble("owe"));
                                }
                            }
                        });
                    }
                }
//                Log.d("MONEY", "onSuccess: " + mBalanceMoney.get(mMemberIdList.get(0)));
            }
        });
    }

    @Override
    public void settleUp(int position, List<Friend> friendList, List<Double> moneyList) {
        mView.showSettleUpDialog(position, friendList, moneyList);
    }

    @Override
    public void setSettleUpToFirebase(final Double settleMoney, final Friend payMoneyFriend, final Friend getMoneyFriend) {
        Calendar calendar = Calendar.getInstance();
        String format = String.valueOf(calendar.get(Calendar.YEAR)) + "/" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        Event event = new Event("結清帳務", "", mGroup.getId(), settleMoney, format, new Date(System.currentTimeMillis()), true, payMoneyFriend.getName());
        mFirestore.collection(Constants.EVENTS).add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                WriteBatch batch = mFirestore.batch();
                String eventId = documentReference.getId();
                DocumentReference refEventId = mFirestore.collection(Constants.EVENTS).document(eventId);
                batch.update(refEventId, Constants.ID, eventId);

                //set event
                Map<String, Double> map = new HashMap<>();
                map.put(Constants.OWE, 0.0);
                map.put(Constants.PAY, settleMoney);
                DocumentReference refEventPay = mFirestore.collection(Constants.EVENTS).document(eventId).collection(Constants.MEMBERS).document(payMoneyFriend.getUid());
                batch.set(refEventPay, map);

                map.put(Constants.OWE, settleMoney);
                map.put(Constants.PAY, 0.0);
                DocumentReference refEventOwe = mFirestore.collection(Constants.EVENTS).document(eventId).collection(Constants.MEMBERS).document(getMoneyFriend.getUid());
                batch.set(refEventOwe, map);

                //update user event
                DocumentReference refUserEvent = mFirestore.collection(Constants.GROUPS).document(mGroup.getId());
                batch.update(refUserEvent, Constants.EVENTS, FieldValue.arrayUnion(eventId));
                batch.commit();
            }
        });
    }


}
