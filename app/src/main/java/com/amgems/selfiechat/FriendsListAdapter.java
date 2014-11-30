package com.amgems.selfiechat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author Sherman Pay.
 * @version 0.1, 11/29/14.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<String> mFriendsList;

    public FriendsListAdapter(List<String> friendsList) {
       mFriendsList = friendsList;
    }

    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_adapter, parent, false);
        // Setup View here
        ViewHolder vh = new ViewHolder((TextView)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendsListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mFriendsList.get(i));
    }

    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        public ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }

        @Override
        public String toString() {
            return mTextView.toString();
        }
    }
}
