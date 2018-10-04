package com.ruby.splitmoney.addlist;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruby.splitmoney.R;
import com.ruby.splitmoney.util.WrapLayout;

import java.util.Calendar;


public class AddListFragment extends Fragment implements AddListContract.View, View.OnClickListener {

    private AddListContract.Presenter mPresenter;
    private Context mContext;
    private TextView mPickDate;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;
    private WrapLayout mAddMemberLayout;
    private ImageView mAddMemberIcon;
    private ImageView mCalendarIcon;
    private int mLayoutIndex;


    public AddListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        mContext = container.getContext();
        mPresenter = new AddListPresenter(this);
        mPresenter.start();

        mLayoutIndex = 2;
        mAddMemberLayout = view.findViewById(R.id.add_list_member_linearlayout);
        mAddMemberIcon = view.findViewById(R.id.add_list_plus);

        mAddMemberIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout itemLayout = (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.item_member_name, mAddMemberLayout, false);
                TextView name = itemLayout.findViewById(R.id.add_member_user_name);
                String[] names = {"Ruby","Ryan","Happy"};
                name.setText(names[mAddMemberLayout.getChildCount() % 3]);
                        Log.d("TAG", "onClick: "+mLayoutIndex);
                itemLayout.setId(mLayoutIndex);

                itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Log.d("TAG", "onClick: "+v.getId());
//                        mAddMemberLayout.removeViewAt(v.getId());
                        mAddMemberLayout.removeView(v);
                    }
                });
                mAddMemberLayout.addView(itemLayout, mAddMemberLayout.getChildCount() - 1);

                mLayoutIndex++;

            }
        });


        mPickDate = view.findViewById(R.id.add_list_pick_day);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        String format = String.valueOf(mYear) + "/" + String.valueOf(mMonth + 1) + "/" + String.valueOf(mDay);
        mPickDate.setText(format);
        mPickDate.setOnClickListener(this);
        mCalendarIcon = view.findViewById(R.id.add_list_calendar_icon);
        mCalendarIcon.setOnClickListener(this);

        return view;
    }


    @Override
    public void setPresenter(AddListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_list_calendar_icon:
            case R.id.add_list_pick_day:
                mCalendar = Calendar.getInstance();
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(mContext, R.style.datePicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String format = String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth);
                        mPickDate.setText(format);
                    }
                }, mYear, mMonth, mDay).show();
                break;
            default:
                break;
        }
    }
}
