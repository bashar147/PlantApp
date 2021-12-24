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


public class FriendAddRemoveFragment extends Fragment {

    private RecyclerView friendAddRemoveRecycler;
    private FirebaseDatabase friendAddRemoveFireBaseDataBase;
    private DatabaseReference friendAddRemove_dataRef, friendAddRemove_profilePic;
    private String positionKey, myName, profileImage, date;
    private  FirebaseRecyclerAdapter<RetriveFriendSearchClass, FriendAddRemoveNodeViewHolder> adapter;


    public FriendAddRemoveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_add_remove, container, false);

        // Cast all components
        friendAddRemoveRecycler = view.findViewById(R.id.friendAddRemove_Recycler);
        friendAddRemoveRecycler.hasFixedSize();
        friendAddRemoveRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        friendAddRemoveFireBaseDataBase = FirebaseDatabase.getInstance();

        String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // create database reference
        friendAddRemove_dataRef = friendAddRemoveFireBaseDataBase.getReference("friend request")
                .child(myUserId);

        friendAddRemove_profilePic = friendAddRemoveFireBaseDataBase.getReference("users");

        friendAddRemove_profilePic.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

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

        RetrieveFriendRequest();

        friendAddRemove_dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error...\n" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }

    private void RetrieveFriendRequest() {

        Query firebaseSearchQuery = friendAddRemove_profilePic.orderByChild("fullName").
                startAt(myName).endAt(myName + "\uf8ff");

        FirebaseRecyclerOptions<RetriveFriendSearchClass> options = new FirebaseRecyclerOptions.Builder<RetriveFriendSearchClass>()
                .setQuery(firebaseSearchQuery, new SnapshotParser<RetriveFriendSearchClass>() {
                    @NonNull
                    @Override
                    public RetriveFriendSearchClass parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.println(Log.ASSERT, "ADD_REMOVE_Options", "I'm here from option");
                        //Log.println(Log.ASSERT, "snapshot.childprofile_P", snapshot.child("profilePicUrl").getValue().toString());

                        return new
                                RetriveFriendSearchClass(snapshot.child("fullName").getValue().toString()
                                , snapshot.child("dateOfBarth").getValue().toString());
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<RetriveFriendSearchClass, FriendAddRemoveNodeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendAddRemoveNodeViewHolder holder, int position, @NonNull RetriveFriendSearchClass model) {

                holder.UserName(model.getUserName());
                holder.sentDate(model.getSentDate());

                positionKey = getRef(position).getKey();

                // check Profile Picture

                friendAddRemove_profilePic.child(positionKey).child("profilePicUrl")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                profileImage = snapshot.getValue().toString();

                                if (!profileImage.equals(null)) {
                                    holder.profilePic(profileImage);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Error...\n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                Button acceptButton = holder.myView.findViewById(R.id.friendAddRemove_AcceptButton);
                Button rejectButton = holder.myView.findViewById(R.id.friendAddRemove_rejectButton);

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // set friendList dataBase Ref
                        DatabaseReference myFriendList = friendAddRemoveFireBaseDataBase.getReference("friend list")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(positionKey);

                        // set friends database ref
                        DatabaseReference myFriendListFriend = friendAddRemoveFireBaseDataBase.getReference("friend list")
                                .child(positionKey).
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        Calendar calendar = Calendar.getInstance();

                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        month++;
                        int year = calendar.get(Calendar.YEAR);


                        String data = day + "/" + month + "/" + year;
                        String RequestersName = model.getUserName();

                        RetriveFriendSearchClass setFriendDBS = new RetriveFriendSearchClass(
                                RequestersName, data);

                        myFriendList.setValue(setFriendDBS)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Friend Added Successfully", Toast.LENGTH_LONG).show();
                                            RetriveFriendSearchClass setMyName = new RetriveFriendSearchClass(myName, data);
                                            myFriendListFriend.setValue(setMyName);
                                        } else {
                                            Toast.makeText(getContext(), "Error Adding Friend...\n", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

                rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendAddRemove_dataRef.child(positionKey).setValue(null);
                    }
                });
            }

            @NonNull
            @Override
            public FriendAddRemoveNodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_add_remove, parent, false);
                return new FriendAddRemoveFragment.FriendAddRemoveNodeViewHolder(view);

            }
        };

        friendAddRemoveRecycler.setAdapter(adapter);

    }

    public static class FriendAddRemoveNodeViewHolder extends RecyclerView.ViewHolder {
        View myView;

        public FriendAddRemoveNodeViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void UserName(String userName) {
            TextView friendAddRemoveName = myView.findViewById(R.id.friendAddRemove_TextView);
            friendAddRemoveName.setText(userName);
        }

        public void sentDate(String date) {
            TextView friendAddRemoveDate = myView.findViewById(R.id.friendADdRemove_SentOn);
            friendAddRemoveDate.setText("Sent On..." + date);
        }

        public void profilePic(String imageUrl) {
            ImageView friendAddRemoveProfilePic = myView.findViewById(R.id.friendAddRemove_ProfielImage);
            Picasso.get().load(imageUrl).fit().centerCrop().into(friendAddRemoveProfilePic);

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
