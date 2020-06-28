package com.perlaaguileta.imc;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth authlogin;
    EditText user, password;
    private  String emali = "";
    private String pass="";

    NavController navController;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public loginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loginFragment newInstance(String param1, String param2) {
        loginFragment fragment = new loginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        authlogin = FirebaseAuth.getInstance();

        user = view.findViewById(R.id.edtUsuario);
        password = view.findViewById(R.id.edtPassword);

        Button log = view.findViewById(R.id.btn_sesion);
        TextView registrar = view.findViewById(R.id.textRegistrar);
        TextView restablecer = view.findViewById(R.id.textOlvidar);

        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_restablecerPasswordFragment);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emali = user.getText().toString();
                pass = password.getText().toString();
                if (!emali.isEmpty() && !pass.isEmpty()) {

                    loginUser();

                }else{
                    Toast.makeText(getActivity() , "Se requiere que acomplete los campos.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registrarFragment);
            }
        });

    }

    private void loginUser (){
        authlogin.signInWithEmailAndPassword(emali, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    navController.navigate(R.id.action_loginFragment_to_controlFragment);
                    Toast.makeText(getActivity() , "¡Bienvenido!.", Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(getActivity() , "El usuario y contraseña no coinciden. Verifique sus datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (authlogin.getCurrentUser()!=null){
            navController.navigate(R.id.action_loginFragment_to_controlFragment);
        }
    }
}