package com.perlaaguileta.imc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link registrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class registrarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText nombre, edad, peso,estatura, correo, contraseña;
    Button btnRegistro;

    //variable de los datos a registrar

    private String name ="";
    private int age = 0;
    private double weight = 0.0;
    private double height= 0.0;
    private String email ="";
    private String password ="";

    FirebaseAuth auth ;
    DatabaseReference database;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public registrarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment registrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static registrarFragment newInstance(String param1, String param2) {
        registrarFragment fragment = new registrarFragment();
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
        return inflater.inflate(R.layout.fragment_registrar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        nombre = view.findViewById(R.id.txtNombre);
        edad = view.findViewById(R.id.txtEdad);
        peso =  view.findViewById(R.id.txtPeso);
        estatura = view.findViewById(R.id.txtEstatura);
        correo =  view.findViewById(R.id.txtEmail);
        contraseña = view.findViewById(R.id.txtPass);

        final NavController navController = Navigation.findNavController(view);
        btnRegistro = view.findViewById(R.id.btn_registrarse);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validacion

                if (!nombre.getText().toString().isEmpty() && !correo.getText().toString().isEmpty() && !contraseña.getText().toString().isEmpty() && !edad.getText().toString().isEmpty()
                        && !peso.getText().toString().isEmpty() && !estatura.getText().toString().isEmpty()){

                    name = nombre.getText().toString();
                    age = Integer.valueOf(edad.getText().toString());
                    weight = Double.parseDouble(peso.getText().toString());
                    height = Double.parseDouble(estatura.getText().toString());
                    email = correo.getText().toString();
                    password = contraseña.getText().toString();

                    if(password.length()>=6) {
                        registrarUser();
                    }else{
                        Toast.makeText(getActivity() , "Se require que la contraseña contenga al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity() , "Se requiere que acomplete los campos.", Toast.LENGTH_SHORT).show();
                }

                navController.navigate(R.id.action_registrarFragment_to_inicioFragment);
            }
        });
    }

    private void registrarUser(){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map< String, Object > map = new HashMap<>();
                    map.put("Nombre", name);
                    map.put("Edad", age);
                    map.put("Peso", weight);
                    map.put("Estatura", height);
                    map.put("Correo", email);
                    map.put("Contraseña", password);

                    String id = auth.getCurrentUser().getUid();
                    database.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(getActivity() , "El registro se ha completado satisfactoriamnete.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity() , "No se pudieron crear los datos.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity() , "No se puede registrar este usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}