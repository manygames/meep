package br.com.manygames.meep.ui.activity.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.manygames.meep.ui.activity.model.Nota;

public class OldNotaDAO {

    private final static ArrayList<Nota> notas = new ArrayList<>();

    public List<Nota> todas() {
        return (List<Nota>) notas.clone();
    }

    public void insere(Nota... notas) {
        OldNotaDAO.notas.addAll(Arrays.asList(notas));
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
    }

    public void remove(int posicao) {
        notas.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(notas, posicaoInicio, posicaoFim);
    }
}
