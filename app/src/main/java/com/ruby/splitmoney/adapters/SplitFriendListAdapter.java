package com.ruby.splitmoney.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.friend.FriendContract;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitFriendListAdapter extends RecyclerView.Adapter {

    private FriendContract.Presenter mPresenter;
    private List<Friend> mFriendNameList;

    public void setFriendList(){
        mFriendNameList = new ArrayList<>(FriendList.getInstance().getFriendList());
        notifyDataSetChanged();
    }

    public SplitFriendListAdapter(FriendContract.Presenter presenter) {
        mPresenter = presenter;
        mFriendNameList = new ArrayList<>();
        String[] friends = new String[]{"恩涵","冠賢","Roger","文彬","Ruby","瓘閎","Paula","Justin","Sandy","Wayne"};

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);

        return new SplitFriendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SplitFriendListViewHolder) holder).bindView();
    }

    @Override
    public int getItemCount() {
        return mFriendNameList.size();
    }

    private class  SplitFriendListViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;

        public SplitFriendListViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.friend_name_text);
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
        }

        private void bindView(){
            int position = getAdapterPosition();
            mName.setText(mFriendNameList.get(position).getName());
        }
    }
}
