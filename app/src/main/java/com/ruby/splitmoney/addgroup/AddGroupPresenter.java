package com.ruby.splitmoney.addgroup;

import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.Constants;

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
        mView.showGroupMember();
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
        mFirestore.collection(Constants.GROUPS).add(group).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String groupId = documentReference.getId();
                mFirestore.collection(Constants.GROUPS).document(groupId).update(Constants.ID, groupId);
                for (String uid : mFriendUid) {
                    mFirestore.collection(Constants.USERS).document(uid).update(Constants.GROUPS, FieldValue.arrayUnion(groupId));
                }
            }
        });

        addGroupFriend(friends);
    }

    @Override
    public void clickAddFriendToGroup() {
        mView.showAddFriendDialog();
    }

    @Override
    public void clickSaveButton(EditText groupName, List<Friend> addedFriend) {
        if (!groupName.getText().toString().equals("") && addedFriend.size() != 0) {
            saveGroupData(groupName.getText().toString(), addedFriend);

            mView.popBackStack();
        } else if (groupName.getText().toString().equals("")) {
            mView.showNoNameMessage();
        } else {
            mView.showNoFriendMessage();
        }
    }

    private void addGroupFriend(List<Friend> friends) {
        for (Friend friend : friends) {
            friend.setMoney(0.0);
        }
        for (int i = 0; i < friends.size(); i++) {
            for (int j = i + 1; j < friends.size(); j++) {
                final Friend friendA = friends.get(i);
                final Friend friendB = friends.get(j);
                mFirestore.collection(Constants.USERS).document(friends.get(i).getUid()).collection(Constants.FRIENDS).document(friends.get(j).getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            addFriend(friendA, friendB);
                        }
                    }
                });
            }
        }
    }

    private void addFriend(Friend friendA, Friend friendB) {
        mFirestore.collection(Constants.USERS).document(friendA.getUid()).collection(Constants.FRIENDS).document(friendB.getUid()).set(friendB);
        mFirestore.collection(Constants.USERS).document(friendB.getUid()).collection(Constants.FRIENDS).document(friendA.getUid()).set(friendA);
    }
}
