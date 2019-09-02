/*
package com.rwoods.thecomicsoracle.activity

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LevelListDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.util.Constants
import com.rwoods.thecomicsoracle.view.CharacterDescriptionActivityView
import com.squareup.moshi.Moshi
import java.io.IOException

class CharacterDescriptionActivity : AppCompatActivity(), CharacterDescriptionActivityView {

    private var tvCharacterDesc: TextView? = null

    @BindView(R.id.btn_favorite) @JvmField var btnFavoriteCharacter: Button? = null

    private var mComicCharacter: ComicCharacter? = null
    private var favoriteCharacter = false

    private var presenter: CharacterDescriptionPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_description_text_view)
        ButterKnife.setDebug(true)
        ButterKnife.bind(this)


        // Set up the toolbar and action bar.
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        if (toolbar != null) {
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }

        presenter = CharacterDescriptionPresenter(this)

        tvCharacterDesc = findViewById(R.id.tv_description) as TextView
        //btnFavoriteCharacter = (Button) findViewById(R.id.btn_favorite);

        tvCharacterDesc!!.movementMethod = ScrollingMovementMethod()


        val args = intent.extras

        if (args != null) {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<ComicCharacter>(ComicCharacter::class.java).lenient()

            val json = args.getString(Constants.CHARACTER)
            if (json != null) {
                try {
                    mComicCharacter = jsonAdapter.fromJson(json)
                    displayCharacter(mComicCharacter)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private fun displayCharacter(comicCharacter: ComicCharacter?) {
        if (comicCharacter != null) {
            val characterDescription: String? = this.mComicCharacter!!.description
            favoriteCharacter = isFavoriteCharacter(this.mComicCharacter!!.id)

            if (favoriteCharacter) {
                btnFavoriteCharacter!!.setBackgroundResource(android.R.drawable.star_on)
            }

            if (characterDescription != null) {

                val spanned = Html.fromHtml(characterDescription, Html.FROM_HTML_MODE_COMPACT,
                        Html.ImageGetter { source ->
                            val d = LevelListDrawable()
                            val empty = ContextCompat.getDrawable(this@CharacterDescriptionActivity, R.drawable.page_overview_50px)

                            d.addLevel(0, 0, empty)
                            empty?.intrinsicWidth?.let { d.setBounds(0, 0, it, empty.intrinsicHeight) }
                            LoadHtmlTask(this@CharacterDescriptionActivity, source, d).execute(TextViewImageStore(tvCharacterDesc, empty!!.intrinsicWidth, empty.intrinsicHeight))

                            d
                        }, null)
                tvCharacterDesc!!.text = spanned
            }
        }
    }

    @OnClick(R.id.btn_favorite)
    fun selectAsFavorite() {
        favoriteCharacter = isFavoriteCharacter(mComicCharacter!!.id)
        presenter!!.addOrRemoveToFavorites(mComicCharacter as ComicCharacter, favoriteCharacter, btnFavoriteCharacter as Button)
    }

    private fun isFavoriteCharacter(id: Long?): Boolean {
        */
/*val fcResults = realm!!.where<ComicCharacter>(ComicCharacter::class.java)
                .equalTo("id", id)
                .findAll()

        return !fcResults.isEmpty()*//*

        return true
    }

    private inner class TextViewImageStore {

        var tv: TextView? = null
        var intrinsicWidth: Int = 0
        var intrinsicHeight: Int = 0

        constructor(tv: TextView?, intrinsicWidth: Int, intrinsicHeight: Int) {
            this.tv = tv
            this.intrinsicWidth = intrinsicWidth
            this.intrinsicHeight = intrinsicHeight
        }
    }

    */
/*@Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("key", yourDataHere);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
*//*

    private inner class LoadHtmlTask internal constructor(private val context: Context, private val source: String, private val levelListDrawable: LevelListDrawable) : AsyncTask<TextViewImageStore, Void, Bitmap>() {
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


            } catch (e: Exception) { */
/* Like a null bitmap, etc. *//*

            }
        }
    }
}
*/
