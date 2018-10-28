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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruby.splitmoney.R;
import com.ruby.splitmoney.objects.Event;
import com.ruby.splitmoney.objects.Friend;
import com.ruby.splitmoney.util.Constants;
import com.ruby.splitmoney.util.FriendList;

import java.util.ArrayList;
import java.util.List;

public class ListDetailAdapter extends RecyclerView.Adapter {

    private List<DocumentSnapshot> mSnapshots;
    private Context mContext;
    private FirebaseUser mUser;

    public ListDetailAdapter() {
        mSnapshots = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setSnapshots(List<DocumentSnapshot> snapshots) {
        mSnapshots = snapshots;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_detail, parent, false);
        return new ListDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ListDetailViewHolder) viewHolder).bindView();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    private class ListDetailViewHolder extends RecyclerView.ViewHolder {

        private TextView mDescription;
        private ImageView mUserImage;
        private int mPosition;

        public ListDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mDescription = itemView.findViewById(R.id.list_detail_description);
            mUserImage = itemView.findViewById(R.id.list_detail_user_image);
        }

        private void bindView() {
            mPosition = getAdapterPosition();
            String userId = mSnapshots.get(mPosition).getId();
            if (userId.equals(mUser.getUid()) && mUser.getPhotoUrl() != null) {
                Glide.with(mContext).load(mUser.getPhotoUrl()).into(mUserImage);
                setString(mUser.getDisplayName());
            } else {
                for (Friend friend : FriendList.getInstance().getFriendList()) {
                    if (friend.getUid().equals(userId)) {
                        if (friend.getImage() != null) {
                            Glide.with(mContext).load(Uri.parse(friend.getImage())).into(mUserImage);
                        }
                        setString(friend.getName());
                    }
                }
            }


        }

        private void setString(String name) {
            if (mSnapshots.get(mPosition).getDouble(Constants.PAY) != 0.0) {
                String description = mContext.getString(R.string.let) + " " + name + " " + mContext.getString(R.string.paying) + " " + mContext.getString(R.string.dollar_sign) + mSnapshots.get(mPosition).getDouble(Constants.PAY) + "\n" + mContext.getString(R.string.include_shared_money) + mSnapshots.get(mPosition).getDouble(Constants.OWE);
                mDescription.setText(description);
            } else {
                String description = name + " " + mContext.getString(R.string.should_share) + mSnapshots.get(mPosition).getDouble(Constants.OWE);
                mDescription.setText(description);
            }

        }
    }
}
