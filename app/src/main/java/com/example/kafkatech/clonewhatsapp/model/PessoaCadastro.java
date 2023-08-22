package com.example.kafkatech.clonewhatsapp.model;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PessoaCadastro {

    private String nome;
    private String email;
    private String senha;
    private String id;
    private String foto;

    public PessoaCadastro() {
    }

    public PessoaCadastro(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void salvar(){
        DatabaseReference    databaseReference = ConfiguraFirebase.getFirebaseDataBase();
        databaseReference.child("usuarios")
                .child(getId()).setValue(this);
    }

    public void atualizar(){
        String idUser = UsuarioFirebase.getidUsuario();
        DatabaseReference firebaseDatabase = ConfiguraFirebase.getFirebaseDataBase();

        DatabaseReference usuariosRef = firebaseDatabase
                .child("usuarios")
                .child(idUser);

        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren(valoresUsuario);
    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("photo", getFoto());
        return usuarioMap;
    }
}
