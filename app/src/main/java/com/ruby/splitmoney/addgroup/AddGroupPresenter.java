package com.ruby.splitmoney.addgroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;

import java.util.ArrayList;
import java.util.List;

public class AddGroupPresenter implements AddGroupContract.Presenter {

    private AddGroupContract.View mView;
    private FirebaseFirestore mFirestore;
    private List<String> mFriendUid;


    public AddGroupPresenter(AddGroupContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    public void start() {

    }

    @Override
    public void addToAddedFriendList(Friend friend, int position) {
        mView.changeToAddedFriendList(friend, position);
    }

    @Override
    public void addToNotAddFriendList(Friend friend, int position) {
        mView.changeNotAddFriendList(friend, position);
    }

    @Override
    public void saveGroupData(String groupName, List<Friend> friends) {
        mFriendUid = new ArrayList<>();
        //加入自己 & 朋友
        mFriendUid.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        for (Friend friend : friends) {
            mFriendUid.add(friend.getUid());
        }
        Group group = new Group(groupName, "", mFriendUid, null);
        mFirestore.collection("groups").add(group).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String groupId = documentReference.getId();
                mFirestore.collection("groups").document(groupId).update("id", groupId);
                for (String uid : mFriendUid) {
                    mFirestore.collection("users").document(uid).update("groups",FieldValue.arrayUnion(groupId));
                }
            }
        });
    }
}
