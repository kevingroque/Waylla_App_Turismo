package com.roque.app.waylla_app.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.roque.app.waylla_app.R;
import com.roque.app.waylla_app.adapters.LomasGridViewAdapter;
import com.roque.app.waylla_app.fragments.DatePickerFragment;
import com.roque.app.waylla_app.models.Evento;
import com.roque.app.waylla_app.models.Loma;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearEventoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    private LinearLayout mLayoutDetalles, mLayoutRequisitos, mLayoutItinerario
                        ,mLayoutCosto, mLayoutRecomendaciones;

    private FloatingActionButton mFabNext1, mFabNext2, mFabNext3, mFabNext4, mFabNext5
                                ,mFabBack1, mFabBack2, mFabBack3, mFabBack4, mFabBack5 , mFabCrear;

    private EditText mTitulo,mFechaInicio,mFechaFinal,mHoraInicio,mHoraFinal ,mDescripcion, mRequisitos, mRecomendaciones, mCosto, mDetalleCosto;

    private ProgressDialog mProgress;

    private TextView mNombreSelectLomaTxt;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    private Context mContext;
    private List<Loma> lomaList;
    private LomasGridViewAdapter mLomasAdapter;

    private Boolean isFirstPageFirstLoad = true;

    private CoordinatorLayout mCLayout;
    private GridView mGridView;

    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;

    private String destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        //Inicializar Firestore
        mStorage = FirebaseStorage.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();

        //Instanciar todos los componentes

        mProgress = new ProgressDialog(this);

        mLayoutDetalles = (LinearLayout) findViewById(R.id.crearevento_layout_detalles);
        mLayoutRequisitos = (LinearLayout) findViewById(R.id.crearevento_layout_requisitos);
        mLayoutCosto = (LinearLayout) findViewById(R.id.crearevento_layout_costo);

        mFabNext1 = (FloatingActionButton) findViewById(R.id.creareventos_fab_next_1);
        mFabNext2 = (FloatingActionButton) findViewById(R.id.creareventos_fab_next_2);
        mFabNext3 = (FloatingActionButton) findViewById(R.id.creareventos_fab_next_3);

        mFabBack1 = (FloatingActionButton) findViewById(R.id.creareventos_fab_back_1);
        mFabBack2 = (FloatingActionButton) findViewById(R.id.creareventos_fab_back_2);
        mFabBack3 = (FloatingActionButton) findViewById(R.id.creareventos_fab_back_3);
        mFabCrear = (FloatingActionButton) findViewById(R.id.creareventos_fab_crear_events);

        mCLayout = (CoordinatorLayout) findViewById(R.id.crearevento_coordinator_layout);
        mGridView = (GridView) findViewById(R.id.crearevento_gridview_lomaslist);

        mTitulo = (EditText) findViewById(R.id.crearevento_edittext_titulo);
        mFechaInicio = (EditText) findViewById(R.id.crearevento_edittext_fecha_inicio);
        mFechaFinal = (EditText) findViewById(R.id.crearevento_edittext_fecha_final);
        mHoraInicio = (EditText) findViewById(R.id.crearevento_edittext_hora_inicio);
        mHoraFinal = (EditText) findViewById(R.id.crearevento_edittext_hora_final);
        mDescripcion = (EditText) findViewById(R.id.crearevento_edittext_descripcion_evento);
        mRequisitos = (EditText) findViewById(R.id.crearevento_edittext_requisitos);
        mRecomendaciones = (EditText) findViewById(R.id.crearevento_edittext_recomendaciones);
        mCosto = (EditText) findViewById(R.id.crearevento_edittext_costo);
        mDetalleCosto = (EditText) findViewById(R.id.crearevento_edittext_detalle_costo);

        //Click o pulsar el componente
        mFechaInicio.setOnClickListener(this);
        mFechaFinal.setOnClickListener(this);
        mHoraInicio.setOnClickListener(this);
        mHoraFinal.setOnClickListener(this);

        mFabNext1.setOnClickListener(this);
        mFabNext2.setOnClickListener(this);
        mFabNext3.setOnClickListener(this);
        mFabBack1.setOnClickListener(this);
        mFabBack2.setOnClickListener(this);
        mFabBack3.setOnClickListener(this);

        mFabCrear.setOnClickListener(this);


        //Inicializar Lista
        lomaList = new ArrayList<>();

        //Inicializar Adaptador
        mLomasAdapter = new LomasGridViewAdapter(this,lomaList);

        // Mostrar los datos en el gridview
        mGridView.setAdapter(mLomasAdapter);

        llenarGridView();
        seleccionarLoma();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Seleccionar fechas
            case R.id.crearevento_edittext_fecha_inicio:
                showDatePickerDialog(mFechaInicio);
                break;
            case R.id.crearevento_edittext_fecha_final:
                showDatePickerDialog(mFechaFinal);
                break;
            //Seleccionar Hora
            case R.id.crearevento_edittext_hora_inicio:
                showTimePickerDialog(mHoraInicio);
                break;
            case R.id.crearevento_edittext_hora_final:
                showTimePickerDialog(mHoraFinal);
                break;
            //Siguiente layout
            case R.id.creareventos_fab_next_1:
                animSiguiente(mLayoutDetalles);
                break;
            case R.id.creareventos_fab_next_2:
                animSiguiente(mLayoutRequisitos);
                break;
            case R.id.creareventos_fab_next_3:
                animSiguiente(mLayoutCosto);
                break;
            //Regresar al layout anterior
            case R.id.creareventos_fab_back_1:
                animRegresar(mLayoutDetalles);
                break;
            case R.id.creareventos_fab_back_2:
                animRegresar(mLayoutRequisitos);
                break;
            case R.id.creareventos_fab_back_3:
                animRegresar(mLayoutCosto);
                break;
            case R.id.creareventos_fab_crear_events:
                crearEvento();
                break;
        }
    }

    //Poblar Grid View
    private void llenarGridView(){
        Query firstQuery = mFirestore.collection("lomas").orderBy("nombre", Query.Direction.DESCENDING).limit(10);
        firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

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
    }

    //Seleccionar un item del gridview
    private void seleccionarLoma(){
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                mNombreSelectLomaTxt =(TextView) view.findViewById(R.id.selectloma_txt_nombreloma);

                destino = mNombreSelectLomaTxt.getText().toString();

                // Display the selected item text on snack bar
                Snackbar snackbar = Snackbar.make(
                        mCLayout,
                        "Selected : " + selectedItem +" - " + mNombreSelectLomaTxt.getText().toString(),
                        Snackbar.LENGTH_LONG
                );
                snackbar.getView().setBackgroundColor(Color.parseColor("#FF108714"));
                snackbar.show();
            }
        });
    }

    //Mostrar Dialog Fecha
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(this.getFragmentManager(), "datePicker");
    }

    //Mostrar Dialog Hora
    private void  showTimePickerDialog(final EditText editText){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                editText.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, minuto, false);

        timePickerDialog.show();
    }

    //Formaton de hora
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }


    //Crear evento
    private void crearEvento(){

        mProgress.setMessage("Creando Evento :)...");
        mProgress.show();
        mProgress.setCancelable(false);

        final String titulo = mTitulo.getText().toString();
        final String fecha_inicio = mFechaInicio.getText().toString();
        final String fecha_final = mFechaFinal.getText().toString();
        final String hora_inicio = mHoraInicio.getText().toString();
        final String hora_final = mHoraFinal.getText().toString();
        final String descripcion = mDescripcion.getText().toString();
        final String requisito = mRequisitos.getText().toString();
        final String recomendaciones = mRecomendaciones.getText().toString();
        final String costo = mCosto.getText().toString();
        final String detalle_costo = mDetalleCosto.getText().toString();

        if (!TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(descripcion)
                && !TextUtils.isEmpty(fecha_inicio) && !TextUtils.isEmpty(fecha_final)
                && !TextUtils.isEmpty(hora_inicio) && !TextUtils.isEmpty(hora_final)){

            final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mFirebaseAuth.getCurrentUser();

            Date date = new Date();

            Map<String,Object> eventoMap = new HashMap<>();
            eventoMap.put("destino", destino);
            eventoMap.put("user_uid", user.getUid());
            eventoMap.put("titulo", titulo);
            eventoMap.put("fecha_inicial", fecha_inicio);
            eventoMap.put("fecha_final",fecha_final);
            eventoMap.put("hora_inicial",hora_inicio);
            eventoMap.put("hora_final", hora_final);
            eventoMap.put("descripcion", descripcion);
            if (TextUtils.isEmpty(requisito)){
                eventoMap.put("requisito", "requisito");
            }else {
                eventoMap.put("requisito", requisito);
            }
            if (TextUtils.isEmpty(recomendaciones)){
                eventoMap.put("recomendaciones","");
            }else {
                eventoMap.put("recomendaciones",recomendaciones);
            }
            if (TextUtils.isEmpty(costo)){
                eventoMap.put("costo", "0");
            }else {
                eventoMap.put("costo", costo);
            }
            if (TextUtils.isEmpty(detalle_costo)){
                eventoMap.put("detalle_costo", "");
            }else {
                eventoMap.put("detalle_costo", detalle_costo);
            }
            eventoMap.put("timestamp", FieldValue.serverTimestamp());

            mFirestore.collection("eventos").add(eventoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    mProgress.dismiss();
                    if (task.isSuccessful()){
                        goEvents();
                    }else {
                        Toast.makeText(CrearEventoActivity.this, "Error al crear evento", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            mProgress.dismiss();
            Toast.makeText(CrearEventoActivity.this, "Error al crear evento", Toast.LENGTH_SHORT).show();
        }
    }

    private void goEvents() {
        Intent intent = new Intent(this, EventosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Animacion de layouts
    private void animar(boolean mostrar, LinearLayout mLayout) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        mLayout.setLayoutAnimation(controller);
        mLayout.startAnimation(animation);
    }

    //Siguiente
    public void animSiguiente(LinearLayout mLayout) {
        if (mLayout.getVisibility() == View.GONE) {
            animar(true, mLayout);
            mLayout.setVisibility(View.VISIBLE);
        }
    }

    //Regresar
    public void animRegresar(LinearLayout mLayout) {
        if (mLayout.getVisibility() == View.VISIBLE) {
            animar(false, mLayout);
            mLayout.setVisibility(View.GONE);
        }
    }


}
