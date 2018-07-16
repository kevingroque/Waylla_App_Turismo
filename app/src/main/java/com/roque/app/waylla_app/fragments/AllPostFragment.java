package com.roque.app.waylla_app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.roque.app.waylla_app.activities.CrearPostActivity;
import com.roque.app.waylla_app.adapters.PostsRecyclerViewAdapter;
import com.roque.app.waylla_app.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPostFragment extends Fragment {

    private RecyclerView mPostListRecycler;
    private List<Post> postList;
    private FloatingActionButton mCrearPostFab;

    private PostsRecyclerViewAdapter mPostAdapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private DocumentSnapshot mLastVisible;
    private Boolean isFirstPageFirstLoad = true;


    public AllPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_post, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        postList = new ArrayList<>();

        mCrearPostFab = (FloatingActionButton) view.findViewById(R.id.post_fab_agregar);
        mPostListRecycler = (RecyclerView) view.findViewById(R.id.post_recycler_postlist);

        mPostAdapter = new PostsRecyclerViewAdapter(getContext(),postList);
        mPostListRecycler.setHasFixedSize(true);
        mPostListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostListRecycler.setAdapter(mPostAdapter);

        if(mFirebaseAuth.getCurrentUser() != null) {
            mPostListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        Query firstQuery = mFirestore.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(10);
        firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    if (isFirstPageFirstLoad) {
                        mLastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        postList.clear();
                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String postId = doc.getDocument().getId();
                            Post post = doc.getDocument().toObject(Post.class).withId(postId);

                            if (isFirstPageFirstLoad) {
                                postList.add(post);
                            } else {
                                postList.add(0, post);
                            }
                            mPostAdapter.notifyDataSetChanged();
                        }
                    }
                    isFirstPageFirstLoad = false;
                }
            }
        });

        mCrearPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CrearPostActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


        public void loadMorePost(){

            if(mFirebaseAuth.getCurrentUser() != null) {

                Query nextQuery = mFirestore.collection("posts")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .startAfter(mLastVisible)
                        .limit(3);

                nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (!documentSnapshots.isEmpty()) {

                            mLastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String blogPostId = doc.getDocument().getId();
                                    Post post = doc.getDocument().toObject(Post.class).withId(blogPostId);
                                    postList.add(post);

                                    mPostAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            }
        }

}
