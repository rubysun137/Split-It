package com.ruby.splitmoney.listdetail;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.ListDetailAdapter;
import com.ruby.splitmoney.objects.Event;

import java.util.ArrayList;
import java.util.List;


public class ListDetailFragment extends Fragment implements ListDetailContract.View, View.OnClickListener {

    private ListDetailContract.Presenter mPresenter;
    private Event mEvent;
    private TextView mTitle;
    private TextView mTotalMoney;
    private TextView mDate;
    private ListDetailAdapter mListDetailAdapter;
    private Button mDeleteButton;
    private Context mContext;
    private Dialog mDialog;
    private FirebaseFirestore mFirestore;
    private List<String> mMemberId;
    private String mPayId;

    public void setEvent(Event event) {
        mEvent = event;
    }

    public ListDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_detail, container, false);
        mPresenter = new ListDetailPresenter(this);
        mPresenter.start();
        mContext = container.getContext();
        mFirestore = FirebaseFirestore.getInstance();
        mMemberId = new ArrayList<>();

        mTitle = view.findViewById(R.id.list_detail_name);
        mTotalMoney = view.findViewById(R.id.list_detail_total_money);
        mDate = view.findViewById(R.id.list_detail_date);
        mDeleteButton = view.findViewById(R.id.delete_list_button);

        mTitle.setText(mEvent.getName());
        mTotalMoney.setText(String.valueOf(mEvent.getMoney()));
        mDate.setText(mEvent.getDate());


        RecyclerView recyclerView = view.findViewById(R.id.list_detail_recycler_view);
        mListDetailAdapter = new ListDetailAdapter(mEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mListDetailAdapter);

        mDeleteButton.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete_friend, null, false);

        mDialog = new AlertDialog.Builder(mContext).setView(view).show();
        mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        TextView title = view.findViewById(R.id.delete_title);
        title.setText("刪除清單");
        TextView content = view.findViewById(R.id.delete_content);
        content.setText("請問確定要刪除帳務清單嗎?");
        view.findViewById(R.id.dialog_positive).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFirestore.collection("events").document(mEvent.getId()).collection("members").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            if (snapshot.getDouble("pay") == 0.0) {

                                mMemberId.add(snapshot.getId());
                            } else {
                                mPayId = snapshot.getId();
                            }
                        }
                    }
                });
                //TODO 刪除 event list 有錯
                for (String id : mMemberId) {
                    mFirestore.collection("users").document(mPayId).collection("friends").document(id).delete();
                    mFirestore.collection("users").document(id).collection("friends").document(mPayId).delete();
                }
                //刪除 event
                mFirestore.collection("events").document(mEvent.getId()).delete();
                mDialog.dismiss();
            }
        });

        view.findViewById(R.id.dialog_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void setPresenter(ListDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
