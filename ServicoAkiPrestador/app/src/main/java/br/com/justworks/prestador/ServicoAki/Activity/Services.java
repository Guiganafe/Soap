package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import br.com.justworks.prestador.ServicoAki.R;

public class Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Intent intent = getIntent();
        String id_categoria = intent.getStringExtra("id_categoria");
        Toast.makeText(this, "id: " + id_categoria, Toast.LENGTH_SHORT).show();
    }
}