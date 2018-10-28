package com.ruby.splitmoney.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendList {
    private static FriendList mInstance;
    private List<Friend> mFriendList = new ArrayList<>();


    public static FriendList getInstance() {
        if (mInstance == null) {
            synchronized (FriendList.class) {
                if (mInstance == null) {
                    mInstance = new FriendList();
                    mInstance.init();
                }
            }
        }
        return mInstance;
    }

    public List<Friend> getFriendList() {
        return mFriendList;
    }

    public void setFriendList(List<Friend> friendList) {
        mFriendList = new ArrayList<>(friendList);
    }

    private void init() {
        FirebaseFirestore.getInstance().collection(Constants.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constants.FRIENDS).orderBy(Constants.NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Friend> friends = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        friends.add(document.toObject(Friend.class));
                    }
                    mFriendList = new ArrayList<>(friends);
                    Log.d("FriendList", "onComplete: " + friends.size());

                }
            }
        });

    }
}
