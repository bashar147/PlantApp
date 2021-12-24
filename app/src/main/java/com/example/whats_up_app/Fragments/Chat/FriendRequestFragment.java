package com.example.whats_up_app.Fragments.Chat;

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

import com.example.whats_up_app.Classes.RetriveFriendSearchClass;
import com.example.whats_up_app.Classes.UploadUserData;
import com.example.whats_up_app.R;
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

import java.util.Calendar;

public class FriendRequestFragment extends Fragment {


    public FriendRequestFragment() {

    }


    private RecyclerView friendSearch_recycler;
    private EditText friendSearch_userName;
    private Button friendSearch_btn;
    private FirebaseDatabase friendSearch_database;
    private DatabaseReference mySearchRef,friendListRef,friendReqRef;
    private String positionKey;
    FirebaseRecyclerAdapter<UploadUserData, FriendRequestFragment.searchFriendViewHolder> firebaseRecyclerAdapter;
    private String nameUser="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);

        // RecyclerView Part
        friendSearch_recycler = view.findViewById(R.id.friendSearch_Recycler);
        friendSearch_recycler.hasFixedSize();
        friendSearch_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        //Initialize Component


        friendSearch_userName = view.findViewById(R.id.friendSearch_editText);
        friendSearch_btn = view.findViewById(R.id.friendSearch_Button);
        friendSearch_btn.setOnClickListener(this::searchClick);
        friendSearch_database = FirebaseDatabase.getInstance();

        // Initialize Database Ref
        mySearchRef = friendSearch_database.getReference("users");

        String userName = friendSearch_userName.getText().toString();
        retrieveUserSearched(userName);


        return view;

    }

    private void searchClick(View view){
        String userName = friendSearch_userName.getText().toString();
        retrieveUserSearched(userName);
    }

    private void retrieveUserSearched(String userName) {
        Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
        Query firebaseSearchQuery =  mySearchRef.orderByChild("fullName").
                startAt(userName).endAt(userName+"\uf8ff");



        FirebaseRecyclerOptions<UploadUserData> options = new FirebaseRecyclerOptions.Builder<UploadUserData>()
                .setQuery(firebaseSearchQuery, new SnapshotParser<UploadUserData>() {
                    @NonNull
                    @Override
                    public UploadUserData parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.println(Log.ASSERT,"FriendRequestFragment","I'm here from option");
                        Log.println(Log.ASSERT,"snapshot.childprofile_P",snapshot.child("profilePicUrl").getValue().toString());

                        return new
                         UploadUserData(snapshot.child("dateOfBarth").getValue().toString()
                        ,snapshot.child("fullName").getValue().toString()
                        ,snapshot.child("profilePicUrl").getValue().toString());
                    }
                }).build();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UploadUserData, FriendRequestFragment.searchFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendRequestFragment.searchFriendViewHolder holder, int position, @NonNull UploadUserData model) {
                Log.println(Log.ASSERT,"onBindViewHolder","I'm here");
                positionKey = getRef(position).getKey();

                friendReqRef = friendSearch_database.getReference("friend request")
                        .child(positionKey).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.println(Log.ASSERT,"Uid and posKey","Uid= "+Uid+" "+"PosKey= "+positionKey);
                if (!Uid.equals(positionKey)){
                    holder.UserName(model.getFullName());
                    holder.setProfilePic(model.getProfilePicUrl());
                    Log.println(Log.ASSERT,"!Uid.equals(positionKey","I'm here");

                    friendListRef = friendSearch_database.getReference("friend list")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(positionKey);

                    friendListRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                holder.setButtonText("invisible");
                            }else {
                                holder.setButtonText("visible");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Error\n"+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else {
                    Log.println(Log.ASSERT,"else","I'm here");

                    holder.myView.setVisibility(View.GONE);
                    holder.myView.setLayoutParams(new RecyclerView.LayoutParams(0,0));

                }

                Button addButton = holder.myView.findViewById(R.id.friendSearch_AddButton);
                friendReqRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            addButton.setText("Request Sent");
                            addButton.setEnabled(false);
                            Log.println(Log.ASSERT,"friendReqRef","I'm here");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error\n"+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                mySearchRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                UploadUserData userData = snapshot.getValue(UploadUserData.class);

                                nameUser = userData.getFullName();

                                Calendar calendar = Calendar.getInstance();

                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int month = calendar.get(Calendar.MONTH);
                                int year = calendar.get(Calendar.YEAR);

                                String dateUser = day +"/" + month + "/" + year;

                                Log.println(Log.ASSERT,"mySearchRef","I'm here");

                                RetriveFriendSearchClass sentDetails = new RetriveFriendSearchClass(nameUser,dateUser);

                                friendReqRef.setValue(sentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            addButton.setText("Request Sent");
                                            addButton.setEnabled(false);

                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Error\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            // 12 video here

                        });

                    }
                });

            }
            @NonNull
            @Override
            public FriendRequestFragment.searchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_friend_search_list, parent, false);
//                return new FriendRequestFragment.searchFriendViewHolder(inflate);

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_friend_search_list, parent, false);
                return new searchFriendViewHolder(view);
            }
        };

        friendSearch_recycler.setAdapter(firebaseRecyclerAdapter);

    }

    public static class searchFriendViewHolder extends RecyclerView.ViewHolder{
        View myView;


        public searchFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void UserName(String UserName){
            TextView searchFriend = myView.findViewById(R.id.friendSearch_TextView);
            searchFriend.setText(UserName);
        }
        public void setButtonText(@NonNull String Type){
            Button searchFriendBtn = myView.findViewById(R.id.friendSearch_Button);
            if (Type.equals("invisible")){
                //searchFriendBtn.setVisibility(View.INVISIBLE);
            }
        }

        public void setProfilePic(String imageUrl){
            ImageView profielPic = myView.findViewById(R.id.friendSearch_ProfileImage);
            Picasso.get().load(imageUrl)
                    .fit().centerCrop().into(profielPic);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}