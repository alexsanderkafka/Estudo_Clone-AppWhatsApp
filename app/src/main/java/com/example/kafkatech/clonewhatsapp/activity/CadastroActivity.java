package com.example.kafkatech.clonewhatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha;
    private Button buttonCadastro;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        buttonCadastro = findViewById(R.id.buttonCadastro);

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editNome.getText().toString();
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if(!nome.isEmpty()){
                    if(!email.isEmpty()){
                        if(!senha.isEmpty()){
                            Usuario user = new Usuario(nome, email, senha);
                            cadastrarUsuario(user);
                        }
                        else{
                            Toast.makeText(CadastroActivity.this, "Preencha o campo senha", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(CadastroActivity.this, "Preencha o campo E-mail", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(CadastroActivity.this, "Preencha o campo nome", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void vaiParaTelaMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void cadastrarUsuario(Usuario user){
        auth = ConfiguraFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String excecao = "";
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                    UsuarioFirebase.atualizarNomeUser(user.getNome());

                    try {
                        String emailCode = CodeBase64.codeBase64(user.getEmail());
                        user.setId(emailCode);
                        user.salvar();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar user", Toast.LENGTH_SHORT).show();
                    vaiParaTelaMain();
                }
                else{
                    try {
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido!";
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    }
                    catch(Exception e){
                        excecao = "Erro ao cadastrar usuário " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}