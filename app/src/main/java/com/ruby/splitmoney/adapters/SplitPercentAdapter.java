package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.addlist.AddListContract;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.quicksplit.QuickSplitContract;

import java.util.ArrayList;
import java.util.List;

public class SplitPercentAdapter extends RecyclerView.Adapter {
    private AddListContract.Presenter mPresenter;
    private List<Friend> mFriends;
    private int mTotalMoney;
    private Context mContext;

    public SplitPercentAdapter(String money, List<Friend> friends, AddListContract.Presenter presenter) {
        mTotalMoney = Integer.parseInt(money);
        mFriends = new ArrayList<>(friends);
        mPresenter = presenter;
        mPresenter.setListSize(mFriends.size() + 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_percent, parent, false);
        mContext = parent.getContext();
        return new SplitPercentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SplitPercentViewHolder) holder).bindView();
    }

    @Override
    public int getItemCount() {
        return mFriends.size() + 1;
    }

    private class SplitPercentViewHolder extends RecyclerView.ViewHolder {
        private TextView mMember;
        private EditText mSharedMoney;
        private int mPosition;
        private List<String> mStringList;

        public SplitPercentViewHolder(@NonNull View itemView) {
            super(itemView);
            mMember = itemView.findViewById(R.id.partial_split_percent_member);
            mSharedMoney = itemView.findViewById(R.id.partial_split_percent_share_money);
            mStringList = new ArrayList<>();
            for (int i = 0; i <= mFriends.size(); i++) {
                mStringList.add(i, "");
            }
        }

        private void bindView() {
            mPosition = getAdapterPosition();
            if (mPosition == 0) {
                mMember.setText(mContext.getString(R.string.you));
            } else {
                mMember.setText(mFriends.get(mPosition - 1).getName());
            }
            mSharedMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    mStringList.set(mPosition, mSharedMoney.getText().toString());
                    String share = mSharedMoney.getText().toString();
                    if (share.equals("")) {
                        share = "0";
                    }
                    mPresenter.addSharedMoneyList(mPosition, Integer.valueOf(share));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            mSharedMoney.setText(mStringList.get(mPosition));
        }
    }
}
