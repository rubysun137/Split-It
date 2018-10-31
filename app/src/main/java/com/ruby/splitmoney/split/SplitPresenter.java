package com.ruby.splitmoney.split;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.Constants;

public class SplitPresenter implements SplitContract.Presenter {

    private SplitContract.View mView;
    private FirebaseFirestore mDataBase;
    private Context mDialogContext;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private String mFriendEmail;


    public SplitPresenter(SplitContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

    }


    @Override
    public void start() {
        mDataBase = FirebaseFirestore.getInstance();
    }


    @Override
    public void searchForFriend(String email, Context context) {
        mDialogContext = context;
        mFriendEmail = email;
        mDataBase.collection(Constants.USERS).whereEqualTo(Constants.EMAIL, email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        Toast.makeText(mDialogContext, "email 輸入錯誤", Toast.LENGTH_SHORT).show();
                    } else if (!mFriendEmail.equals(mUser.getEmail())) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            String uid = snapshot.getString(Constants.UID);
                            String name = snapshot.getString(Constants.NAME);
                            String image = snapshot.getString(Constants.IMAGE);
                            addFriend(mUser.getUid(), uid, name, image);
                        }
                    } else {
                        Toast.makeText(mDialogContext, "不能加自己為好友啦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    private void addFriend(String myUid, String friendUid, String name, String image) {
        Friend friend = new Friend(mFriendEmail, friendUid, name, 0.0, image);
//        mFirestore.collection("users").document(myUid).update(
//                "friends." + friendUid + ".friend_email", mFriendEmail,
//                "friends." + friendUid + ".friend_uid", friendUid,
//                "friends." + friendUid + ".friend_name", name);
//        mDataBase.collection("users").document(myUid).update("friends", friend);
        Friend myInfo = new Friend(mUser.getEmail(), mUser.getUid(), mUser.getDisplayName(), 0.0, String.valueOf(mUser.getPhotoUrl()));
        mDataBase.collection(Constants.USERS).document(myUid).collection(Constants.FRIENDS).document(friendUid).set(friend);
        mDataBase.collection(Constants.USERS).document(friendUid).collection(Constants.FRIENDS).document(myUid).set(myInfo);
        mView.closeAddFriendDialog(name);
    }
}
