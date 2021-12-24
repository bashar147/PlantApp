package com.example.whats_up_app.Adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whats_up_app.Content.VideoItems;
import com.example.whats_up_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>{

    private List<VideoItems>videoItems;

    public ViewPager2Adapter(List<VideoItems> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_layout, parent, false);
        return new ViewPager2Adapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        VideoView videoView;
        TextView title , desc;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoView);
            title = itemView.findViewById(R.id.textVideoTitle);
            desc = itemView.findViewById(R.id.textVideoDescription);
            progressBar = itemView.findViewById(R.id.progressBar);

        }

        void setVideoData(@NotNull VideoItems videoItems){
            title.setText(videoItems.getVideoTitle());
            desc.setText(videoItems.getVideoDescription());
            videoView.setVideoPath(videoItems.getVideoUrl());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio/ screenRatio ;
                    if (scale >= 1f){
                        videoView.setScaleX(scale);
                    }else {
                        videoView.setScaleY(scale);
                    }
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }
}
