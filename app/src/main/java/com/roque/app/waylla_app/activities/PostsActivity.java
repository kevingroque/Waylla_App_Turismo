package com.roque.app.waylla_app.activities;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.fragments.AllPostFragment;
import com.roque.app.waylla_app.fragments.FavoritosPostFragment;
import com.roque.app.waylla_app.fragments.MisPostsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        showToolbar("", true);

        BottomBar bottomBar = (BottomBar)findViewById(R.id.bottom_bar_post);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId){
                    case R.id.tab_home:
                        AllPostFragment allPostFragment = new AllPostFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.post_container_postList, allPostFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.tab_favoritos:
                        FavoritosPostFragment favoritosPostFragment = new FavoritosPostFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.post_container_postList, favoritosPostFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.tab_misposts:
                        MisPostsFragment misPostsFragment = new MisPostsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.post_container_postList, misPostsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                }
            }
        });
    }

    public void showToolbar(String titulo, Boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        TextView title = (TextView)findViewById(R.id.toolbar_tittle);
        title.setText("Posts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
