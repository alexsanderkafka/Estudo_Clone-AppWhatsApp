package com.example.kafkatech.clonewhatsapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.AdapterView;

import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.activity.ChatActivity;
import com.example.kafkatech.clonewhatsapp.adapter.ConversasAdapter;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.RecyclerItemClickListener;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Conversa;
import com.example.kafkatech.clonewhatsapp.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversasFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConversasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewConversas;
    private List<Conversa> listConversas = new ArrayList<>();
    private ConversasAdapter adapter;
    private DatabaseReference databaseReference;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ConversasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerViewConversas = view.findViewById(R.id.recyclerViewConversas);

        //Configura o adapter
        adapter = new ConversasAdapter(listConversas, getActivity());
        //Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        String idUser = UsuarioFirebase.getidUsuario();
        databaseReference = ConfiguraFirebase.getFirebaseDataBase();
        conversasRef = databaseReference.child("conversas")
                .child(idUser);

        //Configura o evento de click no recycler view
        recyclerViewConversas.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewConversas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Conversa conversa = listConversas.get(position);
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra("chatContato", conversa.getUserExibicao());
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void pesquisarConversas(String texto){
        //Log.d("pesquisa", texto);

        List<Conversa> listaConversasBusca = new ArrayList<>();
        for(Conversa conversa : listConversas){
            //Pesquisa pelo msg e nome de exibicao
            String nome = conversa.getUserExibicao().getNome().toLowerCase();
            String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();
            if(nome.contains(texto) || ultimaMsg.contains(texto)){
                listaConversasBusca.add(conversa);
            }
        }

        adapter = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarConversas(){

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversa conversa = snapshot.getValue(Conversa.class);
                listConversas.add(conversa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void recarregaConversas(){
        adapter = new ConversasAdapter(listConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}