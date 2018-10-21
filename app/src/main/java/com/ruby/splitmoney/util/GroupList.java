package com.ruby.splitmoney.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class GroupList {
    private static GroupList mInstance;
    private List<Group> mGroupList = new ArrayList<>();
    private List<String> mGroupIdList;


    public static GroupList getInstance() {
        if (mInstance == null) {
            synchronized (GroupList.class) {
                if (mInstance == null) {
                    mInstance = new GroupList();
                    mInstance.init();
                }
            }
        }
        return mInstance;
    }

    public List<Group> getGroupList() {
        return mGroupList;
    }

    public void setGroupList(List<Group> groupList) {
        mGroupList = new ArrayList<>(groupList);
    }

    public void init() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.contains("groups")) {
                    mGroupList = new ArrayList<>();
                    mGroupIdList = new ArrayList<>((List<String>) documentSnapshot.get("groups"));
                    FirebaseFirestore.getInstance().collection("groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                for (String id : mGroupIdList) {
                                    if (snapshot.getId().equals(id)) {
                                        Group group = snapshot.toObject(Group.class);
                                        mGroupList.add(group);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

    }
}
