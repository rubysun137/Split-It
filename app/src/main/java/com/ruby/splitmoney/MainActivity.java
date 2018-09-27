package com.ruby.splitmoney;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.ruby.splitmoney.util.BaseActivity;

public class MainActivity extends BaseActivity implements MainContract.View,
        View.OnClickListener {

    private MainContract.Presenter mPresenter;
    private LinearLayout mNavSpend;
    private LinearLayout mNavSplit;
    private LinearLayout mNavQuick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavSpend = findViewById(R.id.nav_spend);
        mNavSplit = findViewById(R.id.nav_split);
        mNavQuick = findViewById(R.id.nav_quick);

        mNavSpend.setOnClickListener(this);
        mNavSplit.setOnClickListener(this);
        mNavQuick.setOnClickListener(this);


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
            case R.id.nav_spend:
                break;
            case R.id.nav_split:
                break;
            case R.id.nav_quick:
                break;
        }
    }
}
