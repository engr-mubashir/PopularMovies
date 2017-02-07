package com.example.android.popularmovies;

/**
 * Created by Lenovo on 2/4/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JSONUtils;
import com.squareup.picasso.Picasso;

public class MovieDataAdapter extends ArrayAdapter<JSONUtils> {

    public MovieDataAdapter(Context context) {

        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String imageUri = "http://image.tmdb.org/t/p/w185";
        JSONUtils jsonUtils = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_detail, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_image);
        TextView titleView = (TextView) convertView.findViewById(R.id.item_text);

        if(jsonUtils != null){
            imageUri += jsonUtils.getImage();
        }

        Picasso.with(getContext()).load(imageUri).into(imageView);
        titleView.setText(jsonUtils.getTitle());

        return convertView;

    }
}