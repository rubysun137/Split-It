package com.ruby.splitmoney.listdetail;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Event;

import java.util.List;

public class ListDetailPresenter implements ListDetailContract.Presenter {

    private ListDetailContract.View mView;
    private List<DocumentSnapshot> mSnapshots;


    public ListDetailPresenter(ListDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);

    }


    @Override
    public void start() {

    }

    @Override
    public void getListMessage(Event event) {
        FirebaseFirestore.getInstance().collection("events").document(event.getId()).collection("members").orderBy("pay", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mSnapshots = queryDocumentSnapshots.getDocuments();
                mView.showListMessage(mSnapshots);
            }
        });
    }
}
