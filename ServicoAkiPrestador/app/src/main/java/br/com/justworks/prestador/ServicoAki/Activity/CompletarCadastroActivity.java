package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import br.com.justworks.prestador.ServicoAki.R;

public class CompletarCadastroActivity extends AppCompatActivity {

    /*
        Activity usada nos Fragments de completar cadastro, passo 1 a 9
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro);
        Toast.makeText(CompletarCadastroActivity.this, "Complete seu cadastro", Toast.LENGTH_SHORT).show();
    }
}