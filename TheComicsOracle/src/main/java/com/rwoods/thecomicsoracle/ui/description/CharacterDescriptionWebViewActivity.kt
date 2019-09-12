package com.rwoods.thecomicsoracle.ui.description

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.activity_character_description_web_view.*
import java.nio.charset.StandardCharsets


class CharacterDescriptionWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_description_web_view)

        webViewDescription.setPadding(0, 0, 0, 0)
        webViewDescription.setInitialScale(getScale())

        var characterDescription = ""

        val args = intent.extras

        args?.let {bundle ->
            bundle.getByteArray(Constants.CHARACTER)?.apply {
                characterDescription = String(this, StandardCharsets.UTF_8)
            }
        }

        webViewDescription.settings.loadWithOverviewMode = true
        webViewDescription.settings.useWideViewPort = true


        val mBuilder = StringBuilder(characterDescription.replace("<head>",
                "<head><meta name=\"viewport\" content=\"width=device-width,height=device-height,target-densityDpi=device-dpi,user-scalable=yes,initial-scale=0.5, maximum-scale=2, minimum-scale=0.5\" />"))

        webViewDescription.loadData(mBuilder.toString(), "text/html", "utf-8")
    }

    private fun getScale(): Int {
        val size = Point()
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)
        val width = size.x.toDouble()
        val height = size.y.toDouble()

        var `val`: Double? = width / 480
        `val` = `val`!! * 100.0
        return `val`.toInt()
    }
}
