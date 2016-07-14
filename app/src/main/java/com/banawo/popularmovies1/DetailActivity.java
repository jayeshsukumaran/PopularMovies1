package com.banawo.popularmovies1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportFragmentManager().beginTransaction().replace(R.id.detail,new DetailFragment()).commit();
    }

    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //data to be saved
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //retore data
    }

}
