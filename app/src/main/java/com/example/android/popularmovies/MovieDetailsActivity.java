package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Lenovo on 2/6/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailsActivityFragment.MOVIE_DETAILS,
                    getIntent().getParcelableExtra(MovieDetailsActivityFragment.MOVIE_DETAILS));

            MovieDetailsActivityFragment fragment = new MovieDetailsActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail, fragment)
                    .commit();
        }
    }
}
