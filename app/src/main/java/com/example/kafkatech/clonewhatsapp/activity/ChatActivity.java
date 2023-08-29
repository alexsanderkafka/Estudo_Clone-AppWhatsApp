package com.example.kafkatech.clonewhatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CircleImageView imageView;
    private TextView nomeDoAmigo;
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
    }
}