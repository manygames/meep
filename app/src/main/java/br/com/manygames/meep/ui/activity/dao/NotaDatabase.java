package br.com.manygames.meep.ui.activity.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.manygames.meep.ui.activity.model.Nota;

@Database(entities = {Nota.class}, version = 1, exportSchema = false)
public abstract class NotaDatabase extends RoomDatabase{

    private static NotaDatabase INSTANCE;

    public abstract NotaDAO notaDAO();

    //Database
    private static final String DATABASE_NAME = "Nota";

    public static NotaDatabase getNotaDatabase(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NotaDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
