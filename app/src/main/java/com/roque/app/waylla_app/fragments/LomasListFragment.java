package com.roque.app.waylla_app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.adapters.LomasRecyclerViewAdapter;
import com.roque.app.waylla_app.models.Loma;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LomasListFragment extends Fragment {

    private RecyclerView mLomasListRecycler;
    private List<Loma> lomaList;

    private LomasRecyclerViewAdapter mLomasAdapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private DocumentSnapshot mLastVisible;
    private Boolean isFirstPageFirstLoad = true;


    public LomasListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lomas_list, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        lomaList = new ArrayList<>();

        mLomasListRecycler = (RecyclerView) view.findViewById(R.id.lomas_recycler_lomasList);

        mLomasAdapter = new LomasRecyclerViewAdapter(getContext(),lomaList);
        mLomasListRecycler.setHasFixedSize(true);
        mLomasListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mLomasListRecycler.setAdapter(mLomasAdapter);

        if(mFirebaseAuth.getCurrentUser() != null) {
            mLomasListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
                        loadMoreLomas();
                    }
                }
            });
        }

        Query firstQuery = mFirestore.collection("lomas").orderBy("nombre", Query.Direction.DESCENDING).limit(10);
        firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    if (isFirstPageFirstLoad) {
                        mLastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        lomaList.clear();
                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String lomaId = doc.getDocument().getId();
                            Loma loma = doc.getDocument().toObject(Loma.class).withId(lomaId);

                            if (isFirstPageFirstLoad) {
                                lomaList.add(loma);
                            } else {
                                lomaList.add(0, loma);
                            }
                            mLomasAdapter.notifyDataSetChanged();
                        }
                    }
                    isFirstPageFirstLoad = false;
                }
            }
        });
        return view;
    }

    public void loadMoreLomas(){

        if(mFirebaseAuth.getCurrentUser() != null) {

            Query nextQuery = mFirestore.collection("lomas")
                    .orderBy("nombre", Query.Direction.DESCENDING)
                    .startAfter(mLastVisible)
                    .limit(10);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        mLastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String LomasId = doc.getDocument().getId();
                                Loma loma = doc.getDocument().toObject(Loma.class).withId(LomasId);
                                lomaList.add(loma);

                                mLomasAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }


}
