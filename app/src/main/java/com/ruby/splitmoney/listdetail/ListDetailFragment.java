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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.ListDetailAdapter;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.util.Constants;

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
    private ProgressBar mProgressBar;

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
        mProgressBar = view.findViewById(R.id.list_detail_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitle.setText(mEvent.getName());
        mTotalMoney.setText(String.valueOf(mEvent.getMoney()));
        mDate.setText(mEvent.getDate());


        RecyclerView recyclerView = view.findViewById(R.id.list_detail_recycler_view);
        mListDetailAdapter = new ListDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mListDetailAdapter);

        mDeleteButton.setOnClickListener(this);

        mPresenter.getListMessage(mEvent);

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
                mFirestore.collection(Constants.EVENTS).document(mEvent.getId()).collection(Constants.MEMBERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            if (snapshot.getDouble(Constants.PAY) == 0.0) {

                                mMemberId.add(snapshot.getId());
                            } else {
                                mPayId = snapshot.getId();
                            }
                        }
                        if (mEvent.getGroup().equals("")) {
                            //從 user 的 event list 中刪除
                            for (String id : mMemberId) {
                                if (!id.equals(mPayId)) {
                                    mFirestore.collection(Constants.USERS).document(mPayId).collection(Constants.FRIENDS).document(id).update(Constants.EVENTS, FieldValue.arrayRemove(mEvent.getId()));
                                    mFirestore.collection(Constants.USERS).document(id).collection(Constants.FRIENDS).document(mPayId).update(Constants.EVENTS, FieldValue.arrayRemove(mEvent.getId()));
                                }
                            }
                        } else {
                            //從 group list 中刪除
                            mFirestore.collection(Constants.GROUPS).document(mEvent.getGroup()).update(Constants.EVENTS, FieldValue.arrayRemove(mEvent.getId()));
                        }
                        //刪除 event
                        mFirestore.collection(Constants.EVENTS).document(mEvent.getId()).delete();
                        mDialog.dismiss();
                        getFragmentManager().popBackStack();
                    }
                });
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

    @Override
    public void showListMessage(List<DocumentSnapshot> snapshots) {
        mProgressBar.setVisibility(View.GONE);
        mListDetailAdapter.setSnapshots(snapshots);
    }
}
