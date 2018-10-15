package br.com.manygames.meep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.manygames.meep.R;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaCoresAdapter;

import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {
    public static final String TITULO_APPBAR_INSERE = "Insere Nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera Nota";
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;
    private Nota nota;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITULO_APPBAR_INSERE);
        setContentView(R.layout.activity_formulario_nota);

        inicializaCampos();

        Intent dadosRecebidos = getIntent();
        inicializaNota(dadosRecebidos);

        preencheCampos();

        configuraAdapter();
    }

    private void configuraAdapter() {
        //inicio - criando seletor de cores

        RecyclerView listaCores = findViewById(R.id.formulario_nota_lista_cores);

        ArrayList<Integer> cores = new ArrayList<Integer>();
        cores.add(Color.GREEN);
        cores.add(Color.WHITE);
        cores.add(Color.RED);
        cores.add(Color.BLUE);
        cores.add(Color.YELLOW);
        cores.add(Color.MAGENTA);
        cores.add(Color.GRAY);
        cores.add(Color.CYAN);

        ListaCoresAdapter adapter = new ListaCoresAdapter(this, cores);
        listaCores.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListaCoresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int cor, int position) {
                setLayoutColor(cor);
                nota.setCor(cor);
            }
        });

        //fim
    }

    private void setLayoutColor(int cor) {
        layout.setBackgroundColor(cor);
    }

    private void inicializaNota(Intent dadosRecebidos) {
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle(TITULO_APPBAR_ALTERA);
            nota = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
        } else {
            nota = new Nota();
        }
    }

    private void preencheCampos() {
        titulo.setText(nota.getTitulo());
        descricao.setText(nota.getDescricao());
        setLayoutColor(nota.getCor());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
        layout = findViewById(R.id.formulario_nota_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_salva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ehMenuSalvaNota(item)){
            nota = criaNota();
            retornaNota();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota() {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private Nota criaNota() {
        ColorDrawable background = (ColorDrawable) layout.getBackground();
        return new Nota(titulo.getText().toString(), descricao.getText().toString(), background.getColor());
    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return R.id.menu_formulario_ic_salva == item.getItemId();
    }
}
