package com.roque.app.waylla_app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoUsuarioDialog  extends DialogFragment implements View.OnClickListener{

    private View mRootView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mRootView = getActivity().getLayoutInflater().inflate(R.layout.info_usuario_dialog, null);
        mRootView = (CircleImageView)mRootView.findViewById(R.id.dialog_img_fotoperfil);
        mRootView = (TextView)mRootView.findViewById(R.id.dialog_text_nombreuser);
        builder.setView(mRootView);
        return builder.create();
    }

    @Override
    public void onClick(View v) {

    }
}
