package com.example.whats_up_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whats_up_app.Content.FragmentFriendAddRemoveContent;
import com.example.whats_up_app.R;

import java.util.ArrayList;

public class FragmentFriendAddRemoveAdapter extends RecyclerView.Adapter<FragmentFriendAddRemoveAdapter.ViewHolder>{

    private Context context;
    private ArrayList<FragmentFriendAddRemoveContent> contents;

    public FragmentFriendAddRemoveAdapter(Context context, ArrayList<FragmentFriendAddRemoveContent> contents) {
        this.context = context;
        this.contents = contents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_add_remove, parent, false);
        return new FragmentFriendAddRemoveAdapter.ViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView pImage;
        private TextView textView , sent ;
        private Button accept , reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pImage = itemView.findViewById(R.id.friendAddRemove_ProfielImage);
            textView = itemView.findViewById(R.id.friendAddRemove_TextView);
            sent =  itemView.findViewById(R.id.friendADdRemove_SentOn);
            accept =  itemView.findViewById(R.id.friendAddRemove_AcceptButton);
            reject =  itemView.findViewById(R.id.friendAddRemove_rejectButton);

        }
    }
}
