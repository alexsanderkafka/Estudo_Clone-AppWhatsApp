package com.example.kafkatech.clonewhatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.example.kafkatech.clonewhatsapp.helper.Permissao;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.PessoaCadastro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracaoActivity extends AppCompatActivity {

    private ImageButton imageButtonCam, imageButtonGallery;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALLERY = 200;
    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private CircleImageView imagePerfil;
    private StorageReference storageReference;

    private String identificadorUsuario;
    private DatabaseReference databaseReference;
    private EditText nomeUser;
    private ImageView buttonSalvarNome;
    private PessoaCadastro userLogado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        storageReference = ConfiguraFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getidUsuario();
        userLogado = UsuarioFirebase.getDadosUsuarioLogado();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configuração");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        imageButtonCam = findViewById(R.id.imageButtonCam);
        imageButtonGallery = findViewById(R.id.imageButtonGallery);
        imagePerfil = findViewById(R.id.imagePerfil);
        nomeUser = findViewById(R.id.editTextPerfilUser);
        buttonSalvarNome = findViewById(R.id.buttonSalvarNome);

        imageButtonCam.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) == null){
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        imageButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_GALLERY);
                }
            }
        });

        //Recupera dados do usuário
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();
        if(url != null){
            Glide.with(ConfiguracaoActivity.this)
                    .load(url)
                    .into(imagePerfil);
        }
        else{
            imagePerfil.setImageResource(R.drawable.padrao);
        }

        nomeUser.setText(usuario.getDisplayName());

        buttonSalvarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeUser.getText().toString();
                boolean retorno = UsuarioFirebase.atualizarNomeUser(nome);
                if(retorno){
                    userLogado.setNome(nome);
                    userLogado.atualizar();
                    Toast.makeText(ConfiguracaoActivity.this, "Nome alterado com sucesso", Toast.LENGTH_SHORT).show();
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
                    case SELECAO_GALLERY:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null){
                    imagePerfil.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    StorageReference imagensRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            //.child(identificadorUsuario)
                            .child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imagensRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracaoActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfiguracaoActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            imagensRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUser(url);
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

    private void atualizaFotoUser(Uri url) {
        boolean retorno = UsuarioFirebase.atualizarFotoUser(url);
        if(retorno){
            userLogado.setFoto(url.toString());
            userLogado.atualizar();
            Toast.makeText(this, "Sua foi alterada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abrirTela();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirTela(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}