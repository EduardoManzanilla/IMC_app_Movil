package com.perlaaguileta.imc;

import android.app.Dialog;
import android.app.ProgressDialog;
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

    EditText contraseña, correo;
    String pass, email;

    private Dialog dialog;
    private FirebaseAuth auth;
    private DatabaseReference database;

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
        dialog= new Dialog(getActivity());
        auth = FirebaseAuth.getInstance();

        contraseña = view.findViewById(R.id.Res_password);
        correo = view.findViewById(R.id.Res_correo);

        database = FirebaseDatabase.getInstance().getReference();

        final NavController navController = Navigation.findNavController(view);
        final Button restablecer = view.findViewById(R.id.btn_restablecer);
        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassword();
            }
        });
    }

    private void resetpassword(){
         pass = contraseña.getText().toString();
         email= correo.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            final String uid = user.getUid();
            database.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        Map<String, Object> personaMap = new HashMap<>();

                        personaMap.put("Contraseña", pass);


                        database.child("users").child(uid).updateChildren(personaMap);
                        Toast.makeText(getActivity() , "Actualización Exitosa.", Toast.LENGTH_SHORT).show();
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