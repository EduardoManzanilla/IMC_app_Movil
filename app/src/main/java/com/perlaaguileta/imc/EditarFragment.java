package com.perlaaguileta.imc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private DatabaseReference database;
    private DatabaseReference dataB;
    EditText edad, peso, estatura, contraseña;
    Button btnActualizar;

    private int age = 0;
    private double weight = 0.0;
    private double height= 0.0;


    NavController navController;

    public EditarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarFragment newInstance(String param1, String param2) {
        EditarFragment fragment = new EditarFragment();
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
        return inflater.inflate(R.layout.fragment_editar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();

        edad = view.findViewById(R.id.txt_Edad);
        peso =  view.findViewById(R.id.txt_Peso);
        estatura = view.findViewById(R.id.txt_Estatura);

        navController = Navigation.findNavController(view);

        database = FirebaseDatabase.getInstance().getReference();

        final NavController navController = Navigation.findNavController(view);
        btnActualizar = view.findViewById(R.id.btn_actualizar);
        Button btn_eliminar = view.findViewById(R.id.btn_eliminarCuenta);

        mostrar();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUserInfo();
                /*
                edad = view.findViewById(R.id.txt_Edad);
                peso =  view.findViewById(R.id.txt_Peso);
                estatura = view.findViewById(R.id.txt_Estatura);*/
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_eliminar, null);
                contraseña = (EditText) v.findViewById(R.id.Dialog_Password);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(v)
                        // Add action buttons
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Entrando al onClik", "positivo");
                                String valor = contraseña.getText().toString();
                                Log.i("contraseña", valor);

                                validate(valor);

                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create();
                builder.show();
            }
        });
    }

    private void getUserInfo(){

        age = Integer.valueOf(edad.getText().toString());
        weight = Double.parseDouble(peso.getText().toString());
        height = Double.parseDouble(estatura.getText().toString());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            final String uid = user.getUid();

            HashMap personaMap = new HashMap();
            personaMap.put("Peso", weight);
            personaMap.put("Estatura", height);
            personaMap.put("Edad", age);

            database.child("users").child(uid).updateChildren(personaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity() , "Actualización Exitosa.", Toast.LENGTH_SHORT).show();
                }
            });

        }else{

        }
     /*   age = Integer.valueOf(edad.getText().toString());
        weight = Double.parseDouble(peso.getText().toString());
        height = Double.parseDouble(estatura.getText().toString());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            final String uid = user.getUid();
            database.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        Map <String, Object> personaMap = new HashMap<>();
                        personaMap.put("Peso", weight);
                        personaMap.put("Estatura", height);
                        personaMap.put("Edad", age);

                        database.child("users").child(uid).updateChildren(personaMap);
                        Toast.makeText(getActivity() , "Actualización Exitosa.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity() , "No se pudo realizar la actualización.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{

        }*/
    }


    private void mostrar(){
        String id = null;
        auth = FirebaseAuth.getInstance();
        //String id = auth.getInstance().getCurrentUser().getUid();
        //FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null) {
            id = user.getUid();
            Log.i("getUid: ", id);
        }else {
            Log.i("getUid",  "usuario no encontrado");

        }


        database = FirebaseDatabase.getInstance().getReference();//new DatabaseReference();
        dataB = database.child("users");

        dataB.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String ed = dataSnapshot.child("Edad").getValue().toString();
                    String pess = dataSnapshot.child("Peso").getValue().toString();
                    String esta = dataSnapshot.child("Estatura").getValue().toString();

                    edad.setText(ed);
                    peso.setText(pess);
                    estatura.setText(esta);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void validate(final String oldPassword) {//Metodo para eliminar usuario des de la autenticacion

        String id = "";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            id = user.getUid();

            database.child("users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        String coo = dataSnapshot.child("Correo").getValue().toString();

                        AuthCredential credential = EmailAuthProvider.getCredential(coo, oldPassword);
                            user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("Re-authenticated");
                                    user.delete();
                                    eliminarBase();
                                }

                            });
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

    private void eliminarBase(){//Eliminacion de los datos de la base de datos, pero no de la autenticaion
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            final String uid = user.getUid();
            database.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        database.child("users").child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                navController.navigate(R.id.action_editarFragment_to_inicioFragment);
                                Toast.makeText(getActivity() , "Cuenta Eliminada con Exito.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity() , "Lo sentinmos tenems problemas para eliminar su cuenta.", Toast.LENGTH_SHORT).show();
                            }
                        });

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