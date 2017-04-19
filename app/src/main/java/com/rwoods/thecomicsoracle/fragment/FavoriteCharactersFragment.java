package com.rwoods.thecomicsoracle.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionActivity;
import com.rwoods.thecomicsoracle.activity.ComicsOracleMainActivity;
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter;
import com.rwoods.thecomicsoracle.model.ComicCharacter;
import com.rwoods.thecomicsoracle.util.Constants;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by rahmanwoods on 6/22/16.
 */
public class FavoriteCharactersFragment extends Fragment {

    private static final String FRAGMENT_NAME = "fav_character";
    private ComicCharacterAdapter mFavComicCharacterAdapter;

    private RecyclerView mFavCharacterRecyclerView;
    private ArrayList<ComicCharacter> mFavCharacterList;
    private String fragmentName;

    public static FavoriteCharactersFragment newInstance() {
        FavoriteCharactersFragment fragment = new FavoriteCharactersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FavoriteCharactersFragment() {
    }

    public String getFragmentName() {
        return FRAGMENT_NAME;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentName = getArguments().getString(FRAGMENT_NAME);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_fav_character, container, false);

        //Your RecyclerView
        mFavCharacterRecyclerView = (RecyclerView) rootView.findViewById(R.id.fav_character_recycler_view);
        mFavCharacterRecyclerView.setHasFixedSize(true);
        mFavCharacterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFavCharacterList = new ArrayList<>();


        getFavoritesFromDb();

        return rootView;
    }

    private void getFavoritesFromDb() {
        final RealmResults<ComicCharacter> fcResults = ((ComicsOracleMainActivity) getActivity()).getRealm().where(ComicCharacter.class).findAll();

        mFavCharacterList.clear();

        if (!fcResults.isEmpty()){

            for (ComicCharacter fc : fcResults){
                mFavCharacterList.add(fc);
            }

            mFavComicCharacterAdapter = new ComicCharacterAdapter(getActivity(), mFavCharacterList);

            mFavCharacterRecyclerView.setAdapter(mFavComicCharacterAdapter);

            setOnClickListener();
        }
    }


    private void clearPreviousList() {
        mFavCharacterList.clear();
    }


    private void setOnClickListener() {

        mFavComicCharacterAdapter.setOnItemClickListener(new ComicCharacterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Intent intent = new Intent(getActivity(), CharacterDescriptionWebViewActivity.class);
                Intent intent = new Intent(getActivity(), CharacterDescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.CHARACTER, mFavCharacterList.get(position));
                intent.putExtras(bundle); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
