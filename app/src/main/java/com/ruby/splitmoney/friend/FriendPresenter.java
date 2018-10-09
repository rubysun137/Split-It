package com.ruby.splitmoney.friend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.App;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class FriendPresenter implements FriendContract.Presenter {

    private FriendContract.View mView;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private List<Friend> mFriends;
    private Context mContext;
    private Dialog mDialog;


    public FriendPresenter(FriendContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mContext = context;
    }


    @Override
    public void start() {
        setFriendList();
    }

    private void setFriendList() {
        mFirestore.collection("users").document(mUser.getUid()).collection("friends").orderBy("name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mFriends = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    mFriends.add(snapshot.toObject(Friend.class));
                }
                FriendList.getInstance().setFriendList(mFriends);
                mView.showFriendList();
            }
        });
    }

    @Override
    public void transToFriendDetailPage(String friendName) {
        mView.setFriendDetailPage(friendName);
    }

    @Override
    public void deleteFriendDialog(final String friendUid) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_friend,null,false);
        mDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .show();
        mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        view.findViewById(R.id.dialog_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore.collection("users").document(mUser.getUid()).collection("friends").document(friendUid).delete();
                mFirestore.collection("users").document(friendUid).collection("friends").document(mUser.getUid()).delete();
                mDialog.dismiss();
            }
        });

        view.findViewById(R.id.dialog_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


    }
}
