package com.rwoods.thecomicsoracle.ui.description

import android.annotation.TargetApi
import android.graphics.Point
import android.graphics.drawable.LevelListDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.ui.ComicsOracleMainViewModel
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.fragment_description_text_view.*
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class CharacterDescriptionFragment : Fragment() {

    private val viewModel:ComicsOracleMainViewModel by activityViewModels()

    private var character: ComicCharacter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_description_text_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.descriptionLiveData.observe(viewLifecycleOwner, Observer { textViewImageStore ->
            progressBar.visibility = GONE
        })

        displayCharacter()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun displayCharacter() {

        val characterDescription = arguments?.getByteArray(Constants.CHARACTER)?.run {
            String(this, StandardCharsets.UTF_8)
        } ?: run {
            ""
        }


        characterDescription.run {
            val spanned = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT,
                    Html.ImageGetter { source ->
                        val levelListDrawable = LevelListDrawable()
                        val empty = context?.let { ContextCompat.getDrawable(it, R.drawable.page_overview_50px) }

                        levelListDrawable.addLevel(0, 0, empty)
                        empty?.intrinsicWidth?.run { levelListDrawable.setBounds(0, 0, this, empty.intrinsicHeight) }

                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.processHtml(TextViewImageStore(description, empty?.intrinsicWidth
                                    ?: 0, empty?.intrinsicHeight ?: 0), source, Point(), levelListDrawable)
                        }

                        levelListDrawable
                    }, null)
            description.text = spanned
        }
    }


    /*private inner class LoadHtmlTask internal constructor(private val context: Context, private val source: String, private val levelListDrawable: LevelListDrawable) : AsyncTask<TextViewImageStore, Void, Bitmap>() {
        private var t: TextView? = null
        private var width: Int = 0
        private var height: Int = 0
        lateinit private var bmp: Bitmap

        override fun doInBackground(vararg params: TextViewImageStore): Bitmap? {

            t = params[0].tv
            width = params[0].intrinsicWidth
            height = params[0].intrinsicHeight

            bmp = Glide
                    .with(context)
                    .asBitmap()
                    .load(R.drawable.default_profile_avatar)
                    .apply(RequestOptions()
                            .centerCrop()
                            .dontTransform()
                    )
                    .into(480, 480) // Width and height
                    .get()

            try {
                bmp = Glide
                        .with(context)
                        .asBitmap()
                        .load(source)
                        .apply(RequestOptions()
                                .centerCrop()
                                .dontTransform()
                                .error(R.drawable.default_profile_avatar))
                        .into(480, 480) // Width and height
                        .get()

            } catch (e: Exception) {
            }

            return bmp
        }

        override fun onPostExecute(bitmap: Bitmap) {
            try {
                val d = BitmapDrawable(resources, bitmap)
                val size = Point()
                windowManager.defaultDisplay.getSize(size)
                // Lets calculate the ratio according to the screen width in px
                val multiplier = size.x / bitmap.width
                //Log.d(LOG_CAT, "multiplier: " + multiplier);
                levelListDrawable.addLevel(1, 1, d)
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, bitmap.width * multiplier, bitmap.height * multiplier)
                levelListDrawable.level = 1
                t!!.text = t!!.text // invalidate() doesn't work correctly...


            } catch (e: Exception) {

            }
        }
    }*/
}
