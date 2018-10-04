package com.ruby.splitmoney.friend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class FriendPresenter implements FriendContract.Presenter {

    private FriendContract.View mView;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private List<Friend> mFriends;


    public FriendPresenter(FriendContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public void start() {
        setFriendList();
    }

    private void setFriendList() {
        mFirestore.collection("users").document(mUser.getUid()).collection("friends").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mFriends = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    mFriends.add(snapshot.toObject(Friend.class));
                }
                mView.showFriendList(mFriends);
            }
        });
    }

    @Override
    public void transToFriendDetailPage(String friendName) {
        mView.setFriendDetailPage(friendName);
    }
}
