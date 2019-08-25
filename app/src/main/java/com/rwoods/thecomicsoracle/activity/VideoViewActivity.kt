package com.rwoods.thecomicsoracle.activity

import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.util.Constants

class VideoViewActivity : AppCompatActivity() {

    private var videoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val args = intent.extras

        videoView?.setVideoPath(args?.getString(Constants.VIDEO_URL))

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        videoView?.setMediaController(mediaController)

        videoView?.start()

    }
}
