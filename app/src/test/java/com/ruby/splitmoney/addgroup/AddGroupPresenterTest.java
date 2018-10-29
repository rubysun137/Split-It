package com.ruby.splitmoney.addgroup;

import android.text.Editable;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruby.splitmoney.objects.Friend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class,FirebaseAuth.class})
public class AddGroupPresenterTest {
    FirebaseFirestore mFirestore;
    CollectionReference mCollectionReference;
    AddGroupPresenter mPresenter;
    AddGroupFragment mView;


    @Before
    public void setUp() throws Exception {
        mFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mFirestore);
//        FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);
//        PowerMockito.mockStatic(FirebaseAuth.class);
//        when(FirebaseAuth.getInstance()).thenReturn(firebaseAuth);
//        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
//        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
//        when(firebaseUser.getUid()).thenReturn("myUid");
//        mCollectionReference = Mockito.mock(CollectionReference.class);
//        when(mFirestore.collection(anyString())).thenReturn(mCollectionReference);
//        Task<DocumentReference> task = Mockito.mock(Task.class);
//        OnSuccessListener<DocumentReference> onSuccessListener = Mockito.mock(OnSuccessListener.class);
//        when(mCollectionReference.add(anyString())).thenReturn(task);
        mView = Mockito.mock(AddGroupFragment.class);
        mPresenter = new AddGroupPresenter(mView);
    }

    @Test
    public void saveGroupData() {

//        String groupName = "test";
//        List<Friend> friends = new ArrayList<>();
//        Friend friend = new Friend("ruby@mail.com","friendUid","Ruby", 0.0,null);
//        friends.add(friend);
//        mPresenter.saveGroupData(groupName,friends);
//        verify(mCollectionReference,times(1)).add(null);
    }

    @Test
    public void saveButtonClicked() {
        EditText editText = Mockito.mock(EditText.class);
        Editable editable = Mockito.mock(Editable.class);
        when(editText.getText()).thenReturn(editable);
        when(editable.toString()).thenReturn("123");
//        when(editable.toString()).thenReturn("");
        List<Friend> friends = new ArrayList<>();
        Friend friend = new Friend("ruby@mail.com","friendUid","Ruby", 0.0,null);
        friends.add(friend);
        mPresenter.clickSaveButton(editText,friends);
        verify(mView,times(1)).popBackStack();
//        verify(mView,times(1)).showNoNameMessage();
//        verify(mView,times(1)).showNoFriendMessage();

    }
}