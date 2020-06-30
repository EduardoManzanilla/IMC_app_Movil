package com.perlaaguileta.imc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link informeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class informeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference reference;
    FirebaseAuth auth;
    RecyclerView recyView;
    ArrayList<Avances> arrayH;
    Adaptador adapter;

    String imc, clasi, reco, imagen;



    public informeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment informeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static informeFragment newInstance(String param1, String param2) {
        informeFragment fragment = new informeFragment();
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
        return inflater.inflate(R.layout.fragment_informe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id = null;
        recyView = (RecyclerView) view.findViewById(R.id.recycleAvance);
        recyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayH = new ArrayList<Avances>();
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user!=null) {
            id = user.getUid();

            reference.child("users").child(id).child("Avances").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()){
                       // Avances a = snap.getValue(Avances.class);
                        imc = snap.child("IMC").getValue().toString();
                        clasi = snap.child("Clasificación").getValue().toString();
                        reco = snap.child("Recomendación").getValue().toString();
                        imagen = snap.child("image").getValue().toString();
                        Avances a = new Avances(imc,clasi,reco,imagen);
                        arrayH.add(a);
                    }
                    adapter = new Adaptador(getActivity(),arrayH);
                    recyView.setAdapter(adapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}