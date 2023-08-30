package com.example.kafkatech.clonewhatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.adapter.MensagensAdapter;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.databinding.ActivityMainBinding;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Mensagem;
import com.example.kafkatech.clonewhatsapp.model.PessoaCadastro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CircleImageView imageView;
    private TextView nomeDoAmigo;
    private PessoaCadastro userDestinatario;
    private EditText editMensagem;
    private String idUserRemetente;
    private String idUserDestinatario;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagemList = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference msgRef;
    private ChildEventListener childEventListenerMensagens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageView = findViewById(R.id.circleImageFoto);
        nomeDoAmigo = findViewById(R.id.textViewNomeChat);
        fab = findViewById(R.id.floatingActionSend);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recupera dados do usuário destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userDestinatario = (PessoaCadastro) bundle.getSerializable("chatContato");
            nomeDoAmigo.setText(userDestinatario.getNome());

            String foto = userDestinatario.getFoto();
            if(foto != null){
                Uri url = Uri.parse(userDestinatario.getFoto());
                Glide.with(ChatActivity.this)
                        .load(url)
                        .into(imageView);
            }
            else{
                imageView.setImageResource(R.drawable.padrao);
            }
        }

        //Recupera os dados do usuario remente e destinatário
        idUserRemetente = UsuarioFirebase.getidUsuario();
        idUserDestinatario = CodeBase64.codeBase64(userDestinatario.getEmail());

        //Configurção adapter
        adapter = new MensagensAdapter(mensagemList, getApplicationContext());

        //Configuração recyclerview
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerMensagens.setLayoutManager(manager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        database = ConfiguraFirebase.getFirebaseDataBase();
        msgRef = database.child("mensagens")
                .child(idUserRemetente)
                .child(idUserDestinatario);
    }

    public void enviar(View view){
        String textMsg = editMensagem.getText().toString();
        if(!textMsg.isEmpty()){
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUser(idUserRemetente);
            mensagem.setMensagem(textMsg);

            //Salvar a mensagem para o remetente
            salvarMensagem(idUserRemetente, idUserDestinatario, mensagem);
        }
        else{
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();
        }
    }

    public void salvarMensagem(String idRementente, String idDestinatario, Mensagem mensagem){
        DatabaseReference reference = ConfiguraFirebase.getFirebaseDataBase();
        DatabaseReference mensagemRef = reference.child("mensagens");
        mensagemRef.child(idRementente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //Limpar o texto
        editMensagem.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        msgRef.removeEventListener(childEventListenerMensagens);
    }

    public void recuperarMensagens(){
        childEventListenerMensagens = msgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                mensagemList.add(mensagem);
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
}