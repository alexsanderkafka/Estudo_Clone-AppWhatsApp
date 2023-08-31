package com.example.kafkatech.clonewhatsapp.model;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.google.firebase.database.DatabaseReference;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private PessoaCadastro userExibicao;

    public Conversa() {
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

    public PessoaCadastro getUserExibicao() {
        return userExibicao;
    }

    public void setUserExibicao(PessoaCadastro userExibicao) {
        this.userExibicao = userExibicao;
    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguraFirebase.getFirebaseDataBase();
        DatabaseReference conversaRef = databaseReference.child("conversas");
        conversaRef.child(this.getIdRemetente())
                .child(this.getIdDestinatario())
                .setValue(this);
    }

}
