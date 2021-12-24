package com.example.whats_up_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whats_up_app.Adapter.ViewPager2Adapter;
import com.example.whats_up_app.Content.VideoItems;
import com.example.whats_up_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class Swipeable_videos extends Fragment {


    public Swipeable_videos() {

    }




    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_swipeable_videos, container, false);
        final ViewPager2 pager2 =view.findViewById(R.id.viewPager2);

        List<VideoItems> videoItems = new ArrayList<>();

        VideoItems videoItemsCelebration = new VideoItems();
        videoItemsCelebration.setVideoUrl("android.resource://"+ getActivity().getPackageName() + "/" + R.raw.a);
        videoItemsCelebration.setVideoTitle("Cars show");
        videoItemsCelebration.setVideoDescription("beautiful cars");

        videoItems.add(videoItemsCelebration);

        VideoItems videoItemsParty = new VideoItems();
        videoItemsParty.setVideoUrl("android.resource://"+ getActivity().getPackageName() + "/" + R.raw.b);
        videoItemsParty.setVideoTitle("Cars show");
        videoItemsParty.setVideoDescription("beautiful cars");

        videoItems.add(videoItemsParty);

        VideoItems videoItemsExercise = new VideoItems();
        videoItemsExercise.setVideoUrl("android.resource://"+ getActivity().getPackageName() + "/" + R.raw.c);
        videoItemsExercise.setVideoTitle("Cars show");
        videoItemsExercise.setVideoDescription("beautiful cars");
        videoItems.add(videoItemsExercise);


        VideoItems car5 = new VideoItems();
        car5.setVideoUrl("android.resource://"+ getActivity().getPackageName() + "/" + R.raw.d);
        car5.setVideoTitle("Cars show");
        car5.setVideoDescription("beautiful cars");
        videoItems.add(car5);

        pager2.setAdapter(new ViewPager2Adapter(videoItems));

        return view;
    }


}