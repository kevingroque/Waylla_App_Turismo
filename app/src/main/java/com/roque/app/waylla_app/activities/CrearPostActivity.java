package com.roque.app.waylla_app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roque.app.waylla_app.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class CrearPostActivity extends AppCompatActivity {

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final String TAG = "CrearPostActivity";

    private ImageView mSelectedImage;
    private EditText mDescripcionText;
    private Button mCrearPostBtn;
    private Uri mImageUri = null;
    private ProgressDialog mProgress;

    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();

        mCrearPostBtn = (Button) findViewById(R.id.crearpost_btn_publicar);
        mSelectedImage = (ImageView) findViewById(R.id.crearpost_img_imagen);
        mDescripcionText = (EditText) findViewById(R.id.crearpost_edittext_descripción);

        mProgress = new ProgressDialog(this);

        mSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryInten = new Intent(Intent.ACTION_GET_CONTENT);
                galleryInten.setType("image/*");
                startActivityForResult(galleryInten,GALLERY_REQUEST_CODE);
            }
        });

        mCrearPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingPost();
            }
        });
    }

    private void startingPost(){

        mProgress.setMessage("Publicando...");
        mProgress.show();
        mProgress.setCancelable(false);

        final String descripcion = mDescripcionText.getText().toString();

        if (!TextUtils.isEmpty(descripcion) && mImageUri  != null){

            final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mFirebaseAuth.getCurrentUser();

            StorageReference filePath = mStorage.child("posts_images/"+mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){

                        String downloadUri = task.getResult().getDownloadUrl().toString();

                        Date date = new Date();
                        String stringTime = DateFormat.getTimeInstance().format(date);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
                        String dateInString = "31-08-1982";
                        String stringDate =  dateFormat.format(date);

                        Map<String,Object> postMap = new HashMap<>();
                        postMap.put("image_url", downloadUri);
                        postMap.put("descripcion", descripcion);
                        postMap.put("user_uid",user.getUid());
                        postMap.put("hora",stringTime);
                        postMap.put("fecha", stringDate);
                        postMap.put("timestamp", FieldValue.serverTimestamp());

                        mFirestore.collection("posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                mProgress.dismiss();
                                if (task.isSuccessful()){

                                    //Codigo parasubir puntos

                                    
                                    goPosts();
                                }else {
                                    Toast.makeText(CrearPostActivity.this, "Error al subir post", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        mProgress.dismiss();
                        Toast.makeText(CrearPostActivity.this, "Error al subir post", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                mSelectedImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                CrearPostActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(CrearPostActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

    private void goPosts() {
        Intent intent = new Intent(this, PostsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
