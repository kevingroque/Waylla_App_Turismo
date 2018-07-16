package com.roque.app.waylla_app.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.adapters.EventosRecyclerViewAdapter;
import com.roque.app.waylla_app.adapters.PostsRecyclerViewAdapter;
import com.roque.app.waylla_app.models.Evento;
import com.roque.app.waylla_app.models.Post;

import java.util.ArrayList;
import java.util.List;

import static com.ibm.mobilefirstplatform.clientsdk.android.analytics.internal.inappfeedback.MFPInAppFeedBackListner.getContext;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView mEventsListRecycler;
    private List<Evento> eventoList;

    private EventosRecyclerViewAdapter mEventosAdapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private DocumentSnapshot mLastVisible;
    private Boolean isFirstPageFirstLoad = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        showToolbar("", true);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        eventoList = new ArrayList<>();

        mEventsListRecycler = (RecyclerView) findViewById(R.id.eventos_recycler_eventlist);

        mEventosAdapter = new EventosRecyclerViewAdapter(this,eventoList);
        mEventsListRecycler.setHasFixedSize(true);
        mEventsListRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEventsListRecycler.setAdapter(mEventosAdapter);

        if(mFirebaseAuth.getCurrentUser() != null) {
            mEventsListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
                        loadMorePost();
                    }
                }
            });
        }

        Query firstQuery = mFirestore.collection("eventos").orderBy("timestamp", Query.Direction.DESCENDING).limit(10);
        firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    if (isFirstPageFirstLoad) {
                        mLastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        eventoList.clear();
                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String eventId = doc.getDocument().getId();
                            Evento evento = doc.getDocument().toObject(Evento.class).withId(eventId);

                            if (isFirstPageFirstLoad) {
                                eventoList.add(evento);
                            } else {
                                eventoList.add(0, evento);
                            }
                            mEventosAdapter.notifyDataSetChanged();
                        }
                    }
                    isFirstPageFirstLoad = false;
                }
            }
        });
    }

    public void loadMorePost(){

        if(mFirebaseAuth.getCurrentUser() != null) {

            Query nextQuery = mFirestore.collection("eventos")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(mLastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(EventosActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        mLastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String eventId = doc.getDocument().getId();
                                Evento evento = doc.getDocument().toObject(Evento.class).withId(eventId);

                                eventoList.add(evento);

                                mEventosAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    public void goCrearEventos(View view){
        Intent intent = new Intent(this, CrearEventoActivity.class);
        startActivity(intent);
    }

    public void showToolbar(String titulo, Boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        TextView title = (TextView)findViewById(R.id.toolbar_tittle);
        title.setText("Eventos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
