package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.frienddetail.FriendDetailContract;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.List;


public class FriendDetailAdapter extends RecyclerView.Adapter {

    private FriendDetailContract.Presenter mPresenter;
    private Friend mFriend;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;
    private List<Double> mMoneyList;
    private Context mContext;

    public void setEvents(List<Event> events, List<Double> moneyList) {
        mMoneyList = new ArrayList<>(moneyList);
        mEventList = new ArrayList<>(events);
        notifyDataSetChanged();

    }


    public FriendDetailAdapter(FriendDetailContract.Presenter presenter, Friend friend) {
        mPresenter = presenter;
        mFriend = friend;
        mEventList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_detail_list, parent, false);
        mContext = parent.getContext();
        return new FriendDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FriendDetailViewHolder) holder).bindView();

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    private class FriendDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mBalanceType;
        private TextView mBalanceMoney;
        private TextView mDollarSign;

        public FriendDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.split_list_name);
            mBalanceType = itemView.findViewById(R.id.friend_detail_list_balance_type);
            mBalanceMoney = itemView.findViewById(R.id.friend_detail_list_balance_money);
            mDollarSign = itemView.findViewById(R.id.friend_detail_list_dollar_sign);

        }

        private void bindView() {
            Log.d("EVENT NUMBER", "bindView: position: " + getAdapterPosition());
            Log.d("EVENT NUMBER", "event size: " + mEventList.size());
            Log.d("EVENT NUMBER", "money size: " + mMoneyList.size());
            int position = getAdapterPosition();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CLICK LIST!!!!", "onClick: ");
                    mPresenter.transToListDetailPage(mEventList.get(getAdapterPosition()));
                }
            });
            mTitle.setText(mEventList.get(position).getName());
            if (mEventList.get(position).isSettleUp()) {
                if (mMoneyList.get(position) < 0) {
                    mBalanceType.setText(R.string.friend_pay_back);
                    mBalanceMoney.setText(String.valueOf(0 - mMoneyList.get(position)));
                    setColor(android.R.color.white);

                } else if (mMoneyList.get(position) > 0) {
                    mBalanceType.setText(R.string.you_pay_back);
                    mBalanceMoney.setText(String.valueOf(mMoneyList.get(position)));
                    setColor(android.R.color.white);
                }
            } else {
                if (mMoneyList.get(position) < 0) {
                    mBalanceType.setText(R.string.you_should_pay);
                    mBalanceMoney.setText(String.valueOf(0 - mMoneyList.get(position)));
                    setColor(R.color.moneyRed);
                } else if (mMoneyList.get(position) > 0) {
                    mBalanceType.setText(R.string.you_lent);
                    mBalanceMoney.setText(String.valueOf(mMoneyList.get(position)));
                    setColor(R.color.moneyGreen);
                } else {
                    mBalanceType.setText(R.string.you_lent);
                    mBalanceMoney.setText(String.valueOf(0));
                    setColor(android.R.color.white);
                }
            }
        }

        private void setColor(int color) {
            mBalanceType.setTextColor(ContextCompat.getColor(mContext, color));
            mBalanceMoney.setTextColor(ContextCompat.getColor(mContext, color));
            mDollarSign.setTextColor(ContextCompat.getColor(mContext, color));
        }
    }
}
