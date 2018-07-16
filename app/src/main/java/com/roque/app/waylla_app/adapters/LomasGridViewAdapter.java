package com.roque.app.waylla_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.models.Loma;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LomasGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Loma> mLomaList;
    private LayoutInflater mLayoutInflater;

    public LomasGridViewAdapter(Context mContext, List<Loma> mLomaList) {
        this.mContext = mContext;
        this.mLomaList = mLomaList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLomaList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.cardview_select_lomas_item, parent, false);

            TextView nombre = (TextView)convertView.findViewById(R.id.selectloma_txt_nombreloma);
            CircleImageView imageView = (CircleImageView) convertView.findViewById(R.id.selectloma_img_loma);

            String url_image = mLomaList.get(position).getImagen();

            nombre.setText(mLomaList.get(position).getNombre());
            Glide.with(mContext).load(url_image).into(imageView);

        }
        return convertView;
    }
}
