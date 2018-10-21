package com.ruby.splitmoney.groupdetail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.GroupDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.FriendList;
import com.ruby.splitmoney.util.GroupList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;


public class GroupDetailFragment extends Fragment implements GroupDetailContract.View, View.OnClickListener {

    private GroupDetailContract.Presenter mPresenter;
    private Context mContext;
    private String mGroupId;
    private Group mGroup;
    private TextView mGroupName;
    private TextView mMemberName;
    private List<Friend> mGroupMembers;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private GroupDetailAdapter mGroupDetailAdapter;
    private List<Event> mEventList;
    private TextView mNoEventText;


    public GroupDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
        mPresenter = new GroupDetailPresenter(this, container.getContext());
        mPresenter.start();
        mContext = container.getContext();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mGroupId = getArguments().getString("id", "");
        for (Group group : GroupList.getInstance().getGroupList()) {
            if (group.getId().equals(mGroupId)) {
                mGroup = group;
            }
        }

        mGroupName = view.findViewById(R.id.group_detail_name);
        mGroupName.setText(mGroup.getName());

        mMemberName = view.findViewById(R.id.member_list_text);

        mNoEventText = view.findViewById(R.id.no_event_text);

        mGroupMembers = new ArrayList<>();
        mEventList = new ArrayList<>();
        Friend memberMe = new Friend(mUser.getEmail(),mUser.getUid(),mUser.getDisplayName(),null,mUser.getPhotoUrl().toString());
        mGroupMembers.add(memberMe);
        for (Friend friend : FriendList.getInstance().getFriendList()) {
            for (String memberId : mGroup.getMembers()) {
                if (friend.getUid().equals(memberId)) {
                    mGroupMembers.add(friend);
                }
            }
        }
        Collections.sort(mGroupMembers, new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (int i = 0; i < mGroupMembers.size(); i++) {
            if(i==0){
                mMemberName.setText(mGroupMembers.get(i).getName());
            }else{
                mMemberName.append(", " + mGroupMembers.get(i).getName());
            }
        }

        RecyclerView recyclerView = view.findViewById(R.id.group_detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mGroupDetailAdapter = new GroupDetailAdapter(mPresenter);
        recyclerView.setAdapter(mGroupDetailAdapter);

        mFirestore.collection("events").whereEqualTo("group",mGroup.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("GET EVENT NUMBER!!!", "onEvent: "+queryDocumentSnapshots.size());
                mEventList = new ArrayList<>();
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    mEventList.add(snapshot.toObject(Event.class));
                }
                Log.d("EVENT NUMBER!!!", "onEvent: "+mEventList.size());
                mGroupDetailAdapter.setEvents(mEventList);
                if(mEventList.size()==0){
                    mNoEventText.setVisibility(View.VISIBLE);
                }else{
                    mNoEventText.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEventDetailPage(Event event) {
        ((MainActivity)getActivity()).showListDetailPage(event);
    }
}
