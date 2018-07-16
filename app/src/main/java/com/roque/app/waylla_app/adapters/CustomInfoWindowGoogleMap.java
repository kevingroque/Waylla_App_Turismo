package com.roque.app.waylla_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.models.Loma;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info_window_item, null);

        TextView name_tv = view.findViewById(R.id.infowindow_txt_nombre_loma);
        TextView details_tv = view.findViewById(R.id.infowindow_txt_descripcion);
        ImageView img = view.findViewById(R.id.infowindow_img_loma);


        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        Loma infoWindowData = (Loma) marker.getTag();

        Glide.with(context).load(infoWindowData.getImagen()).into(img);


        return view;
    }
}
