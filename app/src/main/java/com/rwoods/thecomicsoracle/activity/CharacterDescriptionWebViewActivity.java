package com.rwoods.thecomicsoracle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.util.Constants;

public class CharacterDescriptionWebViewActivity extends AppCompatActivity {

    private WebView wvCharacterDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_description_web_view);

        wvCharacterDesc = (WebView) findViewById(R.id.wv_description);

        String characterDescription = "";

        Bundle args = getIntent().getExtras();

        if (args != null) {
            characterDescription = args.getString(Constants.CHARACTER_DESC);
        }

        wvCharacterDesc.getSettings().setLoadWithOverviewMode(true);
        wvCharacterDesc.getSettings().setUseWideViewPort(true);

        wvCharacterDesc.loadData(characterDescription, "text/html", "utf-8");

    }
}
