package com.ruby.splitmoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruby.splitmoney.util.BaseActivity;
import com.ruby.splitmoney.util.Constants;

public class MainActivity extends BaseActivity implements MainContract.View,
        View.OnClickListener {

    private MainContract.Presenter mPresenter;
    private LinearLayout mNavMain;
    private LinearLayout mNavSpend;
    private LinearLayout mNavSplit;
    private LinearLayout mNavQuick;
    private LinearLayout mNavChange;
    private TextView mToolTitle;
    private ImageView mBackgroundImage;
    private SharedPreferences mPreferences;
    private boolean mIsHalloween;
    private TextView mUserName;
    private TextView mUserEmail;
    private FirebaseUser mFirebaseUser;
    private NavigationView mNavView;


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
        mBackgroundImage = findViewById(R.id.app_background_image);
        //取得Header
        View headerView = mNavView.getHeaderView(0);
        // 取得Header中的東西
        mUserName = headerView.findViewById(R.id.nav_user_name);
        mUserEmail = headerView.findViewById(R.id.nav_user_email);


        if (mIsHalloween) {
            mBackgroundImage.setImageResource(R.drawable.halloween);
        } else {
            mBackgroundImage.setImageResource(R.drawable.gradient);
        }


        mNavMain.setOnClickListener(this);
        mNavSpend.setOnClickListener(this);
        mNavSplit.setOnClickListener(this);
        mNavQuick.setOnClickListener(this);
        mNavChange.setOnClickListener(this);
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
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.remove(getSupportFragmentManager().findFragmentByTag(Constants.ADD_LIST));
//            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
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
                mPresenter.transToSplit();
                break;
            case R.id.nav_quick:
                mPresenter.transToQuickSplit();
                break;
            case R.id.nav_change:
                mPresenter.changeTheme();
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

    public void showAddListPage() {
        mPresenter.transToAddListPage();
    }
}
