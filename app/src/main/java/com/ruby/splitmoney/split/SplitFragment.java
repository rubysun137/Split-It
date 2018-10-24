package com.ruby.splitmoney.split;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.friend.FriendFragment;
import com.ruby.splitmoney.group.GroupFragment;
import com.ruby.splitmoney.objects.User;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class SplitFragment extends Fragment implements SplitContract.View, View.OnClickListener {

    private SplitContract.Presenter mPresenter;
    private List<Fragment> mFragmentList;
    private FriendFragment mFriendFragment;
    private GroupFragment mGroupFragment;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private FloatingActionMenu mFab;
    private FloatingActionButton mAddListFab;
    private FloatingActionButton mAddFriendFab;
    private FloatingActionButton mAddGroupFab;
    private Context mContext;
    private View mDialogView;
    private Dialog mDialog;
    private FrameLayout mBackgroundLayout;


    public SplitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_split, container, false);
        mPresenter = new SplitPresenter(this);
        mPresenter.start();


        mContext = container.getContext();

        mFragmentList = new ArrayList<>();
        mGroupFragment = new GroupFragment();
        mFriendFragment = new FriendFragment();

        mFragmentList.add(mGroupFragment);
        mFragmentList.add(mFriendFragment);

        mPager = view.findViewById(R.id.viewPagerHolder);

        mPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });


        mTabLayout = view.findViewById(R.id.tabLayout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mBackgroundLayout = view.findViewById(R.id.fab_background);
        mBackgroundLayout.setOnClickListener(this);

        mFab = view.findViewById(R.id.fab);
        mFab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    mBackgroundLayout.setVisibility(View.VISIBLE);
                } else {
                    mBackgroundLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
//        mFab.setOnMenuButtonClickListener(this);
//        mFab.setClosedOnTouchOutside(true);

        mAddListFab = view.findViewById(R.id.fab_add_list);
        mAddFriendFab = view.findViewById(R.id.fab_add_friend);
        mAddGroupFab = view.findViewById(R.id.fab_add_group);


        mAddListFab.setOnClickListener(this);
        mAddFriendFab.setOnClickListener(this);
        mAddGroupFab.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean friendPage = bundle.getBoolean("friendPage");
            transToFriendPage(friendPage);
        }

        return view;
    }


    @Override
    public void setPresenter(SplitContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_list:
                mFab.close(false);
                ((MainActivity) getActivity()).showAddListPage();
//                mBackgroundLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.fab_add_friend:
                mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_friend, null, false);
                mDialog = new AlertDialog.Builder(getContext())
                        .setView(mDialogView)
                        .show();
                mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                mDialogView.findViewById(R.id.send_friend_email).setOnClickListener(this);
                mFab.close(true);
//                mBackgroundLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.fab_add_group:
                mFab.close(false);
                ((MainActivity) getActivity()).showAddGroupPage();
//                mBackgroundLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.send_friend_email:
                EditText mail = mDialogView.findViewById(R.id.add_friend_email);
                String friendEmail = mail.getText().toString();
                mPresenter.searchForFriend(friendEmail, mDialog.getContext());
                break;
            case R.id.fab_background:
                mFab.close(true);
//                mBackgroundLayout.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void closeAddFriendDialog(String name) {
        mDialog.dismiss();
        Toast.makeText(mContext, "已成功加 " + name + " 為好友", Toast.LENGTH_SHORT).show();
    }

    public void transToFriendPage(boolean friendPage) {
        if (friendPage) {
            mPager.setCurrentItem(1);
        }else {
            mPager.setCurrentItem(0);
        }
    }
}
