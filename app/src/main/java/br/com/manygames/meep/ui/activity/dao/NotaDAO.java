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

    @Query("SELECT * FROM Nota")
    List<Nota> todas();

    @Insert
    public long[] insere(Nota... notas);

    @Update
    public void altera(Nota... notas);

    @Delete
    public void remove(Nota... notas);

    @Query("UPDATE Nota SET posicao = " +
            "CASE " +
                "WHEN posicao = :posicaoInicio THEN :posicaoFim " +
                "WHEN posicao = :posicaoFim    THEN :posicaoInicio " +
            "END " +
            "WHERE posicao IN (:posicaoInicio, :posicaoFim)")
    public void troca(int posicaoInicio, int posicaoFim);
}
