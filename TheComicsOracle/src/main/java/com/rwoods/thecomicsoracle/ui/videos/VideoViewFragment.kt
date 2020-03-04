package com.rwoods.thecomicsoracle.ui.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rwoods.thecomicsoracle.R
import kotlinx.android.synthetic.main.fragment_video_view.*

class VideoViewFragment: Fragment() {

    private val args: VideoViewFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView.setVideoPath(args.videoUrl)

        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        videoView.setMediaController(mediaController)

        videoView.start()
    }
}
