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
import android.widget.Button;
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
import com.roque.app.waylla_app.activities.DetalleEventoActivity;
import com.roque.app.waylla_app.models.Evento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventosRecyclerViewAdapter  extends RecyclerView.Adapter<EventosRecyclerViewAdapter.ViewHolder>  {

    private Context mContext;
    private List<Evento> mEventList;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    public EventosRecyclerViewAdapter(Context mContext, List<Evento> mEventList) {
        this.mContext = mContext;
        this.mEventList = mEventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_evento_item, parent,false);
        EventosRecyclerViewAdapter.ViewHolder postHolder = new EventosRecyclerViewAdapter.ViewHolder(v);
        mContext = parent.getContext();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return postHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String eventoId = mEventList.get(position).EventoId;
        final String currentUserId = mFirebaseAuth.getCurrentUser().getUid();

        String destino = mEventList.get(position).getDestino();
        String titulo = mEventList.get(position).getTitulo();
        String fecha_in = mEventList.get(position).getFecha_inicial();
        String hora_in = mEventList.get(position).getHora_inicial();
        String fecha_fin = mEventList.get(position).getFecha_final();
        String hora_fin = mEventList.get(position).getHora_final();

        holder.setEventsData(destino,titulo,fecha_in,fecha_fin,hora_in,hora_fin);

        final String user_id = mEventList.get(position).getUser_uid();

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

        //Get People Count
        mFirestore.collection("eventos/" + eventoId + "/personas").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int count = documentSnapshots.size();
                    holder.updatePeropleCount(count);
                } else {
                    holder.updatePeropleCount(0);
                }
            }
        });

        //Get Likes
        mFirestore.collection("eventos/" + eventoId + "/personas").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    holder.mJoinBtn.setText("Salir");
                } else {
                    holder.mJoinBtn.setText("Unirme");
                }
            }
        });

        //Likes Feature
        holder.mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirestore.collection("eventos/" + eventoId + "/personas").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){
                            Map<String, Object> peopleMap = new HashMap<>();
                            peopleMap.put("timestamp", FieldValue.serverTimestamp());

                            mFirestore.collection("eventos/" + eventoId + "/personas").document(currentUserId).set(peopleMap);
                        } else {
                            mFirestore.collection("eventos/" + eventoId + "/personas").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        holder.mVerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetalleEventoActivity.class);
                detailIntent.putExtra("event_id", eventoId);
                mContext.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mDestinoText,mTituloText ,mUsernameText, mFechaInicialText,mFechaFinalText, mAssitentesCounterText;
        private Button mJoinBtn, mVerBtn;
        private CircleImageView mImageUser;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mJoinBtn = (Button) mView.findViewById(R.id.eventos_btn_unirme);
            mVerBtn = (Button) mView.findViewById(R.id.eventos_btn_ver_evento);

        }

        public void setEventsData(String destino, String titulo, String fecha_inicial, String fecha_final, String hora_inicial, String hora_final){
            mDestinoText = mView.findViewById(R.id.evento_txt_destino);
            mTituloText = mView.findViewById(R.id.eventos_txt_titulo);
            mFechaInicialText = mView.findViewById(R.id.eventos_txt_fecha_inicial);
            mFechaFinalText = mView.findViewById(R.id.eventos_txt_fecha_final);

            mDestinoText.setText(destino);
            mTituloText.setText(titulo);
            mFechaInicialText.setText(fecha_inicial +" - " + hora_inicial);
            mFechaFinalText.setText(fecha_final + " - " + hora_final);

        }

        public void setUserData(String name, String image){
            mUsernameText = mView.findViewById(R.id.eventos_txt_username);
            mImageUser = mView.findViewById(R.id.eventos_image_foto);
            mUsernameText.setText(name);
            Glide.with(mContext).load(image).into(mImageUser);
        }

        public void updatePeropleCount(int count){
            mAssitentesCounterText = mView.findViewById(R.id.eventos_txt_asistentes);
            mAssitentesCounterText.setText("  "+count + " Personas");
        }
    }
}