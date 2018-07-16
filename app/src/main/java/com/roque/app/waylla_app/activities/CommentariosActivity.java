package com.roque.app.waylla_app.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.adapters.ComentariosRecyclerViewAdapter;
import com.roque.app.waylla_app.models.Comentario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentariosActivity extends AppCompatActivity {

    private EditText mComentMensajeTxt;
    private ImageView mSendCommentBtn;
    private RecyclerView mCommentsRecycler;
    private ComentariosRecyclerViewAdapter mCommentsAdapter;
    private List<Comentario> commentsList;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private String post_id;
    private String current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentarios);

        showToolbar("", true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        current_user_id = mFirebaseAuth.getCurrentUser().getUid();
        post_id = getIntent().getStringExtra("post_id");

        mComentMensajeTxt = findViewById(R.id.comment_txt_mensaje);
        mSendCommentBtn = findViewById(R.id.comment_btn_post);
        mCommentsRecycler = findViewById(R.id.comment_recycler_list);

        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        mCommentsAdapter = new ComentariosRecyclerViewAdapter(commentsList);
        mCommentsRecycler.setHasFixedSize(true);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCommentsRecycler.setAdapter(mCommentsAdapter);

        mFirestore.collection("posts/" + post_id + "/comments")
                .addSnapshotListener(CommentariosActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String commentId = doc.getDocument().getId();
                                    Comentario comments = doc.getDocument().toObject(Comentario.class);
                                    commentsList.add(comments);
                                    mCommentsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

        mSendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_message = mComentMensajeTxt.getText().toString();

                Map<String, Object> commentsMap = new HashMap<>();
                commentsMap.put("message", comment_message);
                commentsMap.put("user_id", current_user_id);
                commentsMap.put("timestamp", FieldValue.serverTimestamp());

                mFirestore.collection("posts/" + post_id + "/comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(CommentariosActivity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            mComentMensajeTxt.setText("");
                        }
                    }
                });

            }
        });
    }

    public void showToolbar(String titulo, Boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        TextView title = (TextView)findViewById(R.id.toolbar_tittle);
        title.setText("Comentarios");
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
