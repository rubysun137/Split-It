package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.groupbalance.GroupBalanceContract;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.getColor;

public class GroupBalanceAdapter extends RecyclerView.Adapter {

    private GroupBalanceContract.Presenter mPresenter;
    private Map<String, Double> mMoneyMap;
    private FirebaseUser mUser;
    private List<Friend> mFriendList;
    private List<Double> mMoneyList;
    private Context mContext;

    public GroupBalanceAdapter(GroupBalanceContract.Presenter presenter) {
        mPresenter = presenter;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mMoneyMap = new HashMap<>();
        mFriendList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
    }

    public void setMoneyMap(Map<String, Double> moneyMap) {
        mMoneyMap = new HashMap<>(moneyMap);
        mFriendList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        String picture;
        if (mUser.getPhotoUrl() == null) {
            picture = null;
        } else {
            picture = mUser.getPhotoUrl().toString();
        }
        //先把自己加進 list
        mFriendList.add(new Friend(mUser.getEmail(), mUser.getUid(), mUser.getDisplayName(), 0.0, picture));
        mMoneyList.add(mMoneyMap.get(mUser.getUid()));
        List<Friend> friends = FriendList.getInstance().getFriendList();
        for (Friend friend : friends) {
            if (mMoneyMap.containsKey(friend.getUid())) {
                mFriendList.add(friend);
                mMoneyList.add(mMoneyMap.get(friend.getUid()));
                Log.d("MONEY MAP ", "setMoneyMap: " + mMoneyMap.get(friend.getUid()));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_balance, viewGroup, false);
        mContext = viewGroup.getContext();
        return new GroupBalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((GroupBalanceViewHolder) viewHolder).bindView();
    }

    @Override
    public int getItemCount() {
        return mMoneyMap.size();
    }

    private class GroupBalanceViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMemberImage;
        private TextView mMemberName;
        private TextView mMoney;
        private int mPosition;

        public GroupBalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            mMemberImage = itemView.findViewById(R.id.default_member_image);
            mMemberName = itemView.findViewById(R.id.member_name_text);
            mMoney = itemView.findViewById(R.id.member_list_money);

        }

        private void bindView() {

            mPosition = getAdapterPosition();
            if (mFriendList.get(mPosition).getImage() != null) {
                Glide.with(mContext).load(Uri.parse(mFriendList.get(mPosition).getImage())).into(mMemberImage);
            } else {
                Glide.with(mContext).load(R.drawable.user2).into(mMemberImage);
            }
            mMemberName.setText(mFriendList.get(mPosition).getName());
            mMoney.setText(String.valueOf(mMoneyList.get(mPosition)));
            if (mMoneyList.get(mPosition) > 0) {
                String text = "+" + mMoneyList.get(mPosition);
                mMoney.setText(text);
                mMoney.setTextColor(getColor(mContext, R.color.moneyGreen));
            } else if (mMoneyList.get(mPosition) < 0) {
                mMoney.setText(String.valueOf(mMoneyList.get(mPosition)));
                mMoney.setTextColor(getColor(mContext, R.color.moneyRed));
            } else {
                mMoney.setText("帳務結清");
                mMoney.setTextColor(getColor(mContext, R.color.lightGray));
            }

            //TODO 長按結算功能
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //不是自己
                    if (mPosition != 0) {
                        //我還錢或別人還錢
                        if ((mMoneyList.get(0) > 0 && mMoneyList.get(mPosition) < 0) || (mMoneyList.get(0) < 0 && mMoneyList.get(mPosition) > 0)) {

                            mPresenter.deleteEvent(mPosition, mFriendList, mMoneyList);
                        }

                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
