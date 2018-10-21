package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.groupdetail.GroupDetailContract;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailAdapter extends RecyclerView.Adapter {

    private GroupDetailContract.Presenter mPresenter;
    private Friend mFriend;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;
    private List<Double> mMoneyList;
    private Context mContext;

    public void setEvents(List<Event> events){
        mEventList = new ArrayList<>(events);
        notifyDataSetChanged();

    }


    public GroupDetailAdapter(GroupDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mEventList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_detail_list, parent, false);
        mContext = parent.getContext();
        return new GroupDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GroupDetailViewHolder) holder).bindView();

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }


    private class GroupDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDate;
        private TextView mTotalMoney;
        private TextView mPayByWho;

        public GroupDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.group_event_list_name);
            mDate = itemView.findViewById(R.id.group_detail_list_date);
            mTotalMoney = itemView.findViewById(R.id.group_event_list_total_money);
            mPayByWho = itemView.findViewById(R.id.group_event_pay_by_who);

        }

        private void bindView() {
            int position = getAdapterPosition();
            mTitle.setText(mEventList.get(position).getName());
            mDate.setText(mEventList.get(position).getDate());
            mTotalMoney.setText(String.valueOf(mEventList.get(position).getMoney()));
            String payMan = "由 "+mEventList.get(position).getPayBy()+" 付款";
            mPayByWho.setText(payMan);
        }

//        private void setColor(int color){
//            mBalanceType.setTextColor(getColor(mContext, color));
//            mTotalMoney.setTextColor(getColor(mContext,color));
//            mDollarSign.setTextColor(getColor(mContext,color));
//        }
    }
}
