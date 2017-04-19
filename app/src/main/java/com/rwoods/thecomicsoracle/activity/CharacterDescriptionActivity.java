package com.rwoods.thecomicsoracle.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.model.ComicCharacter;
import com.rwoods.thecomicsoracle.presenter.CharacterDescriptionPresenter;
import com.rwoods.thecomicsoracle.util.Constants;
import com.rwoods.thecomicsoracle.view.CharacterDescriptionActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class CharacterDescriptionActivity extends AppCompatActivity implements CharacterDescriptionActivityView {

    private TextView tvCharacterDesc;

    @BindView(R.id.btn_favorite) Button btnFavoriteCharacter;

    private ComicCharacter mComicCharacter;
    private boolean favoriteCharacter = false;

    private Realm realm;

    private CharacterDescriptionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_description_text_view);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        // Set up the toolbar and action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        presenter = new CharacterDescriptionPresenter(this);

        tvCharacterDesc = (TextView) findViewById(R.id.tv_description);
        //btnFavoriteCharacter = (Button) findViewById(R.id.btn_favorite);

        tvCharacterDesc.setMovementMethod(new ScrollingMovementMethod());

        realm = Realm.getDefaultInstance();

        String characterDescription;

        Bundle args = getIntent().getExtras();

        if (args != null) {
            mComicCharacter = args.getParcelable(Constants.CHARACTER);

            if (mComicCharacter != null) {
                favoriteCharacter = isFavoriteCharacter(mComicCharacter.getId());

                if (favoriteCharacter) {
                    btnFavoriteCharacter.setBackgroundResource(android.R.drawable.star_on);
                }

                characterDescription = mComicCharacter.getDescription();
                if (characterDescription != null) {

                    Spanned spanned = Html.fromHtml(characterDescription,
                            new Html.ImageGetter() {
                                @Override
                                public Drawable getDrawable(String source) {
                                    LevelListDrawable d = new LevelListDrawable();
                                    Drawable empty = ContextCompat.getDrawable(CharacterDescriptionActivity.this, R.drawable.page_overview_50px);

                                    d.addLevel(0, 0, empty);
                                    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                                    new LoadHtmlTask(CharacterDescriptionActivity.this, source, d).execute(new TextViewImageStore(tvCharacterDesc, empty.getIntrinsicWidth(), empty.getIntrinsicHeight()));

                                    return d;
                                }
                            }, null);
                    tvCharacterDesc.setText(spanned);
                }
            }
        }
    }

    @OnClick(R.id.btn_favorite)
    public void selectAsFavorite(){
        favoriteCharacter = isFavoriteCharacter(mComicCharacter.getId());
        presenter.addOrRemoveToFavorites(mComicCharacter, favoriteCharacter, btnFavoriteCharacter);
    }

    private boolean isFavoriteCharacter(Long id) {
        RealmResults<ComicCharacter> fcResults = realm.where(ComicCharacter.class)
                .equalTo("id", id)
                .findAll();

        return !fcResults.isEmpty();
    }

    private class TextViewImageStore {

        private TextView tv;
        int intrinsicWidth;
        int intrinsicHeight;

        public TextViewImageStore(TextView tv, int intrinsicWidth, int intrinsicHeight) {
            this.tv = tv;
            this.intrinsicWidth = intrinsicWidth;
            this.intrinsicHeight = intrinsicHeight;
        }

        public TextViewImageStore() {
        }

        public TextView getTv() {
            return tv;
        }

        public void setTv(TextView tv) {
            this.tv = tv;
        }

        public int getIntrinsicWidth() {
            return intrinsicWidth;
        }

        public void setIntrinsicWidth(int intrinsicWidth) {
            this.intrinsicWidth = intrinsicWidth;
        }

        public int getIntrinsicHeight() {
            return intrinsicHeight;
        }

        public void setIntrinsicHeight(int intrinsicHeight) {
            this.intrinsicHeight = intrinsicHeight;
        }
    }

    /*@Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("key", yourDataHere);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
*/
    private class LoadHtmlTask extends AsyncTask<TextViewImageStore, Void, Bitmap> {

        private LevelListDrawable levelListDrawable;
        private Context context;
        private String source;
        private TextView t;
        private int width;
        private int height;

        LoadHtmlTask(Context context, String source, LevelListDrawable levelListDrawable) {
            this.context = context;
            this.source = source;
            this.levelListDrawable = levelListDrawable;
        }

        @Override
        protected Bitmap doInBackground(TextViewImageStore... params) {

            t = params[0].getTv();
            width = params[0].getIntrinsicWidth();
            height = params[0].getIntrinsicHeight();
            try {

                return Glide
                        .with(context)
                        .load(source)
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.default_profile_avatar)
                        .dontTransform()
                        .into(480, 480) // Width and height
                        .get();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            try {
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);
                // Lets calculate the ratio according to the screen width in px
                int multiplier = size.x / bitmap.getWidth();
                //Log.d(LOG_CAT, "multiplier: " + multiplier);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, bitmap.getWidth() * multiplier, bitmap.getHeight() * multiplier);
                levelListDrawable.setLevel(1);
                t.setText(t.getText()); // invalidate() doesn't work correctly...


            } catch (Exception e) { /* Like a null bitmap, etc. */ }
        }
    }
}
