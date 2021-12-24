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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whats_up_app.Activity.ChatMessageActivity;
import com.example.whats_up_app.Classes.RetrieveChatMessageClass;
import com.example.whats_up_app.Classes.RetriveFriendSearchClass;
import com.example.whats_up_app.Classes.SendUserChatReceieveClass;
import com.example.whats_up_app.Classes.UploadUserData;
import com.example.whats_up_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ChatMainFragment extends Fragment {

    public ChatMainFragment(){

    }

    private RecyclerView chatMain_recycler;
    private DatabaseReference chatMain_chats;
    private DatabaseReference chatMain_profilePicUrl;
    private String positionKey, myName, profileImage, date;
    private FirebaseDatabase chatMain_FireBaseDatabase;
    private  FirebaseRecyclerAdapter<RetrieveChatMessageClass,ChatMainNodeHolder> adapter;
    private  String myUserId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view =  inflater.inflate(R.layout.fragment_chat_main, container, false);

            chatMain_recycler = view.findViewById(R.id.chatMain_recycler);
            chatMain_recycler.hasFixedSize();
            chatMain_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

            chatMain_FireBaseDatabase = FirebaseDatabase.getInstance();

            myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            chatMain_chats = chatMain_FireBaseDatabase.getReference("lastMessage").child(myUserId);

            chatMain_profilePicUrl = chatMain_FireBaseDatabase.getReference("users");

        chatMain_profilePicUrl.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.println(Log.ASSERT,"chatrMain","from name profile date");
                        UploadUserData getName = snapshot.getValue(UploadUserData.class);
                        myName = getName.getFullName();
                        profileImage = getName.getProfilePicUrl();
                        date = getName.getDateOfBarth();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error...\n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RetriveLastMessage();
            chatMain_profilePicUrl.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.println(Log.ASSERT,"chatrMain","from RetriveLastMessage()");
                    RetriveLastMessage();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            return view;
    }

    private void RetriveLastMessage() {

//        Query firebaseSearchQuery =  chatMain_profilePicUrl.orderByChild("userId");
//
//        FirebaseRecyclerOptions<RetrieveChatMessageClass> options = new FirebaseRecyclerOptions.Builder<RetrieveChatMessageClass>()
//                .setQuery(firebaseSearchQuery, new SnapshotParser<RetrieveChatMessageClass>() {
//                    @NonNull
//                    @Override
//                    public RetrieveChatMessageClass parseSnapshot(@NonNull DataSnapshot snapshot) {
//                        Log.println(Log.ASSERT,"ChatMainFragmentOptions","I'm here from option");
//
//                        return new
//                                RetrieveChatMessageClass(snapshot.child("userId").getValue().toString(),
//                                snapshot.child("message").getValue().toString()
//                                ,snapshot.child("time").getValue().toString());
//                    }
//                }).build();

       Log.println(Log.ASSERT,"chatrMain","inside the RetriveLastMessage()");

        Query firebaseSearchQuery = chatMain_profilePicUrl.child(myUserId).orderByChild("fullName").
                startAt(myName).endAt(myName + "\uf8ff");

        FirebaseRecyclerOptions<RetrieveChatMessageClass> options = new FirebaseRecyclerOptions.Builder<RetrieveChatMessageClass>()
                .setQuery(firebaseSearchQuery, new SnapshotParser<RetrieveChatMessageClass>() {
                    @NonNull
                    @Override
                    public RetrieveChatMessageClass parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.println(Log.ASSERT,"ChatMainFragmentOptions","I'm here from option");

                        return new
                                RetrieveChatMessageClass(snapshot.child("fullName").getValue().toString()
                                ,snapshot.child("profilePicUrl").getValue().toString(), snapshot.child("dateOfBarth").getValue().toString());
                    }
                }).build();


        adapter = new FirebaseRecyclerAdapter<RetrieveChatMessageClass, ChatMainNodeHolder>(options) {
            @SuppressLint("RecyclerView")
            @Override
            protected void onBindViewHolder(@NonNull ChatMainNodeHolder holder, int position, @NonNull RetrieveChatMessageClass model) {
                // set the views for card view

                holder.SetMyUName(model.getFrom());
                holder.SetMsg(model.getMessage());

                String positionKey = getRef(position).getKey();

                chatMain_profilePicUrl.child(positionKey).child("profilePicUrl")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String myProfileUrl = snapshot.getValue().toString();
                                if (!myProfileUrl.equals(null)){
                                    holder.SetPUrl(myProfileUrl);
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
                        String posKey = getRef(position).getKey();
                        startActivity(new Intent(getActivity(), ChatMessageActivity.class).putExtra("userId",posKey));

                    }
                });
            }

            @NonNull
            @Override
            public ChatMainNodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };

        chatMain_recycler.setAdapter(adapter);

    }

    public static class ChatMainNodeHolder extends RecyclerView.ViewHolder{
        View myView;
        public ChatMainNodeHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void SetMyUName(String uName){
            TextView uNameT = myView.findViewById(R.id.chatMain_userName);
            uNameT.setText(uName);
        }
        public void SetMsg(String Msg){
            TextView uMsg = myView.findViewById(R.id.chatMain_message);
            uMsg.setText(Msg);
        }
        public void SetPUrl(String Url){
            ImageView iView = myView.findViewById(R.id.chatMain_userImage);
            Picasso.get().load(Url).fit().centerInside().into(iView);

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