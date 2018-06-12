package com.roque.app.waylla_app.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roque.app.waylla_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosPostFragment extends Fragment {

    public FavoritosPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favoritos_post, container, false);
    }

}
