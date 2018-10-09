package com.ruby.splitmoney.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.addlist.AddListContract;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.FriendList;

import java.util.List;

public class AddSplitMemberAdapter extends RecyclerView.Adapter {

    private AddListContract.Presenter mPresenter;
    private List<Friend> mFriends;

    public AddSplitMemberAdapter(AddListContract.Presenter presenter,List<Friend> friends) {
        mPresenter = presenter;
        mFriends = friends;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_list_member,parent,false);

        return new AddSplitMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AddSplitMemberViewHolder) holder).bindView();
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    private class AddSplitMemberViewHolder extends RecyclerView.ViewHolder{

        private TextView mName;

        public AddSplitMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.add_list_friend_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.addSplitFriendView(getAdapterPosition());
                }
            });
        }

        private void bindView (){
            mName.setText(mFriends.get(getAdapterPosition()).getName());
        }
    }
}
