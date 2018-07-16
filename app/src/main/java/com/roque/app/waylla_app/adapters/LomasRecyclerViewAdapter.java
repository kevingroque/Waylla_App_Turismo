package com.roque.app.waylla_app.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.activities.DetalleLomaActivity;
import com.roque.app.waylla_app.models.Loma;

import java.util.List;

public class LomasRecyclerViewAdapter extends RecyclerView.Adapter<LomasRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<Loma> mLomaList;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    public LomasRecyclerViewAdapter(Context mContext, List<Loma> mLomaList) {
        this.mContext = mContext;
        this.mLomaList = mLomaList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_lomas_item, parent,false);
        LomasRecyclerViewAdapter.ViewHolder lomasHolder = new LomasRecyclerViewAdapter.ViewHolder(v);
        mContext = parent.getContext();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return lomasHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final String lomasId = mLomaList.get(position).LomaId;

        String nombreLoma = mLomaList.get(position).getNombre();
        String distrito = mLomaList.get(position).getDistrito();
        String ubicacion = mLomaList.get(position).getUbicacion();
        String image_url = mLomaList.get(position).getImagen();

        holder.setDataLoma(nombreLoma, distrito,ubicacion,image_url);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalleIntent = new Intent(mContext, DetalleLomaActivity.class);
                detalleIntent.putExtra("lomas_id", lomasId);
                mContext.startActivity(detalleIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLomaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView mNombreLomaText, mDistritoText, mUbicacionText;
        private ImageView mImagenLoma;

        private CoordinatorLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLayout = mView.findViewById(R.id.lomas_card);
        }

        public void setDataLoma(String nombre,String distrito, String ubicacion ,String image){
            mNombreLomaText = mView.findViewById(R.id.lomas_txt_nombreloma);
            mDistritoText = mView.findViewById(R.id.txt_distrito_lomas_list);
            mUbicacionText = mView.findViewById(R.id.lomas_txt_ubicacion);
            mImagenLoma= mView.findViewById(R.id.lomas_img_foto);

            mNombreLomaText.setText(nombre);
            mDistritoText.setText(distrito);
            mUbicacionText.setText(ubicacion);
            Glide.with(mContext).load(image).into(mImagenLoma);
        }
    }
}
