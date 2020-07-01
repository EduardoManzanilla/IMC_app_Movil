package com.perlaaguileta.imc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestablecerPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestablecerPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText correo;

    private FirebaseAuth auth;
    private DatabaseReference database;

    private String email= "";

    AlertDialog.Builder builder;
    AlertDialog dialog;

    public RestablecerPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestablecerPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestablecerPasswordFragment newInstance(String param1, String param2) {
        RestablecerPasswordFragment fragment = new RestablecerPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_restablecer_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        correo = view.findViewById(R.id.Res_correo);

        database = FirebaseDatabase.getInstance().getReference();

        final NavController navController = Navigation.findNavController(view);
        Button restablecer = view.findViewById(R.id.btn_restablecer);

        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               email = correo.getText().toString();
               if (!email.isEmpty()){
                   dialog = new SpotsDialog.Builder()
                           .setContext(getActivity())
                           .setMessage("Espere un momento por favor...")
                           .setCancelable(false)
                           .build();
                   dialog.show();

                   Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           dialog.dismiss();
                           resetPassword();
                       }
                   }, 4000);


               }else{
                   Toast.makeText(getContext() , "Debe ingresar el correo.", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private void resetPassword(){
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getContext() , "Se ha enviado un correo para restablecer su contraseña.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext() , "No se pudo enviar el correo para restablecer su contraseña.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}