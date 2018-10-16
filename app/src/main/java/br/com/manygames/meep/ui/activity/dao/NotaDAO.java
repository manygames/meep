package br.com.manygames.meep.ui.activity.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.manygames.meep.ui.activity.model.Nota;

@Dao
public interface NotaDAO {

    @Query("SELECT * FROM Nota ORDER BY posicao")
    List<Nota> todas();

    @Insert
    long[] insere(Nota... notas);

    @Update
    void altera(Nota... notas);

    @Delete
    void remove(Nota... notas);

    @Query("UPDATE Nota SET posicao = posicao + 1")
    void atualizaPosicoesAposInserir();

    @Query("UPDATE Nota SET posicao = posicao - 1 WHERE posicao > :posicao")
    void atualizaPosicoesAposRemover(int posicao);
}
