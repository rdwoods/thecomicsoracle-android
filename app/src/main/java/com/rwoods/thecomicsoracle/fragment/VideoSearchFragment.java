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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.activity.ComicsOracleMainActivity;
import com.rwoods.thecomicsoracle.activity.VideoViewActivity;
import com.rwoods.thecomicsoracle.adapter.VideoAdapter;
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient;
import com.rwoods.thecomicsoracle.model.Video;
import com.rwoods.thecomicsoracle.model.VideoResponse;
import com.rwoods.thecomicsoracle.util.Constants;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_NAME = "video";

    private VideoAdapter mVideoAdapter;
    private String fragmentName;

    private RecyclerView mVideoRecyclerView;

    private ArrayList<Video> mVideoList;
    private ArrayList<Video> mSearchedVideoList;

    private ProgressBar mProgressBar;
    private SearchView mVideoSearchView;

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSearchFragment.class);

    private SharedPreferences.Editor editor;

    private SharedPreferences appSharedPrefs;

    private String savedSearchTerm;
    private JsonAdapter<List<Video>> jsonAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MadSkilzByCharacterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoSearchFragment newInstance() {
        VideoSearchFragment fragment = new VideoSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public VideoSearchFragment() {
    }

    public String getFragmentName() {
        return FRAGMENT_NAME;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Video.class);
        jsonAdapter = moshi.adapter(type);

        appSharedPrefs
                = getActivity().getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);


        savedSearchTerm = appSharedPrefs.getString(getString(R.string.saved_video_search_term), "");

        if (getArguments() != null) {
            fragmentName = getArguments().getString(FRAGMENT_NAME);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mVideoSearchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        //}
        if (mVideoSearchView != null) {

            if (!savedSearchTerm.isEmpty()){
                mVideoSearchView.setQuery(savedSearchTerm, false);
            }

            mVideoSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            mVideoSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String searchText) {

                    clearPreviousList();

                    getVideosFromRest(searchText);

                    mVideoSearchView.clearFocus();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String searchText) {

                    if (searchText.isEmpty()) {

                        clearPreviousList();

                        mVideoAdapter = new VideoAdapter(getActivity(), mSearchedVideoList);

                        setOnClickListener();
                    }

                    return false;
                }
            });

            mVideoSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    clearPreviousList();

                    setOnClickListener();

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
        final View rootView = inflater.inflate(R.layout.fragment_videos, container, false);

        //Your RecyclerView
        mVideoRecyclerView = (RecyclerView) rootView.findViewById(R.id.video_recycler_view);
        mVideoRecyclerView.setHasFixedSize(true);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVideoRecyclerView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.search_video_progress);

        mVideoList = new ArrayList<>();
        mSearchedVideoList = new ArrayList<>();

        restorePreviousSearchResults();

        return rootView;
    }

    private void restorePreviousSearchResults() {
        String savedData = appSharedPrefs.getString(getActivity().getString(R.string.saved_video_results), "");

        try {
            if (!savedData.isEmpty()) {
                mVideoList = (ArrayList<Video>) jsonAdapter.fromJson(savedData);
                mVideoAdapter = new VideoAdapter(getActivity(), mVideoList);
                mVideoRecyclerView.setAdapter(mVideoAdapter);
                mVideoAdapter.notifyDataSetChanged();
                mVideoRecyclerView.setVisibility(View.VISIBLE);
                setOnClickListener();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getVideosFromRest(String searchText) {
        mVideoRecyclerView.setVisibility(View.GONE);
        showProgress(true);

        String filteredVideo = "name:" + searchText;
        Call<VideoResponse> videoResponseCall = ComicsOracleRetrofitApiRestClient.getApiClient().getVideoByName(filteredVideo);

        videoResponseCall.enqueue(new Callback<VideoResponse>() {

            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {

                ArrayList<Video> videos;

                videos = (ArrayList<Video>) response.body().getVideos();

                if (videos == null){
                    //View rootView = getView().findViewById(R.id.character_recycler_view).getRootView();
                    Toast.makeText(getContext(), "Search of video failed.", Toast.LENGTH_LONG).show();
                    return;
                }

                mVideoAdapter = new VideoAdapter(getActivity(), videos);

                mVideoRecyclerView.setAdapter(mVideoAdapter);

                editor = ((ComicsOracleMainActivity) getActivity()).getSharedPrefs().edit();
                editor.remove(getString(R.string.saved_video_search_term));
                editor.remove(getString(R.string.saved_video_results));
                editor.apply();

                try {
                    String savedSearchTerm = mVideoSearchView.getQuery().toString();
                    String savedData = jsonAdapter.toJson(mVideoList);
                    editor.putString(getString(R.string.saved_video_search_term), savedSearchTerm);
                    editor.putString(getString(R.string.saved_video_results), savedData);
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                showProgress(false);
                mVideoRecyclerView.setVisibility(View.VISIBLE);

                setOnClickListener();
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable throwable) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "Video List Failed.", Toast.LENGTH_LONG).show();

                        showProgress(false);
                        mVideoRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    private void clearPreviousList() {
        mVideoList.clear();
    }


    private void setOnClickListener(){

        mVideoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Intent intent = new Intent(getActivity(), CharacterDescriptionWebViewActivity.class);
                Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.VIDEO_URL, mVideoAdapter.getVideoList().get(position).getHighUrl());
                intent.putExtras(bundle); //Put your id to your next Intent

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
}