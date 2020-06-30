package com.perlaaguileta.imc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference database;

    EditText  imc, clasi, reco;
   // TextView reco;
    String cad, recomendacion, imagen;
    boolean bandera = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        imc = view.findViewById(R.id.txtIMC);
        clasi = view.findViewById(R.id.txtClasificacion);
        reco = view.findViewById(R.id.txtRecomendacion);

        Button btn_editarDatos = view.findViewById(R.id.btn_editarDatos);
        Button btn_informe = view.findViewById(R.id.btn_informe);
        Button btn_cerrar = view.findViewById(R.id.btn_cerrarSesion);
        Button btn_avance = view.findViewById(R.id.btn_registrarAvance);

        getUserInfo();

        btn_avance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registroAvance();
            }
        });
        btn_editarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_controlFragment_to_editarFragment);
            }
        });

        btn_informe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_controlFragment_to_informeFragment);
            }
        });

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            //private FirebaseAuth firebaseAuth;
            @Override
            public void onClick(View v) {
                auth.signOut();
                navController.navigate(R.id.action_controlFragment_to_inicioFragment);
            }
               // auth.signOut();
                //auth.getInstance().signOut();

            });
    }

    private void getUserInfo(){
        String id = null;
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user!=null) {
            id = user.getUid();

        database.child("users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    double pes = Double.parseDouble(dataSnapshot.child("Peso").getValue().toString());
                    double estatu = Double.parseDouble(dataSnapshot.child("Estatura").getValue().toString());;

                    double Imc = pes/(estatu*estatu);

                    imc.setText(String.valueOf(Imc));

                    clasificacion(Imc);

                    clasi.setText(cad);
                    reco.setText(recomendacion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }else {
            Log.i("getUid",  "usuario no encontrado");

        }
    }

    private void clasificacion(double imc){

        if(imc<16.00){
            cad="Infrapeso: Delgadez Severa";
            imagen="https://quediferenciahay.com/wp-content/uploads/2014/09/Nutriologo-y-nutricionista-SFW.jpg";
            recomendacion="Comunicarse con un nutriologo.";
        }else if(imc>=16.00 && imc<=16.99){
            cad="Infrapeso: Delgadez moderada";
            imagen="https://img.europapress.es/fotoweb/fotonoticia_20150109140616_1024.jpg";
            recomendacion="Aumenta la ingesta de cereales.";
        }else if(imc>=17.00 && imc<=18.49){
            cad="Infrapeso: Delgadez aceptable";
            imagen="https://www.cambio16.com/wp-content/uploads/2018/03/proteina-animal-vegetal-640x426.jpg";
            recomendacion="Aumenta la ingesta de proteinas.";
        }else if(imc>=18.50 && imc<=24.99){
            cad="Peso Normal";
            imagen="https://www.anahuac.mx/mexico/sites/default/files/noticias/Hacer-ejercicio-fisico-te-ayuda-a-fortalecer-tu-funcion-inmune.jpg";
            recomendacion= "Entrenar 3 a 5 veces por semana.";
        }else if(imc>=25.00 && imc<=29.99){
            cad="Sobrepeso";
            imagen="https://www.cubahora.cu/uploads/imagen/2019/09/30/azucar-consumo.jpeg";
            recomendacion="Reducir el consumo de azucar.";
        }else if(imc>=30.00 && imc<=34.99){
            cad="Obeso: Tipo I";
            imagen="https://i.blogs.es/c4739e/platoservidos/450_1000.jpg";
            recomendacion="Controlar las cantidades de comida.";
        }else if(imc>=35.00 && imc<=39.99){
            cad="Obeso: Tipo II";
            imagen="https://estaticos.serpadres.es/media/cache/760x570_thumb/uploads/images/article/53db7277dce6174d23528c27/dieta.jpg";
            recomendacion="Disponer de un metodo o dieta.";
        }else if(imc>=40.00 ){
            cad="Obeso: Tipo III";
            imagen="https://hidrolipoclasiainfo.files.wordpress.com/2011/07/disminuir-consumo-de-grasas.jpg?w=584";
            recomendacion="Disminuir el consumo de grasas.";
        }else{
            cad="no existe clasificacion";
        }
    }

    private void registroAvance(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c);

        final String imcAvance = imc.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            final String uid = user.getUid();
            database.child("users").child(uid).child("Avances").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        final Map <String, Object> AvanceMap = new HashMap<>();
                        AvanceMap.put("IMC", imcAvance);
                        AvanceMap.put("Clasificación", cad);
                        AvanceMap.put("Recomendación", recomendacion);
                        AvanceMap.put("image", imagen);

                        database.child("users").child(uid).child("Avances").child(formattedDate).setValue(AvanceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Avance registrado satisfactoriamnete.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "No se pudo registrar el avance. Intentelo más tarde.", Toast.LENGTH_SHORT).show();
                                }
                            }});
                    }else {
                        Toast.makeText(getActivity() , "No se pudo realizar la operacion.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{

       }
    }
}