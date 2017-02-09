package com.example.android.popularmovies;

/**
 * Created by Lenovo on 2/4/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;


public class MainActivityFragment extends Fragment {
    private GridView gridView;
    private MovieDataAdapter movieDataAdapter;

    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String POPULARITY = "popular";
    private static final String RATING = "top_rated";
    private static final String MOVIES_KEY = "movies";
    private String sortBy = POPULARITY;
    private ArrayList<JSONUtils> jsonUtils = null;
    Context context = null;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // In order to handle menu events.
        setHasOptionsMenu(true);
        context = getContext();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);

        MenuItem action_popularity_sort = menu.findItem(R.id.action_popularity_sort);
        MenuItem action_rating_sort = menu.findItem(R.id.action_rating_sort);

        if (sortBy.contentEquals(POPULARITY)) {
            if (!action_popularity_sort.isChecked())
                action_popularity_sort.setChecked(true);
        }
        else {
            if (!action_rating_sort.isChecked())
                action_rating_sort.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_popularity_sort:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                sortBy = POPULARITY;
                updateMovies(sortBy);
                return true;
            case R.id.action_rating_sort:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                sortBy = RATING;
                updateMovies(sortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.movie_list);
        movieDataAdapter = new MovieDataAdapter(getActivity());

        gridView.setAdapter(movieDataAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONUtils jsonUtils = movieDataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class)
                        .putExtra(MovieDetailsActivityFragment.MOVIE_DETAILS, jsonUtils);

                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                sortBy = savedInstanceState.getString(SORT_SETTING_KEY);
            }
            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                jsonUtils = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                for (JSONUtils movie : jsonUtils) {
                    movieDataAdapter.add(movie);
                }
            } else {
                updateMovies(sortBy);
            }
        } else {
            updateMovies(sortBy);
        }
        return view;
    }

    private void updateMovies(String sortBy) {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(sortBy);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!sortBy.contentEquals(POPULARITY)) {
            outState.putString(SORT_SETTING_KEY, sortBy);
        }
        if (jsonUtils != null) {
            outState.putParcelableArrayList(MOVIES_KEY, jsonUtils);
        }
        super.onSaveInstanceState(outState);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<JSONUtils>> {
        private final String TAG = FetchMoviesTask.class.getSimpleName();

        private List<JSONUtils> getDataFromJSON(String jsonStr) throws JSONException {
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");
            List<JSONUtils> results = new ArrayList<>();

            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                JSONUtils movieModel = new JSONUtils(movie);
                results.add(movieModel);
            }
            return results;
        }

        @Override
        protected List<JSONUtils> doInBackground(String... params) {
            String jsonResponse = null;
            URL url = NetworkUtils.buildUrl(params[0]);

            try {
                if(NetworkUtils.isInternetAvailable(context)) {
                    jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    return getDataFromJSON(jsonResponse);
                }else{
                    Log.e(TAG, "Internet not available, try again!");
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<JSONUtils> movies) {
            if (movies != null) {
                if (movieDataAdapter != null) {
                    movieDataAdapter.clear();
                    for (JSONUtils movie : movies) {
                        movieDataAdapter.add(movie);
                    }
                }
                jsonUtils = new ArrayList<>();
                jsonUtils.addAll(movies);
            }
        }
    }
}
