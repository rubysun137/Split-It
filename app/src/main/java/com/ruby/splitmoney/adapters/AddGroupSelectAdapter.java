package com.ruby.splitmoney.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.addgroup.AddGroupContract;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.List;

public class AddGroupSelectAdapter extends RecyclerView.Adapter {

    private AddGroupContract.Presenter mPresenter;
    private FirebaseUser mUser;
    private Context mContext;
    private List<Friend> mFriends;

    public AddGroupSelectAdapter(AddGroupContract.Presenter presenter) {
        mPresenter = presenter;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setFriendList(List<Friend> friends) {
        mFriends = new ArrayList<>(friends);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_group_member, viewGroup, false);
        mContext = viewGroup.getContext();
        return new AddGroupSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((AddGroupSelectViewHolder) viewHolder).bindView();
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    private class AddGroupSelectViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMemberImage;
        private TextView mMemberName;
        private TextView mMemberEmail;
        private int mPosition;

        public AddGroupSelectViewHolder(@NonNull View itemView) {
            super(itemView);

            mMemberImage = itemView.findViewById(R.id.add_group_member_image);
            mMemberName = itemView.findViewById(R.id.add_group_member_name);
            mMemberEmail = itemView.findViewById(R.id.add_group_member_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.addToAddedFriendList(mFriends.get(getAdapterPosition()),getAdapterPosition());
                }
            });


        }

        private void bindView() {
            mPosition = getAdapterPosition();
            Friend friend = mFriends.get(mPosition);
            if (friend.getImage() != null) {
                Glide.with(mContext).load(Uri.parse(friend.getImage())).into(mMemberImage);
            } else {
                Glide.with(mContext).load(R.drawable.user2).into(mMemberImage);
            }
            mMemberName.setText(friend.getName());
            mMemberEmail.setText(friend.getEmail());

        }
    }
}
