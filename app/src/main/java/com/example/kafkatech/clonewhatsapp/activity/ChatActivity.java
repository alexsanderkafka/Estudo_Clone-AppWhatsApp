package com.example.kafkatech.clonewhatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.adapter.MensagensAdapter;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Conversa;
import com.example.kafkatech.clonewhatsapp.model.Mensagem;
import com.example.kafkatech.clonewhatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CircleImageView imageView;
    private TextView nomeDoAmigo;
    private Usuario userDestinatario;
    private EditText editMensagem;
    private String idUserRemetente;
    private String idUserDestinatario;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagemList = new ArrayList<>();
    private static final int SELECAO_CAMERA = 100;
    private DatabaseReference database;
    private DatabaseReference msgRef;
    private ChildEventListener childEventListenerMensagens;
    private ImageView imageCamera;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageView = findViewById(R.id.circleImageFoto);
        nomeDoAmigo = findViewById(R.id.textViewNomeChat);
        fab = findViewById(R.id.floatingActionSend);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.buttonSendPhoto);

        storageReference = ConfiguraFirebase.getFirebaseStorage();

        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recupera dados do usuário destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userDestinatario = (Usuario) bundle.getSerializable("chatContato");
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

        //Evento de clique para enviar uma chat
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) == null){
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch(requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if(imagem != null) {
                    //Recupera dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();
                    //Configura referencia do storage
                    StorageReference imagemRef = storageReference.child("imagens")
                            .child("fotos")
                            .child(idUserRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload");
                            Toast.makeText(ChatActivity.this, "Erro ao enviar mensagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String url = task.getResult().toString();
                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUser(idUserRemetente);
                                    mensagem.setMensagem("imagem.jpg");
                                    mensagem.setFoto(url);

                                    //Salva a mensagem remetente
                                    salvarMensagem(idUserRemetente, idUserDestinatario, mensagem);

                                    //Salva a mensagem destinatario
                                    salvarMensagem(idUserDestinatario, idUserRemetente, mensagem);

                                    Toast.makeText(ChatActivity.this, "Sucesso ao enviar imagem", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void enviar(View view){
        String textMsg = editMensagem.getText().toString();
        if(!textMsg.isEmpty()){
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUser(idUserRemetente);
            mensagem.setMensagem(textMsg);

            //Salvar a mensagem para o remetente
            salvarMensagem(idUserRemetente, idUserDestinatario, mensagem);

            //Salvar a mensagem para o destinatario
            salvarMensagem(idUserDestinatario, idUserRemetente, mensagem);

            //Salva uma conversa em um novo nó no firebase
            salvarConversa(mensagem);

        }
        else{
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarConversa(Mensagem mensagem) {
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idUserRemetente);
        conversaRemetente.setIdDestinatario(idUserDestinatario);
        conversaRemetente.setUltimaMensagem(mensagem.getMensagem());
        conversaRemetente.setUserExibicao(userDestinatario);

        conversaRemetente.salvar();
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