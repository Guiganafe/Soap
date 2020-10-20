package br.com.justworks.prestador.ServicoAki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button btn_continuar;
    private TextView tv_cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarComponentes();

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        tv_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastroIntent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(cadastroIntent);
                finish();
            }
        });
    }

    private void inicializarComponentes() {
        btn_continuar = (Button) findViewById(R.id.btn_continuar);
        tv_cadastro = (TextView) findViewById(R.id.tv_cadastro);
    }
}