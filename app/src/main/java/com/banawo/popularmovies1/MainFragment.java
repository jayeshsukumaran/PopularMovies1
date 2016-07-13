package com.banawo.popularmovies1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import com.banawo.popularmovies1.MovieData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    DownloadMovieListTask.MovieArrayAdapter<MovieData> myAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            startActivity(new Intent(getActivity(),SettingsActivity.class));
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);;

        // Inflate the layout for this fragment

        return rootView;
    }


    @Override
    public void onResume() {

        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.sort_key),getString(R.string.sort_default));


        if(isOnline()) {
            new DownloadMovieListTask().execute(sortOrder, null);
        }



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class DownloadMovieListTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            // Clear all movie data before downloading new set of data
            MovieData mData = MovieData.GetInstance();
            mData.clearMovieData();
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            MovieData mData = MovieData.GetInstance();

            myAdapter = new MovieArrayAdapter<MovieData>(getActivity(),
                    R.id.list_item_movie_imageview, mData.getElementList());

            GridView gridView = (GridView) getActivity().findViewById(R.id.gridview_movies);
            gridView.setAdapter(myAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(getActivity(), DetailActivity.class);
                    in.putExtra("MOVIE_POSITION", position);
                    startActivity(in);

                }
            });

        }

        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                String apiKey = getString(R.string.MOVIE_DB_API_KEY);
                Uri.Builder newURI = new Uri.Builder();
                newURI.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(params[0])
                        .appendQueryParameter("api_key",apiKey);

                String queryURL = newURI.build().toString();
                Log.i("Jayesh", queryURL);
                URL url = new URL(queryURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();;

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJsonStr = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();

            }  catch (IOException ex) {
                movieJsonStr = null;
                ex.printStackTrace();
            }

            finally  {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                       e.printStackTrace();
                    }
                }
            }

            try {
                 getMovieDataFromJson(movieJsonStr.toString());
            }catch (JSONException ex) {

                ex.getStackTrace();
            };

            return null;

        }

        private void getMovieDataFromJson(String movieStr)  throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MOV_RESULT_LIST = "results";


            JSONObject movieJsonObj = new JSONObject(movieStr);
            JSONArray  movieJsonArr = movieJsonObj.getJSONArray(MOV_RESULT_LIST);

            int len = movieJsonArr.length();

            MovieData mData = MovieData.GetInstance();


            for(int i=0;i<len;i++) {
                JSONObject movie = movieJsonArr.getJSONObject(i);
                String movieName = movie.getString("original_title");
                String moviePosterLink = movie.getString("poster_path");
                String moviePosterPath = new Uri.Builder().scheme("http")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w342")
                        .build().toString() + moviePosterLink;
                String movieBigPosterPath = new Uri.Builder().scheme("http")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w500")
                        .build().toString() + moviePosterLink;
                String movieSynopsis =  movie.getString("overview");
                String movieUsrRating =  movie.getString("vote_average");
                String movieRelDate = movie.getString("release_date");

                mData.CreateMovie(movieName,moviePosterPath,movieBigPosterPath,movieSynopsis,movieUsrRating,movieRelDate);


            }
            return;
        }

        public  class MovieArrayAdapter<MovieData> extends ArrayAdapter{

            Context c;
            public MovieArrayAdapter(Activity context, int layout, List<MovieData> data) {

                super(context,layout,data);
                c = context;
            }

            public View getView(int position,View ConvertView,ViewGroup parent)
            {
                com.banawo.popularmovies1.MovieData mData = com.banawo.popularmovies1.MovieData.GetInstance();
                LayoutInflater inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View gridView;
                if(ConvertView == null) {
                    gridView = new View(c);

                } else {
                    gridView = ConvertView;
                }
                gridView = inflater.inflate(R.layout.list_item_movie,null);
                ImageView imageView = (ImageView) gridView.findViewById(R.id.list_item_movie_imageview);
                Picasso.with(getContext()).load(mData.getElementAtIndex(position).getMoviePoster()).into(imageView);
                return gridView;
            }

        }



    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }



}
