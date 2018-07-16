package com.roque.app.waylla_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.activities.CommentariosActivity;
import com.roque.app.waylla_app.models.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPostList;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    public PostsRecyclerViewAdapter(Context mContext, List<Post> mPostList){
        this.mContext = mContext;
        this.mPostList = mPostList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post_item, parent,false);
        PostsRecyclerViewAdapter.ViewHolder postHolder = new PostsRecyclerViewAdapter.ViewHolder(v);
        mContext = parent.getContext();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return postHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        final String postId = mPostList.get(position).PostId;
        final String currentUserId = mFirebaseAuth.getCurrentUser().getUid();

        String desc = mPostList.get(position).getDescripcion();
        holder.setDescripcionText(desc);

        String fecha = mPostList.get(position).getFecha();
        holder.setFechaText(fecha);

        String hour = mPostList.get(position).getHora();
        holder.setHoraText(hour);

        String image_url = mPostList.get(position).getImage_url();
        holder.setPostImage(image_url);

        final String user_id = mPostList.get(position).getUser_uid();

        mFirestore.collection("usuarios").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String userName = task.getResult().getString("nombre");
                    String userImage = task.getResult().getString("avatar");
                    holder.setUserData(userName, userImage);
                } else {
                    //Firebase Exception
                }
            }
        });

        //Get Likes Count
        mFirestore.collection("posts/" + postId + "/likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int count = documentSnapshots.size();
                    holder.updateLikesCount(count);
                } else {
                    holder.updateLikesCount(0);
                }
            }
        });

        //Get Likes
        mFirestore.collection("posts/" + postId + "/likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    holder.mLikeBtn.setImageDrawable(mContext.getDrawable(R.drawable.ic_heart_full));
                } else {
                    holder.mLikeBtn.setImageDrawable(mContext.getDrawable(R.drawable.ic_like));
                }
            }
        });

        //Likes Feature
        holder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirestore.collection("posts/" + postId + "/likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            mFirestore.collection("posts/" + postId + "/likes").document(currentUserId).set(likesMap);
                        } else {
                            mFirestore.collection("posts/" + postId + "/likes").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        holder.mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(mContext, CommentariosActivity.class);
                commentIntent.putExtra("post_id", postId);
                mContext.startActivity(commentIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView mNombreText,mDescripcionText, mFechaText,mHoraText, mLikeCounterText;
        private ImageView mImagenPost, mLikeBtn, mCommentBtn;
        private CircleImageView mImageUser;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikeBtn = mView.findViewById(R.id.likesImageView);
            mCommentBtn = mView.findViewById(R.id.commentsCountImageView);
        }

        public void setDescripcionText(String descripcion){
            mDescripcionText = mView.findViewById(R.id.txt_descripcion_post_list);
            mDescripcionText.setText(descripcion);
        }

        public void setFechaText(String fecha){
            mFechaText = mView.findViewById(R.id.dateTextView);
            mFechaText.setText(fecha);
        }

        public void setHoraText(String hora){
            mHoraText = mView.findViewById(R.id.timeTextView);
            mHoraText.setText(hora);
        }

        public void setPostImage(String downloadUri){
            mImagenPost = mView.findViewById(R.id.img_foto_post_list);
            Glide.with(mContext).load(downloadUri).into(mImagenPost);
        }

        public void setUserData(String name, String image){
            mNombreText = mView.findViewById(R.id.txt_nombre_post_list);
            mImageUser = mView.findViewById(R.id.img_perfil_user_post_list);
            mNombreText.setText(name);
            Glide.with(mContext).load(image).into(mImageUser);
        }

        public void updateLikesCount(int count){
            mLikeCounterText = mView.findViewById(R.id.likeCounterTextView);
            mLikeCounterText.setText(count + " Likes");
        }
    }
}
