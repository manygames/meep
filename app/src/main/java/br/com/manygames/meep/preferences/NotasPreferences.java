package br.com.manygames.meep.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class NotasPreferences {
    public static final String LAYOUT_ATUAL = "layoutAtual";
    private Context context;

    public NotasPreferences(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("br.com.manygames.meep.preferences.NotasPreferences", context.MODE_PRIVATE);
    }

    public void salvaLayout(Layouts layoutAtual){
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAYOUT_ATUAL, layoutAtual.id());
        editor.commit();
    }

    public int pegaLayout(){
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getInt(LAYOUT_ATUAL, Layouts.LINEAR.id());
    }
}
