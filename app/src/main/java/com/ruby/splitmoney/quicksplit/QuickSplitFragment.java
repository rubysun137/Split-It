package com.ruby.splitmoney.quicksplit;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;


public class QuickSplitFragment extends Fragment {

    private Spinner mSpinner;


    public QuickSplitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_split, container, false);

        mSpinner = view.findViewById(R.id.split_type_spinner);
        ArrayAdapter<CharSequence> mSplitTypeList = ArrayAdapter.createFromResource(container.getContext(),R.array.splitType,R.layout.item_spinner);
        mSplitTypeList.setDropDownViewResource(R.layout.item_spinner);
        mSpinner.setAdapter(mSplitTypeList);

        return view;
    }

}
