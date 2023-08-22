package com.example.kafkatech.clonewhatsapp.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.model.PessoaCadastro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

public class UsuarioFirebase {
    public static String getidUsuario(){
        FirebaseAuth usuario = ConfiguraFirebase.getFirebaseAuth();
        String email = usuario.getCurrentUser().getEmail();
        String idUser = CodeBase64.codeBase64(email);
        return idUser;
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguraFirebase.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

    public static  boolean atualizarFotoUser(Uri url){
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest
                .Builder()
                .setPhotoUri(url)
                .build();
        try{
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                    }
                }
            });
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static  boolean atualizarNomeUser(String name){
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest
                .Builder()
                .setDisplayName(name)
                .build();
        try{
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static PessoaCadastro getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();
        PessoaCadastro user = new PessoaCadastro();
        user.setEmail(firebaseUser.getEmail());
        user.setNome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            user.setFoto("");
        }
        else{
            user.setFoto(firebaseUser.getPhotoUrl().toString());
        }
        return user;
    }
}
