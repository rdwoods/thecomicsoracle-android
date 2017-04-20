package com.rwoods.thecomicsoracle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.util.Constants;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videoView = (VideoView) findViewById(R.id.video_view);

        String videoUrl = "";

        Bundle args = getIntent().getExtras();

        if (args != null) {
            videoUrl = args.getString(Constants.VIDEO_URL);
        }

        videoView.setVideoPath(videoUrl);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);

        videoView.start();

    }
}
