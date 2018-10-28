package com.ruby.splitmoney.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.quicksplit.QuickSplitContract;

import java.util.ArrayList;
import java.util.List;

public class QuickSplitPartialAdapter extends RecyclerView.Adapter {

    private QuickSplitContract.Presenter mPresenter;
    private int mTotalMoney;
    private int mTotalMember;
    private int mPartialMoney;
    private List<String> mMoneyList;
    private List<String> mMemberList;
//    private List<Integer> mExtraMoney;

    public QuickSplitPartialAdapter(String money, String memberNumber, QuickSplitContract.Presenter presenter) {
        mTotalMoney = Integer.parseInt(money);
        mTotalMember = Integer.parseInt(memberNumber);
        mPresenter = presenter;
//        mExtraMoney = Arrays.asList(new Integer[mTotalMember]);
        mPresenter.setListSize(mTotalMember);
        mMoneyList = new ArrayList<>();
        for (int i = 0; i < mTotalMember; i++) {
            mMoneyList.add(i, "");
        }
        mMemberList = new ArrayList<>();
        for (int i = 0; i < mTotalMember; i++) {
            mMemberList.add(i, "成員" + (i + 1));
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quick_dialog_partial, parent, false);

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

        private EditText mMemberNumber;
        //        private TextView mAverageMoney;
        private EditText mAddMoney;
        private int mPosition;


        private QuickSplitPartialViewHolder(@NonNull View itemView) {
            super(itemView);
            mMemberNumber = itemView.findViewById(R.id.partial_quick_split_member_edit_text);
//            mAverageMoney = itemView.findViewById(R.id.partial_spit_average_money);
            mAddMoney = itemView.findViewById(R.id.partial_quick_split_add_money);


        }

        private void bindView() {
            mPosition = getAdapterPosition();
            mMemberNumber.setText(mMemberList.get(mPosition));
            mMemberNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String member = mMemberNumber.getText().toString();
                    mMemberList.set(mPosition, member);
                    mPresenter.addMemberNameList(mPosition, member);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mAddMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    mMoneyList.set(mPosition, mAddMoney.getText().toString());
                    String add = mAddMoney.getText().toString();
                    if (add.equals("")) {
                        add = "0";
                    }
                    mPresenter.addExtraMoneyList(mPosition, Integer.valueOf(add));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            mAddMoney.setText(mMoneyList.get(mPosition));
        }
    }
}
