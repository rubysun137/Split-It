package com.ruby.splitmoney.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.quicksplit.QuickSplitContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSplitPartialAdapter extends RecyclerView.Adapter {

    private QuickSplitContract.Presenter mPresenter;
    private int mTotalMoney;
    private int mTotalMember;
    private int mPartialMoney;
//    private List<Integer> mExtraMoney;

    public QuickSplitPartialAdapter(String money, String member, QuickSplitContract.Presenter presenter) {
        mTotalMoney = Integer.parseInt(money);
        mTotalMember = Integer.parseInt(member);
        mPresenter = presenter;
//        mExtraMoney = Arrays.asList(new Integer[mTotalMember]);
        mPresenter.setListSize(mTotalMember);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_partial, parent, false);

        return new QuickSplitPartialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((QuickSplitPartialViewHolder) holder).bindView();

    }

    @Override
    public int getItemCount() {
        return mTotalMember;
    }

    private class QuickSplitPartialViewHolder extends RecyclerView.ViewHolder {

        private TextView mMemberNumber;
//        private TextView mAverageMoney;
        private EditText mAddMoney;
        private int mPosition;
        private List<String> mStringList;

        private QuickSplitPartialViewHolder(@NonNull View itemView) {
            super(itemView);
            mMemberNumber = itemView.findViewById(R.id.partial_split_member_number);
//            mAverageMoney = itemView.findViewById(R.id.partial_spit_average_money);
            mAddMoney = itemView.findViewById(R.id.partial_split_add_money);
            mStringList = new ArrayList<>();
            for(int i = 0;i<mTotalMember;i++){
                mStringList.add(i,"");
            }

        }

        private void bindView() {
            mPosition = getAdapterPosition();
            mMemberNumber.setText(String.valueOf(mPosition + 1));

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
