package com.example.whats_up_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whats_up_app.Content.Categories;
import com.example.whats_up_app.R;

import java.util.ArrayList;

public class FragmentFriendListAdapter extends RecyclerView.Adapter<FragmentFriendListAdapter.ViewHolder>{


    private Context context;
    private ArrayList<Categories> contents;

    public FragmentFriendListAdapter() {

    }

    public FragmentFriendListAdapter(Context context, ArrayList<Categories> categoriesContents) {
        this.context = context;
        this.contents = categoriesContents;
    }

    @NonNull
    @Override
    public FragmentFriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_chat_main, parent, false);
        return new FragmentFriendListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
