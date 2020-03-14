package com.rwoods.thecomicsoracle.ui.description

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.fragment_character_description_web_view.*
import java.nio.charset.StandardCharsets


class CharacterDescriptionWebViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_description_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webViewDescription.setPadding(0, 0, 0, 0)

        getScale()?.run {
            webViewDescription.setInitialScale(this)
        }

        val characterDescription: String

        characterDescription = arguments?.getByteArray(Constants.CHARACTER)?.run {
            String(this, StandardCharsets.UTF_8)
        } ?: run {
            ""
        }

        webViewDescription.settings.loadWithOverviewMode = true
        webViewDescription.settings.useWideViewPort = true


        val mBuilder = StringBuilder(characterDescription.replace("<head>",
                "<head><meta name=\"viewport\" content=\"width=device-width,height=device-height,target-densityDpi=device-dpi,user-scalable=yes,initial-scale=0.5, maximum-scale=2, minimum-scale=0.5\" />"))

        webViewDescription.loadData(mBuilder.toString(), "text/html", "utf-8")
    }

    private fun getScale(): Int? {
        val size = Point()

        activity?.windowManager?.defaultDisplay?.getSize(size)
        val width = size.x.toDouble()
        val height = size.y.toDouble()

        var `val`: Double? = width / 480
        `val` = `val`?.times(100.0)
        return `val`?.toInt()
    }
}
