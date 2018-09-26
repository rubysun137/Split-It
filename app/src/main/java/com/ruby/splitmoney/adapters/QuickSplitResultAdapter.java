package com.ruby.splitmoney.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruby.splitmoney.R;

import java.util.List;

public class QuickSplitResultAdapter extends RecyclerView.Adapter {

    private List<Double> mResults;

    public QuickSplitResultAdapter(List<Double> results) {
        mResults = results;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quick_split_result,null);
        return new QuickSplitPartialResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((QuickSplitPartialResultViewHolder)holder).bindView();

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    private class QuickSplitPartialResultViewHolder extends RecyclerView.ViewHolder{
        private TextView mMember;
        private TextView mNumber;
        private int mPosition;

        public QuickSplitPartialResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mMember = itemView.findViewById(R.id.partial_split_result_member);
            mNumber = itemView.findViewById(R.id.partial_split_result_number);
        }

        private void bindView(){
            mPosition = getAdapterPosition();
            String member = "成員"+(mPosition+1);
            mMember.setText(member);
            Log.d("TAG  ", "bindView: "+mResults);
            mNumber.setText(String.valueOf(mResults.get(mPosition)));
        }
    }
}
