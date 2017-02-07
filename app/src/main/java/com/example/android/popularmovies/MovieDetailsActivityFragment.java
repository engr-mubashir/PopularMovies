package com.example.android.popularmovies;

/**
 * Created by Lenovo on 2/6/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JSONUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MovieDetailsActivityFragment extends Fragment {

    private JSONUtils jsonUtils;
    static final String MOVIE_DETAILS = "MOVIE_DETAILS";
    private String imageUri = "http://image.tmdb.org/t/p/w342";

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            jsonUtils = arguments.getParcelable(MovieDetailsActivityFragment.MOVIE_DETAILS);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
        TextView titleView = (TextView) rootView.findViewById(R.id.detail_title);
        TextView overviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        TextView dateView = (TextView) rootView.findViewById(R.id.detail_date);
        TextView voteAverageView = (TextView) rootView.findViewById(R.id.detail_vote_average);

         if (jsonUtils != null){
             imageUri += jsonUtils.getImage2();
         }

        Picasso.with(getContext()).load(imageUri).into(imageView);

        titleView.setText(jsonUtils.getTitle());
        overviewView.setText(jsonUtils.getOverview());

        String movieDate = jsonUtils.getDate();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String date = DateUtils.formatDateTime(getActivity(), format.parse(movieDate).getTime(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
            dateView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        voteAverageView.setText(Double.toString(jsonUtils.getRating()));

        return rootView;
    }
}