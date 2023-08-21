package com.example.kafkatech.clonewhatsapp.model;

import android.graphics.Bitmap;

public class Imagem {
    public Bitmap imagem;

    public Imagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }
}
