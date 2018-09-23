package br.com.manygames.meep.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.manygames.meep.R;
import br.com.manygames.meep.ui.activity.dao.NotaDAO;
import br.com.manygames.meep.ui.activity.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_salva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.menu_formulario_ic_salva == item.getItemId()){
            EditText titulo = findViewById(R.id.formulario_nota_titulo);
            EditText descricao = findViewById(R.id.formulario_nota_descricao);
            Nota nota = new Nota(titulo.getText().toString(), descricao.getText().toString());

            Intent resultadoInsercao = new Intent();
            resultadoInsercao.putExtra("nota", nota);
            setResult(2, resultadoInsercao);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
