package com.example.kafkatech.clonewhatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.databinding.ActivityMainBinding;
import com.example.kafkatech.clonewhatsapp.model.PessoaCadastro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CircleImageView imageView;
    private TextView nomeDoAmigo;
    private PessoaCadastro userDestinatario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageView = findViewById(R.id.circleImageFoto);
        nomeDoAmigo = findViewById(R.id.textViewNomeChat);

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
    }
}