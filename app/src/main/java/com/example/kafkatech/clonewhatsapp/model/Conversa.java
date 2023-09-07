package com.example.kafkatech.clonewhatsapp.model;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.google.firebase.database.DatabaseReference;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario userExibicao;
    private String isGrupo;
    private Grupo grupo;

    public Conversa() {
        this.setIsGrupo("false");
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUserExibicao() {
        return userExibicao;
    }

    public void setUserExibicao(Usuario userExibicao) {
        this.userExibicao = userExibicao;
    }

    public String getIsGrupo() {
        return isGrupo;
    }

    public void setIsGrupo(String isGrupo) {
        this.isGrupo = isGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguraFirebase.getFirebaseDataBase();
        DatabaseReference conversaRef = databaseReference.child("conversas");
        conversaRef.child(this.getIdRemetente())
                .child(this.getIdDestinatario())
                .setValue(this);
    }

}
