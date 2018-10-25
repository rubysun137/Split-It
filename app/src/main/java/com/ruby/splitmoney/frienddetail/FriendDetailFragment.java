package com.ruby.splitmoney.frienddetail;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.FriendDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FriendDetailFragment extends Fragment implements FriendDetailContract.View, View.OnClickListener {

    private FriendDetailContract.Presenter mPresenter;
    private String mFriendName;
    private FriendDetailAdapter mFriendListAdapter;
    private TextView mNameTitle;
    private TextView mNameBig;
    private TextView mClearBalance;
    private TextView mWhoOwe;
    private TextView mOweWho;
    private TextView mOweMoney;
    private List<Friend> mFriendList;
    private Friend mFriend;
    private LinearLayout mBalancedLayout;
    private LinearLayout mNotBalancedLayout;
    private LinearLayout mNoListLayout;
    private Dialog mDialog;
    private Context mContext;
    private double mBalanceMoney;
    private ImageView mFriendDetailImage;
    private TextView mDialogWhoOwe;
    private TextView mDialogOweWho;
    private ImageView mWhoOweImage;
    private ImageView mOweWhoImage;
    private EditText mSettleMoney;
    private ProgressBar mProgressBar;

    public FriendDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_detail, container, false);
        mPresenter = new FriendDetailPresenter(this);
        mPresenter.start();
        mContext = container.getContext();
        mFriendName = getArguments().getString("name", "Friend");

        mNameTitle = view.findViewById(R.id.friend_detail_friend_title);
        mNameBig = view.findViewById(R.id.friend_detail_friend_name);
        mClearBalance = view.findViewById(R.id.friend_detail_clear_balance);
        mWhoOwe = view.findViewById(R.id.friend_detail_who_owe);
        mOweWho = view.findViewById(R.id.friend_detail_owe_who);
        mOweMoney = view.findViewById(R.id.friend_detail_own_money);
        mBalancedLayout = view.findViewById(R.id.money_is_balance_linear_layout);
        mNotBalancedLayout = view.findViewById(R.id.money_is_not_balance_linear_layout);
        mNoListLayout = view.findViewById(R.id.no_list_linear_layout);
        mFriendDetailImage = view.findViewById(R.id.friend_detail_user_image);
        mProgressBar = view.findViewById(R.id.friend_detail_progress_bar);


        mNameTitle.setText(mFriendName);
        mNameBig.setText(mFriendName);
        mWhoOwe.setText(mFriendName);
        mNotBalancedLayout.setVisibility(View.GONE);
        mBalancedLayout.setVisibility(View.GONE);
        mNoListLayout.setVisibility(View.GONE);
        mClearBalance.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mClearBalance.setOnClickListener(this);
        mFriendList = new ArrayList<>(FriendList.getInstance().getFriendList());
        for (Friend friend : mFriendList) {
            if (friend.getName().equals(mFriendName)) {
                mFriend = friend;
                if (mFriend.getImage() != null) {
                    Glide.with(mContext).load(Uri.parse(mFriend.getImage())).into(mFriendDetailImage);
                }
            }
        }

        RecyclerView recyclerView = view.findViewById(R.id.friend_detail_recycler_view);
        mFriendListAdapter = new FriendDetailAdapter(mPresenter, mFriend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFriendListAdapter);
        mPresenter.loadEvents(mFriend);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_detail_clear_balance:
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_settle_up, null, false);
                mDialog = new AlertDialog.Builder(getContext())
                        .setView(view)
                        .show();
                mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


                mDialogWhoOwe = view.findViewById(R.id.dialog_who_owe);
                mDialogOweWho = view.findViewById(R.id.dialog_owe_who);
                mWhoOweImage = view.findViewById(R.id.who_owe_image);
                mOweWhoImage = view.findViewById(R.id.owe_who_image);
                mSettleMoney = view.findViewById(R.id.settle_money_edit_text);

                if (mBalanceMoney > 0) {
                    if(mFriend.getImage()!=null)
                    Glide.with(this).load(Uri.parse(mFriend.getImage())).into(mWhoOweImage);
                    if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null)
                    Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(mOweWhoImage);
                    mDialogWhoOwe.setText(mFriendName);
                    mDialogOweWho.setText("你");
                    mSettleMoney.setText(String.valueOf(mBalanceMoney));
                } else if (mBalanceMoney < 0) {
                    if(mFriend.getImage()!=null)
                        Glide.with(this).load(Uri.parse(mFriend.getImage())).into(mOweWhoImage);
                    if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null)
                        Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(mWhoOweImage);
                    mDialogWhoOwe.setText("你");
                    mDialogOweWho.setText(mFriendName);
                    mSettleMoney.setText(String.valueOf(0 - mBalanceMoney));
                }
                view.findViewById(R.id.dialog_positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double settleMoney = Double.parseDouble(mSettleMoney.getText().toString());
                        if (Math.abs(mBalanceMoney) >= settleMoney) {
                            mPresenter.setSettleUpToFirebase(settleMoney, mBalanceMoney);
                            mDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "請輸入 " + Math.abs(mBalanceMoney) + " 或以下的數字", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                view.findViewById(R.id.dialog_negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void setPresenter(FriendDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEvents(List<Event> events, List<Double> moneyList, Map<Event, Double> map) {
        mProgressBar.setVisibility(View.GONE);
        if (events.size() == 0) {
            mNoListLayout.setVisibility(View.VISIBLE);
            mNotBalancedLayout.setVisibility(View.GONE);
            mBalancedLayout.setVisibility(View.GONE);
            mClearBalance.setVisibility(View.GONE);
        } else {
            mFriendListAdapter.setEvents(events, moneyList);
            mBalanceMoney = 0;
            for (Double money : moneyList) {
                mBalanceMoney += money;
            }
            mBalanceMoney = ((double) Math.round(mBalanceMoney * 100) / 100);
            if (mBalanceMoney > 0) {
                mNoListLayout.setVisibility(View.GONE);
                mNotBalancedLayout.setVisibility(View.VISIBLE);
                mBalancedLayout.setVisibility(View.GONE);
                mWhoOwe.setText(mFriendName);
                mOweWho.setText("你");
                mOweMoney.setText(String.valueOf(mBalanceMoney));
                mClearBalance.setVisibility(View.VISIBLE);
            } else if (mBalanceMoney < 0) {
                mNoListLayout.setVisibility(View.GONE);
                mNotBalancedLayout.setVisibility(View.VISIBLE);
                mBalancedLayout.setVisibility(View.GONE);
                mWhoOwe.setText("你");
                mOweWho.setText(mFriendName);
                mOweMoney.setText(String.valueOf(0 - mBalanceMoney));
                mClearBalance.setVisibility(View.VISIBLE);
            } else {
                mNoListLayout.setVisibility(View.GONE);
                mNotBalancedLayout.setVisibility(View.GONE);
                mBalancedLayout.setVisibility(View.VISIBLE);
                mClearBalance.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void setListDetailPage(Event event) {
        ((MainActivity) getActivity()).showListDetailPage(event);
    }
}
