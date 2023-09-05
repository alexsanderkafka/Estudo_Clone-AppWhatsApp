package com.example.kafkatech.clonewhatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.adapter.ContatosAdapter;
import com.example.kafkatech.clonewhatsapp.adapter.GrupoAdapterSelecionado;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.RecyclerItemClickListener;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GrupoActivity extends AppCompatActivity {
    private RecyclerView recyclerMembrosSelecionados, recyclerMembros;
    private ContatosAdapter contatosAdapter;
    private GrupoAdapterSelecionado grupoAdapterSelecionado;
    private List<Usuario> listaMembros = new ArrayList<>();
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        //Configura a toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGrupo);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        recyclerMembros = findViewById(R.id.recyclerMembros);

        usuariosRef = ConfiguraFirebase.getFirebaseDataBase().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //Configura Adapter
        contatosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());

        //Configura recyclerview para contatos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(contatosAdapter);

        //Event of click
        recyclerMembros.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMembros,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario userSelecioando = listaMembros.get(position);

                                //Remover usuario selecionado da lista
                                listaMembros.remove(userSelecioando);
                                contatosAdapter.notifyDataSetChanged();

                                //Adiciona usuario na nova lista de selecionados
                                listaMembrosSelecionados.add(userSelecioando);
                                grupoAdapterSelecionado.notifyDataSetChanged();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        //Configura recyclerview para selecionados

        //Config Adapter
        grupoAdapterSelecionado = new GrupoAdapterSelecionado(listaMembrosSelecionados, getApplicationContext());

        //Configura recyclerview
        RecyclerView.LayoutManager layoutManagerHotizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHotizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoAdapterSelecionado);

    }

    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerMembros);
    }

    public void recuperarContatos(){
        valueEventListenerMembros = usuariosRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados : snapshot.getChildren()){
                    Usuario user = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = user.getEmail();

                    if(!Objects.equals(usuarioAtual.getEmail(), emailUsuarioAtual)){
                        listaMembros.add(user);
                    }
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}