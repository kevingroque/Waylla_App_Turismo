package com.roque.app.waylla_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.roque.app.waylla_app.R;

public class DetalleLomaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_loma);

        TextView mText = (TextView)findViewById(R.id.text_id_loma);
        String loma_id = getIntent().getStringExtra("lomas_id");
        mText.setText(loma_id);

    }
}
