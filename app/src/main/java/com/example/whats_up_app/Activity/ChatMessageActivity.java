package com.example.whats_up_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whats_up_app.Classes.RetrieveChatMessageClass;
import com.example.whats_up_app.Classes.SendUserChatReceieveClass;
import com.example.whats_up_app.Classes.UploadUserData;
import com.example.whats_up_app.Fragments.Chat.FriendRequestFragment;
import com.example.whats_up_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatMessageActivity extends AppCompatActivity {

    private RecyclerView userChat_recycler;
    private EditText userChat_msgBox;
    private ImageButton userChat_senBtn;
    private Calendar calendar;
    private String positionKeyFriend , positionKeyMsg , friendUserName ;



    private FirebaseDatabase userChat_firebaseDataBase;
    private DatabaseReference userChat_chatMSGREF,userChat_userNameRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        // get a back button on top
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get friends user id that will be passed from fragment
        positionKeyFriend = getIntent().getStringExtra("userId");

        userChat_recycler = findViewById(R.id.chatMessage_recyclerView);
        userChat_recycler.hasFixedSize();
        userChat_recycler.setLayoutManager(new LinearLayoutManager(this));

        userChat_msgBox = findViewById(R.id.chatMessage_msgBox);
        userChat_senBtn = findViewById(R.id.chatMessage_sendBox);

        calendar = Calendar.getInstance();

        // initialize database
        userChat_firebaseDataBase = FirebaseDatabase.getInstance();
        userChat_chatMSGREF = userChat_firebaseDataBase.getReference("chats")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(positionKeyFriend);

        // get the name reference of the user in the database
        // but lets check the firebase DB first

        userChat_userNameRef = userChat_firebaseDataBase.getReference("users");

        // now lets fetch the name from DB
        // make sure the id we are getting the first name from is of the other users and not ours

        userChat_userNameRef.child(positionKeyFriend)
                .child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                if (snapshot.exists()){
                  friendUserName = snapshot.getValue().toString();
                  // now lets set the activity title to the friends user name 
                    setTitle(friendUserName);
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

       userChat_senBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String userChatMsg = userChat_msgBox.getText().toString();
               if (userChatMsg.equals("")){
                   Toast.makeText(getApplicationContext(), "Message is empty", Toast.LENGTH_SHORT).show();
               }else {
                   // now lets get our user name
                   userChat_userNameRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                           .child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String myUserName ;
                            myUserName = snapshot.getValue().toString();

                            // now lets get the current item

                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                            String time = dateFormat.format(calendar.getTime());

                            String message = userChat_msgBox.getText().toString();
                            SendUser(message,time,myUserName);

                          }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
           }
       });

       // retrieve chat by Ref
        userChat_chatMSGREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                RetrieveChatMessage();
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage()+"\n"+error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //now lets do something with the backButton that we Created

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                // we will just kill this activity when the back pressed
                finish();
                return true;
       }

        return super.onOptionsItemSelected(item);
    }

    // this method will be user to send chat message over database
    private void SendUser(String message, String time, String myUserName) {
            userChat_msgBox.setText("");

            DatabaseReference myLastMsgRef = userChat_firebaseDataBase.getReference("lastMessage")
                    .child(positionKeyFriend).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            DatabaseReference myOwnMsgRef = userChat_firebaseDataBase.getReference("LastMessage")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(positionKeyFriend);

            DatabaseReference friendReceievieMsgRef = userChat_firebaseDataBase.getReference("chatMessage")
                    .child(positionKeyFriend).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            DatabaseReference myReceieveMsgRef = userChat_firebaseDataBase.getReference("chatMessage")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(positionKeyFriend);

            // Update my last msg of friend
        RetrieveChatMessageClass lastMsg = new RetrieveChatMessageClass(myUserName,message,time);

        myLastMsgRef.setValue(lastMsg).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Message could not be delivered", Toast.LENGTH_SHORT).show();
            }
        });

        RetrieveChatMessageClass myOwnMsg = new RetrieveChatMessageClass(friendUserName,message,time);
        myOwnMsgRef.setValue(myOwnMsg).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Message could not be sent", Toast.LENGTH_SHORT).show();
            }
        });
        SendUserChatReceieveClass friendMsg  = new SendUserChatReceieveClass(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),message,time);

        friendReceievieMsgRef.push().setValue(friendMsg).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error..."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        SendUserChatReceieveClass myClassMsg = new SendUserChatReceieveClass(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),message,time);
        myReceieveMsgRef.push().setValue(myClassMsg).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error..."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void RetrieveChatMessage(){
        Query firebaseSearchQuery =  userChat_chatMSGREF.orderByChild("userId");

        FirebaseRecyclerOptions<SendUserChatReceieveClass> options = new FirebaseRecyclerOptions.Builder<SendUserChatReceieveClass>()
                .setQuery(firebaseSearchQuery, new SnapshotParser<SendUserChatReceieveClass>() {
                    @NonNull
                    @Override
                    public SendUserChatReceieveClass parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.println(Log.ASSERT,"Options","I'm here from option");

                        return new
                                SendUserChatReceieveClass(snapshot.child("userId").getValue().toString(),
                                snapshot.child("message").getValue().toString()
                        ,snapshot.child("time").getValue().toString());
                    }
                }).build();

        final FirebaseRecyclerAdapter<SendUserChatReceieveClass,UserChatViewHolder> adapter = new FirebaseRecyclerAdapter<SendUserChatReceieveClass, UserChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserChatViewHolder holder, int position, @NonNull SendUserChatReceieveClass model) {
                positionKeyMsg = model.getUserId();

                if(positionKeyMsg.equals(positionKeyFriend)){
                    holder.ChangerOrientationLeft();
                    holder.LastMessage(model.getMessage());
                    holder.UserTime(model.getTime());
                }
                else {
                    holder.ChangerOrientationRight();
                    holder.LastMessage(model.getMessage());
                    holder.UserTime(model.getTime());
                }

            }

            @NonNull
            @Override
            public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sentchat_cardview, parent, false);
                return new ChatMessageActivity.UserChatViewHolder(view);
            }
        };
    }
    public static class UserChatViewHolder extends RecyclerView.ViewHolder{
        View myview;
        public UserChatViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }

        public void LastMessage(String Message)
        {
            TextView sentChat_message = myview.findViewById(R.id.sentChat_message);
            sentChat_message.setText(Message);
        }

        public void UserTime(String Time){
            TextView sentChat_Time = myview.findViewById(R.id.sentChat_Time);
            sentChat_Time.setText(Time);
        }

        public void ChangerOrientationLeft()
        {
            LinearLayout cardView = myview.findViewById(R.id.sentChat_Layout);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT
           ,FrameLayout.LayoutParams.WRAP_CONTENT);

           params.setMarginEnd(100);
           cardView.setLayoutParams(params);
            cardView.setBackground(ContextCompat.getDrawable(myview.getContext(),R.drawable.layout_bg_white));
        }

        public void ChangerOrientationRight()
        {
            LinearLayout cardView = myview.findViewById(R.id.sentChat_Layout);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT
                    ,FrameLayout.LayoutParams.WRAP_CONTENT);

            params.setMarginStart(100);
            cardView.setLayoutParams(params);
            cardView.setBackground(ContextCompat.getDrawable(myview.getContext(),R.drawable.layout_bg_green));
            // nice way to change background
        }
    }
}
