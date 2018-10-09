package com.ruby.splitmoney.adapters;

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

import java.util.ArrayList;
import java.util.List;

public class SplitPartialAdapter extends RecyclerView.Adapter {

    private AddListContract.Presenter mPresenter;
    private int mTotalMoney;
    private List<Friend> mFriends;
    private int mPartialMoney;

    public SplitPartialAdapter(String money, List<Friend> friends, AddListContract.Presenter presenter) {
        mTotalMoney = Integer.parseInt(money);
        mFriends = new ArrayList<>(friends);
        mPresenter = presenter;
        mPresenter.setListSize(mFriends.size()+1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_partial, parent, false);

        return new SplitPartialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SplitPartialViewHolder) holder).bindView();

    }

    @Override
    public int getItemCount() {
        return mFriends.size()+1;
    }

    private class SplitPartialViewHolder extends RecyclerView.ViewHolder {

        private TextView mMember;
        private EditText mAddMoney;
        private int mPosition;
        private List<String> mStringList;

        private SplitPartialViewHolder(@NonNull View itemView) {
            super(itemView);
            mMember = itemView.findViewById(R.id.partial_split_member_number);
            mAddMoney = itemView.findViewById(R.id.partial_split_add_money);
            mStringList = new ArrayList<>();
            for(int i = 0;i<=mFriends.size();i++){
                mStringList.add(i,"");
            }

        }

        private void bindView() {
            mPosition = getAdapterPosition();
            if(mPosition==0){
                mMember.setText("你");
            }else {
                mMember.setText(mFriends.get(mPosition - 1).getName());
            }
            mAddMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    mStringList.set(mPosition,mAddMoney.getText().toString());
                    String add = mAddMoney.getText().toString();
                        if(add.equals("")){
                            add = "0";
                        }
                    mPresenter.addExtraMoneyList(mPosition,Integer.valueOf(add));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            mAddMoney.setText(mStringList.get(mPosition));
        }
    }
}
