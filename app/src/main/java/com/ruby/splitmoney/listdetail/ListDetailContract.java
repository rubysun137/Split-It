package com.ruby.splitmoney.listdetail;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface ListDetailContract {

    interface View extends BaseView<Presenter> {

        void showListMessage(List<DocumentSnapshot> snapshots);
    }

    interface Presenter extends BasePresenter {

        void getListMessage(Event event);

    }
}
