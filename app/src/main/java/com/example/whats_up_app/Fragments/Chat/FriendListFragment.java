package com.example.whats_up_app.Fragments.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.Options;
import com.example.whats_up_app.Activity.ChatMessageActivity;
import com.example.whats_up_app.Adapter.FragmentFriendAddRemoveAdapter;
import com.example.whats_up_app.Classes.RetrieveChatMessageClass;
import com.example.whats_up_app.Classes.RetriveFriendSearchClass;
import com.example.whats_up_app.Classes.UploadUserData;
import com.example.whats_up_app.R;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;


public class FriendListFragment extends Fragment {


    public FriendListFragment() {

    }

    private RecyclerView friendList_recycler;
    private DatabaseReference friendList_List;
    private DatabaseReference friendList_profileUrl;
    private String usrName;
    private FirebaseDatabase friendList_fireBaseDataBase;
    private  FirebaseRecyclerAdapter<RetriveFriendSearchClass,FriendListUserNodeHolder> adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        friendList_recycler = view.findViewById(R.id.friendListRecycler);
        friendList_recycler.hasFixedSize();
        friendList_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        friendList_fireBaseDataBase = FirebaseDatabase.getInstance();

        String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // init database Refs

        friendList_List = friendList_fireBaseDataBase.getReference("friend list");

        friendList_profileUrl = friendList_fireBaseDataBase.getReference("users");

        friendList_List.keepSynced(true);

         usrName = friendList_profileUrl.child(myUserId).child("fullName").toString();
        RetrieveFriendListNodes();
        friendList_List.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RetrieveFriendListNodes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Errot...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return view;

    }

    private void RetrieveFriendListNodes() {

        Query firebaseSearchQuery =  friendList_profileUrl.orderByChild("userName").
                startAt(usrName).endAt(usrName+"\uf8ff");

        FirebaseRecyclerOptions<RetriveFriendSearchClass> options = new FirebaseRecyclerOptions.Builder<RetriveFriendSearchClass>()
                .setQuery(firebaseSearchQuery, new SnapshotParser<RetriveFriendSearchClass>() {
                    @NonNull
                    @Override
                    public RetriveFriendSearchClass parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.println(Log.ASSERT,"FriendListFragmentOptio","I'm here from option");

                        return new
                                RetriveFriendSearchClass(snapshot.child("userName").getValue().toString()
                                ,snapshot.child("sentDate").getValue().toString());
                    }
                }).build();

            adapter = new FirebaseRecyclerAdapter<RetriveFriendSearchClass, FriendListUserNodeHolder>(options) {

            @SuppressLint("RecyclerView")
            @Override
            protected void onBindViewHolder(@NonNull FriendListUserNodeHolder holder, int pos_pos, @NonNull RetriveFriendSearchClass model) {
                 holder.SetUserName(model.getUserName());
                 holder.setSinceDate(model.getSentDate());

                 String positionKey = getRef(pos_pos).getKey();

                 friendList_profileUrl.child(positionKey).child("profilePicUrl").addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         String myProfilePicUrl = snapshot.getValue().toString();

                         if (!myProfilePicUrl.equals(null)){
                             holder.SetPicUrl(myProfilePicUrl);
                         }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {
                         Toast.makeText(getContext(), "Error\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });

                 holder.myView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         String posKey = getRef(pos_pos).getKey();
                         startActivity(new Intent(getActivity(), ChatMessageActivity.class).putExtra("userId",posKey));

                     }
                 });

              }

            @NonNull
            @Override
            public FriendListUserNodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friend_list_recycler, parent, false);
                return new FriendListFragment.FriendListUserNodeHolder(view);
            }
        };

        friendList_recycler.setAdapter(adapter);

    }

    public static class FriendListUserNodeHolder extends RecyclerView.ViewHolder{
        View myView;
        public FriendListUserNodeHolder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;
        }

        public void SetUserName(String userName){
            TextView myUserName = myView.findViewById(R.id.friendList_name);
            myUserName.setText(userName);
        }

        public void setSinceDate(String SinceDate){
            TextView MyS_DAte = myView.findViewById(R.id.friendList_since);
            MyS_DAte.setText(SinceDate);
        }
        public void SetPicUrl(String picUrl){
            ImageView muI_View = myView.findViewById(R.id.friendList_image);
            Picasso.get().load(picUrl).fit().centerInside().into(muI_View);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}