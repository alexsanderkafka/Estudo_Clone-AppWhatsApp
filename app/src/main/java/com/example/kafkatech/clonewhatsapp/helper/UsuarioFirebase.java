package com.example.kafkatech.clonewhatsapp.helper;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioFirebase {
    public static String getidUsuario(){
        FirebaseAuth usuario = ConfiguraFirebase.getFirebaseAuth();
        String email = usuario.getCurrentUser().getEmail();
        String idUser = CodeBase64.codeBase64(email);
        return idUser;
    }
}
