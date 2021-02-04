package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.com.justworks.prestador.ServicoAki.R;

public class AdicionarServico extends AppCompatActivity {

    /*
        Activity usada nos Fragments: SelecionarCategoria -> SelecionarServiço -> ConfigurarServiço
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_servico);
    }
}