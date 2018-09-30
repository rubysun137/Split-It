package com.ruby.splitmoney.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.frienddetail.FriendDetailContract;

import java.util.List;

public class FriendDetailAdapter extends RecyclerView.Adapter {

    private FriendDetailContract.Presenter mPresenter;



    public FriendDetailAdapter(FriendDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quick_split_result,null);
        return new FriendDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FriendDetailViewHolder)holder).bindView();

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private class FriendDetailViewHolder extends RecyclerView.ViewHolder{

        public FriendDetailViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bindView(){
        }
    }
}
