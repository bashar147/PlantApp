package com.example.whats_up_app.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.whats_up_app.Fragments.Chat.ChatMainFragment;
import com.example.whats_up_app.Fragments.Chat.FriendAddRemoveFragment;
import com.example.whats_up_app.Fragments.Chat.FriendListFragment;
import com.example.whats_up_app.Fragments.Chat.FriendRequestFragment;
import com.example.whats_up_app.R;
import com.example.whats_up_app.databinding.ActivityChatMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatMainActivity extends AppCompatActivity {


    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        navView = findViewById(R.id.navigation_view);
        navView.setOnItemSelectedListener(this::navClick);

        ChatMainFragment chatMainFragment = new ChatMainFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout,chatMainFragment).commit();


    }

    private boolean navClick(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.navigation_chat:
                ChatMainFragment chatMainFragment = new ChatMainFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frameLayout,chatMainFragment).commit();
                break;

            case R.id.navigation_friend:
//                FriendListFragment friendListFragment = new FriendListFragment();
                FriendAddRemoveFragment friendAddRemoveFragment = new FriendAddRemoveFragment();
                FragmentManager manager1 = getSupportFragmentManager();
                manager1.beginTransaction().replace(R.id.frameLayout,friendAddRemoveFragment).commit();
                break;


            case R.id.navigation_message:
                FriendListFragment friendListFragment = new FriendListFragment();
                FragmentManager manager2 = getSupportFragmentManager();
                manager2.beginTransaction().replace(R.id.frameLayout,friendListFragment).commit();
                break;

            case R.id.navigation_request:
                FriendRequestFragment friendRequest = new FriendRequestFragment();
                FragmentManager manager3 = getSupportFragmentManager();
                manager3.beginTransaction().replace(R.id.frameLayout,friendRequest).commit();
                break;
        }
        return true;
    }

}