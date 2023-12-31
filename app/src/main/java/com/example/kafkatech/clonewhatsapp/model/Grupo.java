package com.example.kafkatech.clonewhatsapp.model;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.helper.CodeBase64;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {
    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {
        DatabaseReference databaseReference = ConfiguraFirebase.getFirebaseDataBase();
        DatabaseReference grupoRef = databaseReference.child("grupos");

        String idGrupoFirebase = grupoRef.push().getKey();
        setId(idGrupoFirebase);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguraFirebase.getFirebaseDataBase();
        DatabaseReference grupoRef = databaseReference.child("grupos");
        grupoRef.child(getId())
                .setValue(this);

        //Salvar conversa para membros do grupo
        for(Usuario membro : getMembros()){
            String idRemetente = CodeBase64.codeBase64(membro.getEmail());
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMensagem("");
            conversa.setIsGrupo("true");
            conversa.setGrupo(this);

            conversa.salvar();
        }
    }
}
