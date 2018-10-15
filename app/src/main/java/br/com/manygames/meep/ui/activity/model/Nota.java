package br.com.manygames.meep.ui.activity.model;

import java.io.Serializable;

public class Nota implements Serializable {

    private final String titulo;
    private final String descricao;
    private int cor;

    public Nota(String titulo, String descricao, int cor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.cor = cor;
    }

    public Nota(){
        this.titulo = "";
        this.descricao = "";
        this.cor = 0;
    }

    public String getTitulo() { return titulo; }

    public String getDescricao() { return descricao; }

    public int getCor() { return cor; }

    public void setCor(int cor) {
        this.cor = cor;
    }
}