package com.ruby.splitmoney.group;

import android.app.AlertDialog;
import android.content.Context;
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

import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.adapters.GroupListAdapter;
import com.ruby.splitmoney.adapters.QuickSplitPartialAdapter;
import com.ruby.splitmoney.adapters.QuickSplitPercentAdapter;
import com.ruby.splitmoney.adapters.QuickSplitResultAdapter;
import com.ruby.splitmoney.objects.Group;

import java.util.List;


public class GroupFragment extends Fragment implements GroupContract.View, View.OnClickListener {

    private GroupContract.Presenter mPresenter;
    private GroupListAdapter mGroupListAdapter;
    private Context mContext;


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        mPresenter = new GroupPresenter(this, container.getContext());
        mPresenter.start();

        mContext = container.getContext();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.group_recycler_view);
        mGroupListAdapter = new GroupListAdapter(mPresenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mGroupListAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(GroupContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setGroupList(List<Group> groups) {
        mGroupListAdapter.setGroupList(groups);
    }

    @Override
    public void setGroupDetailPage(String groupId) {
        ((MainActivity) getActivity()).showGroupDetailPage(groupId);
    }
}
