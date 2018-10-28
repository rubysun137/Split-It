package com.ruby.splitmoney.group;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.Constants;
import com.ruby.splitmoney.util.GroupList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class GroupPresenter implements GroupContract.Presenter {

    private GroupContract.View mView;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private Context mContext;
    private List<String> mGroupIdList;
    private List<Group> mGroups;


    public GroupPresenter(GroupContract.View view, Context context) {
        mView = view;
        mView.setPresenter(this);
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mContext = context;

    }


    @Override
    public void start() {
        setGroupList();
    }

    private void setGroupList() {
        mFirestore.collection(Constants.USERS).document(mUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.contains(Constants.GROUPS)) {
                    mGroups = new ArrayList<>();
                    mGroupIdList = new ArrayList<>((List<String>) documentSnapshot.get(Constants.GROUPS));
                    mFirestore.collection(Constants.GROUPS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
    public void transToGroupDetailPage(String groupId) {
        mView.setGroupDetailPage(groupId);
    }
}
