package com.rwoods.thecomicsoracle.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bluelinelabs.logansquare.LoganSquare;
import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionActivity;
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter;
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient;
import com.rwoods.thecomicsoracle.entity.ComicCharacter;
import com.rwoods.thecomicsoracle.entity.ComicCharacterResponse;
import com.rwoods.thecomicsoracle.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_NAME = "character";

    private ComicCharacterAdapter mComicCharacterAdapter;
    private String fragmentName;

    private RecyclerView mCharacterRecyclerView;

    private ArrayList<ComicCharacter> mComicCharacterList;
    private ArrayList<ComicCharacter> mSearchedComicCharacterList;

    private ProgressBar mProgressBar;
    private SearchView mCharacterSearchView;

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSearchFragment.class);

    private SharedPreferences.Editor editor;

    private SharedPreferences appSharedPrefs;

    private String savedSearchTerm;

    private boolean isResumed = false;

    private Context mContext;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MadSkilzByCharacterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterSearchFragment newInstance() {
        CharacterSearchFragment fragment = new CharacterSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CharacterSearchFragment() {
    }

    public String getFragmentName() {
        return FRAGMENT_NAME;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        appSharedPrefs = getActivity().getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);


        savedSearchTerm = appSharedPrefs.getString(getString(R.string.saved_character_search_term), "");

        if (getArguments() != null) {
            fragmentName = getArguments().getString(FRAGMENT_NAME);
        }

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mCharacterSearchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        if (mCharacterSearchView != null) {

            if (!savedSearchTerm.isEmpty()){
                mCharacterSearchView.setQuery(savedSearchTerm, false);
            }

            mCharacterSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            mCharacterSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String searchText) {

                    clearPreviousList();

                    getCharactersFromRest(searchText.trim());

                    mCharacterSearchView.clearFocus();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String searchText) {

                    if (searchText.trim().isEmpty()) {

                        clearPreviousList();

                        mComicCharacterAdapter = new ComicCharacterAdapter(getActivity(), mSearchedComicCharacterList);

                        setOnClickListener();
                    }

                    return false;
                }
            });

            mCharacterSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    clearPreviousList();

                    return false;
                }
            });
        }

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
        final View rootView = inflater.inflate(R.layout.fragment_character, container, false);

        //Your RecyclerView
        mCharacterRecyclerView = (RecyclerView) rootView.findViewById(R.id.character_recycler_view);
        mCharacterRecyclerView.setHasFixedSize(true);
        mCharacterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCharacterRecyclerView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.search_character_progress);

        mComicCharacterList = new ArrayList<>();
        mSearchedComicCharacterList = new ArrayList<>();

        restorePreviousSearchResults();

//        switch (NetworkUtil.getInstance().getConnectivityStatus(getActivity().getApplicationContext())) {
//            case NetworkUtil.TYPE_WIFI:
//                if (NetworkUtil.getInstance().isOnline(getActivity())) {
//                    getCharactersFromRest(rootView);
//                } else {
//                    getCharactersFromDb(rootView);
//                }
//                break;
//
//            case NetworkUtil.TYPE_MOBILE:
//                if (NetworkUtil.getInstance().isOnline(getActivity())) {
//                    getCharactersFromRest(rootView);
//                } else {
//                    getCharactersFromDb(rootView);
//                }
//                break;
//
//            case NetworkUtil.TYPE_NOT_CONNECTED:
//                getCharactersFromDb(rootView);
//                break;
//
//            default:
//                getCharactersFromDb(rootView);
//                break;
//        }

        return rootView;
    }

    private void restorePreviousSearchResults() {
        String savedData = appSharedPrefs.getString(getString(R.string.saved_character_results), "");

        try {
            if (!savedData.isEmpty()) {
                mComicCharacterList = (ArrayList<ComicCharacter>) LoganSquare.parseList(savedData, ComicCharacter.class);
                mComicCharacterAdapter = new ComicCharacterAdapter(getActivity(), mComicCharacterList);
                mCharacterRecyclerView.setAdapter(mComicCharacterAdapter);
                mComicCharacterAdapter.notifyDataSetChanged();
                mCharacterRecyclerView.setVisibility(View.VISIBLE);
                setOnClickListener();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCharactersFromRest(String searchText) {
        mCharacterRecyclerView.setVisibility(View.GONE);
        showProgress(true);

        String filteredCharacterName = "name:" + searchText;
        Call<ComicCharacterResponse> characterSearchCall = ComicsOracleRetrofitApiRestClient.getApiClient().getCharacterByName(filteredCharacterName);

        characterSearchCall.enqueue(new Callback<ComicCharacterResponse>() {

            @Override
            public void onResponse(Call<ComicCharacterResponse> call, Response<ComicCharacterResponse> response) {

                ArrayList<ComicCharacter> comicCharacters;

                comicCharacters = (ArrayList<ComicCharacter>) response.body().getComicCharacters();

                if (comicCharacters == null) {
                    //View rootView = getView().findViewById(R.id.character_recycler_view).getRootView();
                    Toast.makeText(getContext(), "Search of characters failed.", Toast.LENGTH_LONG).show();
                    return;
                }

                mComicCharacterAdapter = new ComicCharacterAdapter(getActivity(), comicCharacters);

                mCharacterRecyclerView.setAdapter(mComicCharacterAdapter);

                editor = appSharedPrefs.edit();

                editor.remove(getString(R.string.saved_character_search_term));
                editor.remove(getString(R.string.saved_character_results));
                editor.apply();

                try {
                    String savedSearchTerm = mCharacterSearchView.getQuery().toString();
                    String savedData = LoganSquare.serialize(mComicCharacterAdapter.getCharacterList(), ComicCharacter.class);
                    editor.putString(getString(R.string.saved_character_search_term), savedSearchTerm);
                    editor.putString(getString(R.string.saved_character_results), savedData);
                    editor.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                showProgress(false);
                mCharacterRecyclerView.setVisibility(View.VISIBLE);

                setOnClickListener();
            }

            @Override
            public void onFailure(Call<ComicCharacterResponse> call, Throwable throwable) {
                mCharacterRecyclerView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "Could not get character list", Toast.LENGTH_LONG).show();

                        showProgress(false);
                        mCharacterRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    private void clearPreviousList() {
        mComicCharacterList.clear();
    }


    private void setOnClickListener() {

        mComicCharacterAdapter.setOnItemClickListener(new ComicCharacterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Intent intent = new Intent(getActivity(), CharacterDescriptionWebViewActivity.class);
                Intent intent = new Intent(getActivity(), CharacterDescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.CHARACTER, mComicCharacterAdapter.getCharacterList().get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}