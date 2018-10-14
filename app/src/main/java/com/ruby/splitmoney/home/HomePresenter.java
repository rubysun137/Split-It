package com.ruby.splitmoney.home;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;


    public HomePresenter(HomeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        showTotal();
    }


    @Override
    public void showTotal() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("users").document(mUser.getUid()).collection("friends").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int lentMoney = 0;
                int borrowedMoney = 0;
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    if(snapshot.getDouble("money")>0){
                        lentMoney++;
                    }else if(snapshot.getDouble("money")<0){
                        borrowedMoney++;
                    }
                }
                mView.showTotal(lentMoney,borrowedMoney);
            }
        });
    }
}
