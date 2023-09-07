package com.example.kafkatech.clonewhatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.adapter.GrupoAdapterSelecionado;
import com.example.kafkatech.clonewhatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textViewNomeGrupo;
    private EditText editTextNomeGrupo;
    private CircleImageView imageGrupo;
    private RecyclerView recyclerMembrosSelecionados;
    private GrupoAdapterSelecionado grupoAdapterSelecionado;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);

        //Configura a toolbar
        toolbar = findViewById(R.id.toolbarGrupo);
        toolbar.setTitle("Novo grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        textViewNomeGrupo = findViewById(R.id.textTotalParticipantes);
        editTextNomeGrupo = findViewById(R.id.editNomeGrupo);
        imageGrupo = findViewById(R.id.circleImageGrupo);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosGrupo);

        //Recupera lista de membros passada
        if(getIntent().getExtras() != null){
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);
        }

        textViewNomeGrupo.setText("Participantes: " + listaMembrosSelecionados.size());

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
}