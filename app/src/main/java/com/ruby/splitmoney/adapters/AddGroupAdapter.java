package com.ruby.splitmoney.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.addgroup.AddGroupContract;
import com.ruby.splitmoney.objects.Friend;

import java.util.ArrayList;
import java.util.List;

public class AddGroupAdapter extends RecyclerView.Adapter {

    private AddGroupContract.Presenter mPresenter;
    private FirebaseUser mUser;
    private Context mContext;
    private List<Friend> mFriends;

    public AddGroupAdapter(AddGroupContract.Presenter presenter) {
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
        return new AddGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((AddGroupViewHolder) viewHolder).bindView();
    }

    @Override
    public int getItemCount() {
        return mFriends.size() + 1;
    }

    private class AddGroupViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMemberImage;
        private TextView mMemberName;
        private TextView mMemberEmail;
        private int mPosition;
        private Dialog mDialog;

        public AddGroupViewHolder(@NonNull View itemView) {
            super(itemView);

            mMemberImage = itemView.findViewById(R.id.add_group_member_image);
            mMemberName = itemView.findViewById(R.id.add_group_member_name);
            mMemberEmail = itemView.findViewById(R.id.add_group_member_email);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getAdapterPosition() != 0) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_group_delete_member, null, false);
                        TextView memberName = view.findViewById(R.id.add_group_delete_member_name);
                        memberName.setText(mFriends.get(getAdapterPosition() - 1).getName());
                        mDialog = new AlertDialog.Builder(mContext)
                                .setView(view)
                                .show();
                        mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.addToNotAddFriendList(mFriends.get(getAdapterPosition() - 1), getAdapterPosition() - 1);
                                mDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(mContext, R.string.can_not_delete_yourself, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

        }

        private void bindView() {
            mPosition = getAdapterPosition();
            if (mPosition == 0) {
                if (mUser.getPhotoUrl() != null) {
                    Glide.with(mContext).load(mUser.getPhotoUrl()).into(mMemberImage);
                } else {
                    Glide.with(mContext).load(R.drawable.user2).into(mMemberImage);
                }
                mMemberName.setText(mUser.getDisplayName());
                mMemberEmail.setText(mUser.getEmail());
            } else {
                Friend friend = mFriends.get(mPosition - 1);
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
}
