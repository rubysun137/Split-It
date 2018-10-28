package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.friend.FriendContract;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

public class SplitFriendListAdapter extends RecyclerView.Adapter {

    private FriendContract.Presenter mPresenter;
    private List<Friend> mFriendNameList;
    private Context mContext;

    public void setFriendList() {
        mFriendNameList = new ArrayList<>(FriendList.getInstance().getFriendList());
        notifyDataSetChanged();
    }

    public SplitFriendListAdapter(FriendContract.Presenter presenter) {
        mPresenter = presenter;
        mFriendNameList = new ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        mContext = parent.getContext();

        return new SplitFriendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SplitFriendListViewHolder) holder).bindView();
    }

    @Override
    public int getItemCount() {
        return mFriendNameList.size() + 1;
    }

    private class SplitFriendListViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mName;
        private TextView mMoney;
        private View mDivider;
        private ConstraintLayout mLayout;

        public SplitFriendListViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.default_user_image);
            mName = itemView.findViewById(R.id.friend_name_text);
            mMoney = itemView.findViewById(R.id.friend_list_money);
            mDivider = itemView.findViewById(R.id.friend_divider);
            mLayout = itemView.findViewById(R.id.item_friend_layout);
        }

        private void bindView() {

            int position = getAdapterPosition();
            if (position == mFriendNameList.size()) {
                mLayout.setVisibility(View.INVISIBLE);
            } else {

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.transToFriendDetailPage(mFriendNameList.get(getAdapterPosition()).getName());
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mPresenter.deleteFriendDialog(mFriendNameList.get(getAdapterPosition()).getUid());
                        return true;
                    }
                });

                mLayout.setVisibility(View.VISIBLE);
                if (position == mFriendNameList.size() - 1) {
                    mDivider.setVisibility(View.INVISIBLE);
                } else {
                    mDivider.setVisibility(View.VISIBLE);
                }
                if (mFriendNameList.get(position).getImage() != null) {
                    Glide.with(mContext).load(Uri.parse(mFriendNameList.get(position).getImage())).into(mImage);
                } else {
                    Glide.with(mContext).load(R.drawable.user2).into(mImage);
                }
                mName.setText(mFriendNameList.get(position).getName());
                if (mFriendNameList.get(position).getMoney() > 0) {
                    String text = mContext.getString(R.string.plus) + mFriendNameList.get(position).getMoney();
                    mMoney.setText(text);
                    mMoney.setTextColor(getColor(mContext, R.color.moneyGreen));
                    mMoney.setVisibility(View.VISIBLE);
                } else if (mFriendNameList.get(position).getMoney() < 0) {
                    mMoney.setText(String.valueOf(mFriendNameList.get(position).getMoney()));
                    mMoney.setTextColor(getColor(mContext, R.color.moneyRed));
                    mMoney.setVisibility(View.VISIBLE);
                } else if (mFriendNameList.get(position).getMoney() == 0) {
                    mMoney.setVisibility(View.INVISIBLE);
                }
            }

        }
    }
}
