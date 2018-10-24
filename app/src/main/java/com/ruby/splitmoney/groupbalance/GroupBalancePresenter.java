package com.ruby.splitmoney.groupbalance;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Group;

import java.util.ArrayList;
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


    public GroupBalancePresenter(GroupBalanceContract.View view, Context context) {
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

        mFirestore.collection("events").whereEqualTo("group", mGroup.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()==0){
                    mView.showNoEventSign();
                }
                Log.d("QUERY GROUP ", "onSuccess: " + queryDocumentSnapshots.size());
                for (String id : mMemberIdList) {
                    final String memberId = id;
                    mBalanceMoney.put(memberId, 0.0);
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        snapshot.getReference().collection("members").document(memberId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    double money = 0.0;
                                    money = money + documentSnapshot.getDouble("pay")- documentSnapshot.getDouble("owe");
                                    money = (double) Math.round((mBalanceMoney.get(memberId) + money)*100)/100;
                                    mBalanceMoney.put(memberId, money);
                                    Log.d("MAP!!!!!", "onSuccess: "+ mBalanceMoney.get(memberId));
                                    Log.d("MAP!!!!!", "onSuccess: "+ memberId);
                                    mView.updateBalance(mBalanceMoney);
//                                    mBalanceMoney.put(memberId, mBalanceMoney.get(memberId) + documentSnapshot.getDouble("pay"));
//                                    mBalanceMoney.put(memberId, mBalanceMoney.get(memberId) - documentSnapshot.getDouble("owe"));
                                }
                            }
                        });
                    }
                }
                Log.d("MONEY", "onSuccess: " + mBalanceMoney.get(mMemberIdList.get(0)));
            }
        });
    }
}
