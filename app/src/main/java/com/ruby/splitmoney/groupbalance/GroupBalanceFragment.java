package com.ruby.splitmoney.groupbalance;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.GroupBalanceAdapter;
import com.ruby.splitmoney.adapters.GroupDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.objects.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupBalanceFragment extends Fragment implements GroupBalanceContract.View, View.OnClickListener {

    private GroupBalanceContract.Presenter mPresenter;
    private Context mContext;
    private Group mGroup;
    private FirebaseFirestore mFirestore;
    private List<Event> mEventList;
    private TextView mNoEventText;
    private GroupBalanceAdapter mBalanceAdapter;
    private AlertDialog mDialog;
    private TextView mDialogWhoOwe;
    private TextView mDialogOweWho;
    private ImageView mWhoOweImage;
    private ImageView mOweWhoImage;
    private EditText mSettleMoney;




    public GroupBalanceFragment() {
        // Required empty public constructor
    }

    public void setGroup(Group group) {
        mGroup = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_balance, container, false);
        mPresenter = new GroupBalancePresenter(this, container.getContext());
        mPresenter.start();
        mContext = container.getContext();

        mFirestore = FirebaseFirestore.getInstance();
        mNoEventText = view.findViewById(R.id.no_event_text);

        RecyclerView recyclerView = view.findViewById(R.id.group_detail_balance_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBalanceAdapter = new GroupBalanceAdapter(mPresenter);
        recyclerView.setAdapter(mBalanceAdapter);

        mPresenter.calculateMoney(mGroup);

        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupBalanceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoEventSign() {
        mNoEventText.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateBalance(Map<String, Double> moneyMap) {
        mBalanceAdapter.setMoneyMap(moneyMap);
    }

    @Override
    public void showDeleteEventDialog(int position, List<Friend> friendList, List<Double> moneyList) {
        Double balanceMoney = Math.abs(moneyList.get(0)) > Math.abs(moneyList.get(position)) ? Math.abs(moneyList.get(position)) : Math.abs(moneyList.get(0));
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_settle_up, null, false);
        mDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .show();
        mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        mDialogWhoOwe = view.findViewById(R.id.dialog_who_owe);
        mDialogOweWho = view.findViewById(R.id.dialog_owe_who);
        mWhoOweImage = view.findViewById(R.id.who_owe_image);
        mOweWhoImage = view.findViewById(R.id.owe_who_image);
        mSettleMoney = view.findViewById(R.id.settle_money_edit_text);

        if (moneyList.get(0) > 0 && moneyList.get(position) < 0) {
            //別人要還錢
        } else if (moneyList.get(0) < 0 && moneyList.get(position) > 0) {
            //我要還錢
        }
    }
}
