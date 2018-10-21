package com.ruby.splitmoney.groupdetail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.GroupList;


public class GroupDetailFragment extends Fragment implements GroupDetailContract.View, View.OnClickListener {

    private GroupDetailContract.Presenter mPresenter;
    private Context mContext;
    private String mGroupId;
    private Group mGroup;



    public GroupDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
        mPresenter = new GroupDetailPresenter(this, container.getContext());
        mPresenter.start();
        mContext = container.getContext();
        mGroupId = getArguments().getString("id","");
        for(Group group : GroupList.getInstance().getGroupList()){
            if(group.getId().equals(mGroupId)){
                mGroup = group;
            }
        }


        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
