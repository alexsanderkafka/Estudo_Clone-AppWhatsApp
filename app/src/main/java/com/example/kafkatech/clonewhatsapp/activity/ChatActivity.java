package com.example.kafkatech.clonewhatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.databinding.ActivityMainBinding;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Mensagem;
import com.example.kafkatech.clonewhatsapp.model.PessoaCadastro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CircleImageView imageView;
    private TextView nomeDoAmigo;
    private PessoaCadastro userDestinatario;
    private EditText editMensagem;
    private String idUserRemetente;
    private String idUserDestinatario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageView = findViewById(R.id.circleImageFoto);
        nomeDoAmigo = findViewById(R.id.textViewNomeChat);
        fab = findViewById(R.id.floatingActionSend);
        editMensagem = findViewById(R.id.editMensagem);

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
}