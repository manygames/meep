package br.com.manygames.meep.ui.activity.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "nota")
public class Nota implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String titulo;
    private String descricao;
    private int cor;
    private int posicao;

    public Nota() {
        this.titulo = "";
        this.descricao = "";
        this.cor = 0;
        this.posicao = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}