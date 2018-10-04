package com.ruby.splitmoney.quicksplit;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.QuickSplitPartialAdapter;
import com.ruby.splitmoney.adapters.QuickSplitResultAdapter;
import com.ruby.splitmoney.adapters.QuickSplitPercentAdapter;

import java.util.List;


public class QuickSplitFragment extends Fragment implements QuickSplitContract.View, View.OnClickListener {

    private QuickSplitContract.Presenter mPresenter;

    private Spinner mSpinner;
    private String[] mSplitType;
    private String mSellectSplitType;
    private EditText mTotalMoney;
    private EditText mTotalMember;
    private CheckBox mSelfCheck;
    private EditText mFeePercent;
    private TextView mNextPage;
    private TextView mPrePage;
    private ConstraintLayout mKeyInPage;
    private ConstraintLayout mResultPage;
    private View mDialogView;
    private AlertDialog mDialogPartial;
    private QuickSplitPartialAdapter mPartialAdapter;
    private QuickSplitResultAdapter mPartialResultAdapter;
    private QuickSplitPercentAdapter mPercentAdapter;
    private String mMoney;
    private String mMember;
    private boolean mIsNotEqual;
    private TextView mEqualNumber;
    private LinearLayout mEqualPage;
    private LinearLayout mUnequalPage;
    private LinearLayout mPercentPage;
    private boolean mIsPercent;


    public QuickSplitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_split, container, false);

        mIsNotEqual = false;
        mIsPercent = false;

        mPresenter = new QuickSplitPresenter(this);

        mKeyInPage = view.findViewById(R.id.quick_split_first_page);
        mResultPage = view.findViewById(R.id.quick_split_second_page);

        mPresenter.start();

        mSpinner = view.findViewById(R.id.split_type_spinner);
        mSellectSplitType = "";
        mTotalMoney = view.findViewById(R.id.quick_split_money_edit_text);
        mTotalMember = view.findViewById(R.id.people_edit_text);
        mSelfCheck = view.findViewById(R.id.self_check_box);
        mFeePercent = view.findViewById(R.id.fee_edit_text);
        mNextPage = view.findViewById(R.id.next_page_text_view);
        mPrePage = view.findViewById(R.id.previous_page_text_view);
        mEqualNumber = view.findViewById(R.id.equal_split_number);
        mEqualPage = view.findViewById(R.id.quick_equal_split);
        mEqualPage.setVisibility(View.GONE);
        mUnequalPage = view.findViewById(R.id.quick_unequal_split);
        mUnequalPage.setVisibility(View.GONE);
        mPercentPage = view.findViewById(R.id.quick_percent_split);
        mPercentPage.setVisibility(View.GONE);
        mSplitType = new String[]{"全部均分", "部份均分", "比例分攤"};
        ArrayAdapter<String> mSplitTypeList = new ArrayAdapter<>(container.getContext(), R.layout.item_spinner, mSplitType);
        mSplitTypeList.setDropDownViewResource(R.layout.item_spinner);
        mSpinner.setAdapter(mSplitTypeList);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.selectSplitType(position);
                switch (position) {
                    default:
                    case 0:
                        break;
                    case 1:
                        mMoney = mTotalMoney.getText().toString();
                        mMember = mTotalMember.getText().toString();

                        if (!mMoney.equals("") && !mMember.equals("") && !mMoney.equals("0") && !mMember.equals("0")) {
                            mDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_partial, null);

                            RecyclerView recyclerView = mDialogView.findViewById(R.id.dialog_recycler_view);
                            mPartialAdapter = new QuickSplitPartialAdapter(mMoney, mMember, mPresenter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(mPartialAdapter);

                            mDialogPartial = new AlertDialog.Builder(getContext())
                                    .setView(mDialogView)
                                    .setCancelable(false)
                                    .show();
                            mDialogPartial.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


                            TextView correct = mDialogView.findViewById(R.id.dialog_correct_text);
                            TextView cancel = mDialogView.findViewById(R.id.dialog_cancel_text);

                            correct.setOnClickListener(QuickSplitFragment.this);
                            cancel.setOnClickListener(QuickSplitFragment.this);
                        } else {
                            mSpinner.setSelection(0);
                            Toast.makeText(getContext(), "金額與人數不可為 0 ", Toast.LENGTH_LONG).show();
                            mPresenter.selectSplitType(0);
                        }
                        break;
                    case 2:
                        mMoney = mTotalMoney.getText().toString();
                        mMember = mTotalMember.getText().toString();

                        if (!mMoney.equals("") && !mMember.equals("") && !mMoney.equals("0") && !mMember.equals("0")) {
                            mDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_partial, null);

                            RecyclerView recyclerView = mDialogView.findViewById(R.id.dialog_recycler_view);
                            mPercentAdapter = new QuickSplitPercentAdapter(mMoney, mMember, mPresenter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(mPercentAdapter);

                            mDialogPartial = new AlertDialog.Builder(getContext())
                                    .setView(mDialogView)
                                    .setCancelable(false)
                                    .show();
                            mDialogPartial.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


                            TextView correct = mDialogView.findViewById(R.id.dialog_correct_text);
                            TextView cancel = mDialogView.findViewById(R.id.dialog_cancel_text);

                            correct.setOnClickListener(QuickSplitFragment.this);
                            cancel.setOnClickListener(QuickSplitFragment.this);
                            mIsPercent = true;
                        } else {
                            mSpinner.setSelection(0);
                            Toast.makeText(getContext(), "金額與人數不可為 0 ", Toast.LENGTH_LONG).show();
                            mPresenter.selectSplitType(0);
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPresenter.selectSplitType(0);
            }
        });

        mTotalMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mIsNotEqual) {
                    mIsNotEqual = false;
                    mSpinner.setSelection(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNextPage.setOnClickListener(this);
        mPrePage.setOnClickListener(this);
    }

    @Override
    public void setPresenter(QuickSplitContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showFirstPage() {
        mKeyInPage.setVisibility(View.VISIBLE);
        mResultPage.setVisibility(View.GONE);
    }

    @Override
    public void showSecondPage() {

        mKeyInPage.setVisibility(View.GONE);
        mResultPage.setVisibility(View.VISIBLE);
        mEqualPage.setVisibility(View.GONE);
        mUnequalPage.setVisibility(View.GONE);
        mPercentPage.setVisibility(View.GONE);

    }

    @Override
    public void showEqualResult(double money) {
        mEqualPage.setVisibility(View.VISIBLE);
        mEqualNumber.setText(String.valueOf(money));
    }

    @Override
    public void showUnequalResult(List<Double> results) {
        mUnequalPage.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = mUnequalPage.findViewById(R.id.unequal_result_recycler_view);
        mPartialResultAdapter = new QuickSplitResultAdapter(results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mPartialResultAdapter);

    }

    @Override
    public void showSharedResult(List<Double> results) {
        mPercentPage.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = mPercentPage.findViewById(R.id.percent_result_recycler_view);
        mPartialResultAdapter = new QuickSplitResultAdapter(results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mPartialResultAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_correct_text:
                if (mIsPercent) {
                    if (mPresenter.isSharedListEmpty()) {
                        Toast.makeText(getContext(), "須至少一人分攤帳務", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                mDialogPartial.dismiss();
                mIsNotEqual = true;
                mIsPercent = false;
                break;
            case R.id.dialog_cancel_text:
                mDialogPartial.dismiss();
                mSpinner.setSelection(0);
                mIsPercent = false;
                break;
            case R.id.next_page_text_view:
                int money = parseInt(mTotalMoney.getText().toString());
                int member = parseInt(mTotalMember.getText().toString());
                int percent = parseInt(mFeePercent.getText().toString());
                mPresenter.passValue(money, member, percent);
                mPresenter.toSecondPage();
                break;
            case R.id.previous_page_text_view:
                mPresenter.toFirstPage();
            default:
                break;
        }
    }

    private int parseInt(String s) {
        if (s.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
