package com.amgems.selfiechat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amgems.selfiechat.model.Friend;

import java.util.List;

/**
 * @author Sherman Pay.
 * @version 0.1, 11/29/14.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<Friend> mFriendsList;
    private Resources mResources;

    public FriendsListAdapter(List<Friend> friendsList, Resources res) {
        mFriendsList = friendsList;
        mResources = res;
    }

    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_adapter, parent, false);
        // Setup View here
        ViewHolder vh = new ViewHolder((CardView)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendsListAdapter.ViewHolder viewHolder, int i) {
        Friend f = mFriendsList.get(i);
        viewHolder.mFriendNameView.setText(f.getName());

        // Slow operation! Should be done off the UI thread. However, need to maintain valid state
        // while bitmap is being decoded.
        Bitmap src = BitmapFactory.decodeResource(mResources, f.getImageResId());
        RoundedBitmapDrawable roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(mResources, src);
        roundedBitmapDrawable.setCornerRadius(src.getWidth() / 2);
        roundedBitmapDrawable.setAntiAlias(true);
        viewHolder.mFriendImageView.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mFriendCard;
        TextView mFriendNameView;
        ImageView mFriendImageView;
        public ViewHolder(CardView itemView) {
            super(itemView);
            mFriendCard = itemView;
            mFriendNameView = (TextView)itemView.findViewById(R.id.friend_name);
            mFriendImageView = (ImageView)itemView.findViewById(R.id.friend_image);
        }

        @Override
        public String toString() {
            return mFriendCard.toString();
        }
    }
}
