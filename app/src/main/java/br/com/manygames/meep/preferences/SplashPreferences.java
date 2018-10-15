package br.com.manygames.meep.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SplashPreferences {

    public static final String PRIMEIRO_ACESSO = "ehPrimeiroAcesso";
    private Context context;

    public SplashPreferences(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("br.com.manygames.meep.preferences.NotasPreferences", context.MODE_PRIVATE);
    }

    public void salvaPrimeiroAcesso(){
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PRIMEIRO_ACESSO, false);
        editor.commit();
    }

    public boolean ehPrimeiroAcesso(){
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getBoolean(PRIMEIRO_ACESSO, true);
    }
}
