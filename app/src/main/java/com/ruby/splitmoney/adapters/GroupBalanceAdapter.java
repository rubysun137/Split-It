package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.graphics.Color;
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
    private List<Friend> mFriendsList;
    private List<Double> mMoneyList;
    private Context mContext;

    public GroupBalanceAdapter(GroupBalanceContract.Presenter presenter) {
        mPresenter = presenter;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mMoneyMap = new HashMap<>();
        mFriendsList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
    }

    public void setMoneyMap(Map<String, Double> moneyMap) {
        mMoneyMap = new HashMap<>(moneyMap);
        mFriendsList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mFriendsList.add(new Friend(mUser.getEmail(), mUser.getUid(),mUser.getDisplayName(),0.0,mUser.getPhotoUrl().toString()));
        mMoneyList.add(mMoneyMap.get(mUser.getUid()));
        List<Friend> friends = FriendList.getInstance().getFriendList();
        for(Friend friend :friends){
            if(mMoneyMap.containsKey(friend.getUid())){
                mFriendsList.add(friend);
                mMoneyList.add(mMoneyMap.get(friend.getUid()));
                Log.d("MONEY MAP ", "setMoneyMap: "+mMoneyMap.get(friend.getUid()));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_balance,viewGroup,false);
        mContext = viewGroup.getContext();
        return new GroupBalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((GroupBalanceViewHolder)viewHolder).bindView();
    }

    @Override
    public int getItemCount() {
        return mMoneyMap.size();
    }

    private class GroupBalanceViewHolder extends RecyclerView.ViewHolder{

        private ImageView mMemberImage;
        private TextView mMemberName;
        private TextView mMoney;

        public GroupBalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            mMemberImage = itemView.findViewById(R.id.default_member_image);
            mMemberName = itemView.findViewById(R.id.member_name_text);
            mMoney = itemView.findViewById(R.id.member_list_money);

        }

        private void bindView(){

            int position = getAdapterPosition();
            if(mFriendsList.get(position).getImage()!=null){
                Glide.with(mContext).load(Uri.parse(mFriendsList.get(position).getImage())).into(mMemberImage);
            }else{
                Glide.with(mContext).load(R.drawable.user2).into(mMemberImage);
            }
            mMemberName.setText(mFriendsList.get(position).getName());
            mMoney.setText(String.valueOf(mMoneyList.get(position)));
            if (mMoneyList.get(position) > 0) {
                String text = "+" + mMoneyList.get(position);
                mMoney.setText(text);
                mMoney.setTextColor(getColor(mContext, R.color.moneyGreen));
            } else if (mMoneyList.get(position) < 0) {
                mMoney.setText(String.valueOf(mMoneyList.get(position)));
                mMoney.setTextColor(getColor(mContext, R.color.moneyRed));
            }else {
                mMoney.setText("帳務結清");
                mMoney.setTextColor(getColor(mContext, R.color.lightGray));
            }
        }
    }
}
