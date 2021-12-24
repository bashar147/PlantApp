package com.example.whats_up_app.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.whats_up_app.Fragments.Swipeable_videos;
import com.example.whats_up_app.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edmt.dev.videoplayer.VideoPlayerRecyclerView;
import edmt.dev.videoplayer.adapter.VideoPlayerRecyclerAdapter;
import edmt.dev.videoplayer.model.MediaObject;
import edmt.dev.videoplayer.utils.VerticalSpacingItemDecorator;


public class HomePage extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth auth;

    @BindView(R.id.videoPLayer)
    VideoPlayerRecyclerView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        videoPlayer = findViewById(R.id.videoPLayer);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        add_Nav();
        VideoRecyclerView();
    }

    private boolean onNavigationItemSelected(@NotNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_map:
                Toast.makeText(this, "Go To Maop", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                break;
            case R.id.swipeable_videos:
                Toast.makeText(this, "Go to SwipeAble_videos", Toast.LENGTH_SHORT).show();
                replaceFragment(new Swipeable_videos());
                break;
            case R.id.nav_logOut:
                logOut();
                Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                finish();
                break;
            case R.id.nav_chat:
                Toast.makeText(this, "Go To Chat", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),ChatMainActivity.class));

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void add_Nav(){

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"menu Selected ",Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }

    public void VideoRecyclerView(){
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        videoPlayer.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        videoPlayer.addItemDecoration(verticalSpacingItemDecorator);


        ArrayList<MediaObject> sourceCode = new ArrayList<>(sampleVideoList());
        videoPlayer.setMediaObjects(sourceCode);
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(sourceCode,initGlide());

        videoPlayer.setAdapter(adapter);
    }

    private @NotNull RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);
        return Glide.with(this).setDefaultRequestOptions(options);
    }

    private @NotNull List<MediaObject> sampleVideoList() {
        return Arrays.asList(
                new MediaObject(
                        "Big Buck Bunny",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://i.ytimg.com/vi/aqz-KE-bpKQ/maxresdefault.jpg",
                        "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org"),

                new MediaObject("ElephantsDream",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                "https://upload.wikimedia.org/wikipedia/commons/e/e4/Elephants_Dream_s5_proog.jpg",
                "The first Blender Open Movie from 2006"),

                new MediaObject("For Bigger Blazes",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                        "https://i.ytimg.com/vi/TKMj7eZ_rD0/maxresdefault.jpg",
                        "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes. For $35.\\nLearn how to use Chromecast with HBO GO and more at google.com/chromecast."),

                new MediaObject("For Bigger Escape",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images_480x270/ForBiggerEscapes.jpg",
                        "Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for when Batman's escapes aren't quite big enough. For $35. Learn how to use Chromecast with Google Play Movies and more at google.com/chromecast."));
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }

    public void logOut(){
    auth.signOut();
    }
}



