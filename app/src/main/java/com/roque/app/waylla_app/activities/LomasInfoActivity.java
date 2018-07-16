package com.roque.app.waylla_app.activities;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.fragments.LomasListFragment;
import com.roque.app.waylla_app.fragments.MapaLomasFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class LomasInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lomas_info);

        showToolbar("", true);

        BottomBar bottomBar = (BottomBar)findViewById(R.id.bottom_bar_lomas);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId){
                    case R.id.tab_lomas_mapa:
                        MapaLomasFragment mapaFragment = new MapaLomasFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_lomas_info, mapaFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.tab_lomas_list:
                        LomasListFragment lomasListFragment = new LomasListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_lomas_info, lomasListFragment)
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
        title.setText("Lomas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
