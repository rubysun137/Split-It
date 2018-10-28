package com.ruby.splitmoney.addgroup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.AddGroupAdapter;
import com.ruby.splitmoney.adapters.AddGroupSelectAdapter;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AddGroupFragment extends Fragment implements AddGroupContract.View, View.OnClickListener {

    private AddGroupContract.Presenter mPresenter;
    private AddGroupAdapter mAddGroupAdapter;
    private AddGroupSelectAdapter mAddGroupSelectAdapter;
    private List<Friend> mNotAddFriends;
    private List<Friend> mAddedFriends;
    private Dialog mAddMemberDialog;
    private EditText mGroupName;
    private Button mSaveButton;
    private LinearLayout mAddMemberToList;
    private Context mContext;
    private BottomSheetDialog mBottomSheetDialog;
    private FirebaseFirestore mFirestore;


    public AddGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_group, container, false);
        mPresenter = new AddGroupPresenter(this);
        mPresenter.start();
        mFirestore = FirebaseFirestore.getInstance();
        mContext = container.getContext();

        mAddedFriends = new ArrayList<>();
        mNotAddFriends = new ArrayList<>(FriendList.getInstance().getFriendList());

        RecyclerView recyclerView = view.findViewById(R.id.add_group_recycler_view);
        mAddGroupAdapter = new AddGroupAdapter(mPresenter);
        mAddGroupAdapter.setFriendList(mAddedFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAddGroupAdapter);

        mGroupName = view.findViewById(R.id.add_group_name);
        mSaveButton = view.findViewById(R.id.add_group_save_button);
        mSaveButton.setOnClickListener(this);
        mAddMemberToList = view.findViewById(R.id.add_friend_to_group);
        mAddMemberToList.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend_to_group:
                mBottomSheetDialog = new BottomSheetDialog(mContext);
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_group_member, null);

                RecyclerView recyclerView = view.findViewById(R.id.dialog_add_group_member_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mAddGroupSelectAdapter = new AddGroupSelectAdapter(mPresenter);
                mAddGroupSelectAdapter.setFriendList(mNotAddFriends);
                recyclerView.setAdapter(mAddGroupSelectAdapter);

                mBottomSheetDialog.setContentView(view);
                mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                mBottomSheetDialog.show();
                break;
            case R.id.add_group_save_button:
                if (!mGroupName.getText().toString().equals("") && mAddedFriends.size() != 0) {
                    mPresenter.saveGroupData(mGroupName.getText().toString(), mAddedFriends);
                    getFragmentManager().popBackStack();
                } else if (mGroupName.getText().toString().equals("")) {
                    Toast.makeText(mContext, R.string.enter_group_name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.need_a_friend_to_create_group, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(AddGroupContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void changeToAddedFriendList(Friend friend, int position) {
        mBottomSheetDialog.dismiss();
        mAddedFriends.add(friend);
//        Collections.sort(mAddedFriends, new Comparator<Friend>() {
//            @Override
//            public int compare(Friend o1, Friend o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
        mAddGroupAdapter.setFriendList(mAddedFriends);
        mNotAddFriends.remove(position);
        mAddGroupSelectAdapter.setFriendList(mNotAddFriends);
    }

    @Override
    public void changeNotAddFriendList(Friend friend, int position) {
        mNotAddFriends.add(friend);
        Collections.sort(mNotAddFriends, new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        mAddGroupSelectAdapter.setFriendList(mNotAddFriends);
        mAddedFriends.remove(position);
        mAddGroupAdapter.setFriendList(mAddedFriends);
    }
}
