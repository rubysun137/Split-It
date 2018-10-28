package com.ruby.splitmoney.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.util.Constants;


public class HomeFragment extends Fragment implements HomeContract.View, View.OnClickListener {

    private HomeContract.Presenter mPresenter;
    private Button mGroupButton;
    private Button mFriendButton;
    private Button mListButton;
    private Button mQuickButton;
    private TextView mLentMoney;
    private TextView mBorrowedMoney;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mPresenter = new HomePresenter(this);
        mPresenter.start();

        mGroupButton = view.findViewById(R.id.home_group_button);
        mFriendButton = view.findViewById(R.id.home_friend_button);
        mListButton = view.findViewById(R.id.home_list_button);
        mQuickButton = view.findViewById(R.id.home_quick_button);
        mLentMoney = view.findViewById(R.id.total_lent_number);
        mBorrowedMoney = view.findViewById(R.id.total_borrowed_number);

        mGroupButton.setOnClickListener(this);
        mFriendButton.setOnClickListener(this);
        mListButton.setOnClickListener(this);
        mQuickButton.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_group_button:
                ((MainActivity) getActivity()).switchPage(Constants.GROUP_PAGE);
                break;
            case R.id.home_friend_button:
                ((MainActivity) getActivity()).switchPage(Constants.FRIEND_PAGE);
                break;
            case R.id.home_list_button:
                ((MainActivity) getActivity()).switchPage(Constants.ADD_LIST);
                break;
            case R.id.home_quick_button:
                ((MainActivity) getActivity()).switchPage(Constants.QUICK);
                break;
            default:
                break;
        }

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTotal(int lentMoney, int borrowedMoney) {
        mLentMoney.setText(String.valueOf(lentMoney));
        mBorrowedMoney.setText(String.valueOf(borrowedMoney));
    }
}
