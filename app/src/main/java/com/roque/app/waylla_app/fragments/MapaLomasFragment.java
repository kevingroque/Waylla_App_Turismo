package com.roque.app.waylla_app.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.adapters.CustomInfoWindowGoogleMap;
import com.roque.app.waylla_app.models.Loma;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaLomasFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private ImageButton mBtnSearch;
    private EditText mInputText;

    private FirebaseFirestore mFirestore;
    private List<Loma> lomaList;


    public MapaLomasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView  = inflater.inflate(R.layout.fragment_mapa_lomas, container, false);
        mFirestore = FirebaseFirestore.getInstance();
        lomaList = new ArrayList<>();



        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map_lomas);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        //mGoogleMap.setMinZoomPreference(11);

        mFirestore = FirebaseFirestore.getInstance();
        lomaList = new ArrayList<>();

        mFirestore.collection("lomas").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        String lomaId = doc.getDocument().getId();
                        Loma loma = doc.getDocument().toObject(Loma.class).withId(lomaId);

                        Loma info = new Loma();
                        info.setNombre(loma.getNombre());
                        info.setDescripcion(loma.getDescripcion());
                        info.setImagen(loma.getImagen());


                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getActivity());
                        mGoogleMap.setInfoWindowAdapter(customInfoWindow);

                        LatLng latLng = new LatLng(loma.getLatitud(),loma.getLongitud());

                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng)
                                .title(loma.getNombre())
                                .snippet(loma.getDescripcion())
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                        Marker marker = mGoogleMap.addMarker(markerOptions);
                        marker.setTag(info);
                        marker.showInfoWindow();

                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        float zoon = 8;
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoon));
                    }
                }
            }
        });

       /* // Add a marker in Sydney and move the camera
        LatLng tecsup = new LatLng(-12.0437, -76.9531);



        mGoogleMap.addMarker(new MarkerOptions().position(tecsup).title("Marker in Tecsup"));
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(tecsup));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tecsup,10));*/
    }
}
