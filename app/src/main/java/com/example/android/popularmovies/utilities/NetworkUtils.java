package com.example.android.popularmovies.utilities;

/**
 * Created by Lenovo on 2/4/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils extends Activity{

    final static String TAG = NetworkUtils.class.getSimpleName();
    final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final static String PRAM_API_KEY = "api_key";
    final static String apiKey = "";

    /**
     * Builds the URL used to query MovieDB.
     *
     * @param endPoint The keyword that will be queried for sorting.
     * @return The URL to use to query the MovieDB server.
     */
    public static URL buildUrl(String endPoint) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(endPoint)
                .appendQueryParameter(PRAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, url.toString());
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        BufferedReader bufferedReader = null;
        HttpURLConnection urlConnection = null;
        String jsonString = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            jsonString = stringBuffer.toString();
            return jsonString;
        } catch (IOException e) {
            Log.e(TAG, "Error", e);
            return null;
        } finally {
            urlConnection.disconnect();
            bufferedReader.close();
        }
    }

    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        } else {
            return false;
        }
    }
}