package com.example.whats_up_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whats_up_app.Content.Categories;
import com.example.whats_up_app.R;


import java.util.ArrayList;

public class FragmentChatMainAdapter extends RecyclerView.Adapter<FragmentChatMainAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Categories> categoriesContents ;

    public FragmentChatMainAdapter() {
    }
    public FragmentChatMainAdapter(Context context, ArrayList<Categories> categoriesContents) {
        this.context = context;
        this.categoriesContents = categoriesContents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_chat_main, parent, false);
        return new FragmentChatMainAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(categoriesContents.get(position).getName());
        Glide.with(context).load(categoriesContents.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoriesContents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
