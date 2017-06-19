package com.rwoods.thecomicsoracle.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import android.widget.VideoView

import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.util.Constants

class VideoViewActivity : AppCompatActivity() {

    private var videoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        videoView = findViewById(R.id.video_view) as VideoView

        var videoUrl = ""

        val args = intent.extras

        if (args != null) {
            videoUrl = args.getString(Constants.VIDEO_URL)
        }

        videoView!!.setVideoPath(videoUrl)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        videoView!!.setMediaController(mediaController)

        videoView!!.start()

    }
}
