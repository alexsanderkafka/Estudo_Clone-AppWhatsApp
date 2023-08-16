package com.example.kafkatech.clonewhatsapp.model;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class PessoaCadastro {

    private String nome;
    private String email;
    private String senha;
    private String id;

    public PessoaCadastro() {
    }

    public PessoaCadastro(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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
}
