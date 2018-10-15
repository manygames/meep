package br.com.manygames.meep.ui.recyclerview.adapter;

import android.graphics.Color;

public enum Cor {
    AZUL(Color.BLUE), BRANCO(Color.WHITE), VERMELHO(Color.RED), VERDE(Color.GREEN);

    private int id;
    private String hexa;

    Cor(int id, String hash) {
        this.id = id;
        this.hexa = hash;
    }

    Cor(int green) {

    }

    private int id(){
        return id;
    }

    public String hexa(){
        return hexa;
    }

    public static String pegaHash(int id){
        for (Cor c:
             Cor.values()) {
            if(id == c.id())
                return c.hexa;
        }
        throw new IllegalArgumentException();
    }
}
