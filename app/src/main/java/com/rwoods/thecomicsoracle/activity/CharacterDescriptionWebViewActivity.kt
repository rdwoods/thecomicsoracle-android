package com.rwoods.thecomicsoracle.activity

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.webkit.WebView
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.util.Constants
import java.nio.charset.StandardCharsets


class CharacterDescriptionWebViewActivity : AppCompatActivity() {

    private var wvCharacterDesc: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_description_web_view)

        wvCharacterDesc = findViewById(R.id.wv_description) as WebView
        wvCharacterDesc!!.setPadding(0, 0, 0, 0)
        wvCharacterDesc!!.setInitialScale(getScale())

        var characterDescription = ""

        val args = intent.extras

        if (args != null) {
            characterDescription = String(args.getByteArray(Constants.CHARACTER), StandardCharsets.UTF_8)
        }

        wvCharacterDesc!!.settings.loadWithOverviewMode = true
        wvCharacterDesc!!.settings.useWideViewPort = true


        val mBuilder = StringBuilder(characterDescription.replace("<head>",
                "<head><meta name=\"viewport\" content=\"width=device-width,height=device-height,target-densityDpi=device-dpi,user-scalable=yes,initial-scale=0.5, maximum-scale=2, minimum-scale=0.5\" />"))

        wvCharacterDesc!!.loadData(mBuilder.toString(), "text/html", "utf-8")
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
