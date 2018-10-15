package br.com.manygames.meep.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.manygames.meep.R;
import br.com.manygames.meep.preferences.SplashPreferences;

public class SplashScreenActivity extends AppCompatActivity {
    SplashPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferences = new SplashPreferences(this);

        Handler handler = new Handler();

        int delay;
        if(preferences.ehPrimeiroAcesso()){
            delay = 2000;
        } else {
            delay = 500;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciaApp();
            }
        }, delay);
    }

    private void iniciaApp() {
        Intent intent = new Intent(SplashScreenActivity.this, ListaNotasActivity.class);
        startActivity(intent);
        preferences.salvaPrimeiroAcesso();
        finish();
    }
}
