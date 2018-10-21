package com.ruby.splitmoney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.BaseActivity;
import com.ruby.splitmoney.util.Constants;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends BaseActivity implements MainContract.View,
        View.OnClickListener {

    private MainContract.Presenter mPresenter;
    private LinearLayout mNavMain;
    private LinearLayout mNavSpend;
    private LinearLayout mNavSplit;
    private LinearLayout mNavQuick;
    private LinearLayout mNavChange;
    private LinearLayout mNavLogout;
    private TextView mToolTitle;
    private ImageView mBackgroundImage;
    private SharedPreferences mPreferences;
    private boolean mIsHalloween;
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserEmail;
    private FirebaseUser mFirebaseUser;
    private NavigationView mNavView;
    private View mDialogView;
    private Dialog mDialog;
    private long mExitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mPreferences = getSharedPreferences(Constants.THEME_COLOR, Context.MODE_PRIVATE);
        String theme = mPreferences.getString(Constants.THEME, Constants.DEFAULT_THEME);

        switch (theme) {
            case Constants.DEFAULT_THEME:
                getTheme().applyStyle(R.style.OverlayPrimaryColorDefault, true);
                mIsHalloween = false;
                break;
            case Constants.HALLOWEEN_THEME:
                getTheme().applyStyle(R.style.OverlayPrimaryColorHalloween, true);
                mIsHalloween = true;
                break;
        }

        setContentView(R.layout.activity_main);

        mNavView = findViewById(R.id.nav_view);
        mToolTitle = findViewById(R.id.toolbarTitleText);
        mNavMain = findViewById(R.id.nav_home);
        mNavSpend = findViewById(R.id.nav_spend);
        mNavSplit = findViewById(R.id.nav_split);
        mNavQuick = findViewById(R.id.nav_quick);
        mNavChange = findViewById(R.id.nav_change);
        mNavLogout = findViewById(R.id.nav_logout);
        mBackgroundImage = findViewById(R.id.app_background_image);
        //取得Header
        View headerView = mNavView.getHeaderView(0);
        // 取得Header中的東西
        mUserImage = headerView.findViewById(R.id.nav_user_image);
        mUserName = headerView.findViewById(R.id.nav_user_name);
        mUserEmail = headerView.findViewById(R.id.nav_user_email);


        if (mIsHalloween) {
            mBackgroundImage.setImageResource(R.drawable.halloween);
        } else {
            mBackgroundImage.setImageResource(R.drawable.gradient2);
        }


        mNavMain.setOnClickListener(this);
        mNavSpend.setOnClickListener(this);
        mNavSplit.setOnClickListener(this);
        mNavQuick.setOnClickListener(this);
        mNavChange.setOnClickListener(this);
        mNavLogout.setOnClickListener(this);
        if (mFirebaseUser.getPhotoUrl() != null) {
            Glide.with(this).load(mFirebaseUser.getPhotoUrl()).into(mUserImage);
        }
        mUserName.setText(mFirebaseUser.getDisplayName());
        mUserEmail.setText(mFirebaseUser.getEmail());


        mPresenter = new MainPresenter(this, getSupportFragmentManager());

        mPresenter.start();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //不顯示 application label
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //使 status bar 透明
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setFitsSystemWindows(true);
        drawer.setClipToPadding(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!getSupportFragmentManager().popBackStackImmediate()) {
                //最後一頁
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程式", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (v.getId()) {
            case R.id.nav_home:
                mPresenter.transToHome();
                break;
            case R.id.nav_spend:
                mPresenter.transToSpend();
                break;
            case R.id.nav_split:
                mPresenter.transToSplit(false);
                break;
            case R.id.nav_quick:
                mPresenter.transToQuickSplit();
                break;
            case R.id.nav_change:
                mPresenter.changeTheme();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public void setToolBarTitle(String title) {
        mToolTitle.setText(title);
    }

    @Override
    public void showNewTheme() {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (mIsHalloween) {
            editor.putString(Constants.THEME, Constants.DEFAULT_THEME);
        } else {
            editor.putString(Constants.THEME, Constants.HALLOWEEN_THEME);
        }
        editor.apply();
        recreate();
    }

    public void showFriendDetailPage(String friendName) {
        mPresenter.transToFriendDetailPage(friendName);
    }

    public void showGroupDetailPage(String groupId){
        mPresenter.transToGroupDetailPage(groupId);
    }

    public void showListDetailPage(Event event){
        mPresenter.transToListDetailPage(event);
    }

    public void showAddListPage() {
        mPresenter.transToAddListPage();
    }

    public void showAddGroupPage() {
        mPresenter.transToAddGroupPage();
    }

    public void switchPage(String pageName) {
        switch (pageName) {
            case Constants.GROUP:
                mPresenter.transToSplit(false);
                break;
            case Constants.FRIEND:
                mPresenter.transToSplit(true);
                break;
            case Constants.ADD_LIST:
                FirebaseFirestore.getInstance().collection("users").document(mFirebaseUser.getUid()).collection("friends").orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Friend> friends = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            friends.add(snapshot.toObject(Friend.class));
                        }
                        FriendList.getInstance().setFriendList(friends);
                        mPresenter.transToAddListPage();
                    }
                });
                break;
            case Constants.QUICK:
                mPresenter.transToQuickSplit();
                break;
        }
    }
}
