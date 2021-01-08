
package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Base.EnderecoBase;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class EditarEndereco extends AppCompatActivity {

    private Button btn_excluir,  btn_salvar;
    private EditText edt_nome, edt_rua, edt_numero, edt_complemento, edt_bairro, edt_cep, edt_cidade, edt_estado;
    private String pais;
    private Double longitude, latitude;
    private int position;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_endereco);
        inicializarComponentes();
        maskController();

        Intent intent = getIntent();

        position = intent.getIntExtra("position", 0);

        address = EnderecoBase.getInstance().getAddressItemsList().get(position);

        loadController();

    }

    private void loadController() {

        String addressName = address.getAddressName(), addressType = address.getAddressType(), city = address.getCity(),
                country = address.getCountry(), neighborhood = address.getNeighborhood(), number = address.getNumber(),
                state = address.getState(), street = address.getStreet(), userId = address.getUserId(),
                zipCode = address.getZipCode(), complement;
        double latitude = address.getLatitude(), longitude = address.getLongitude();

        if(street != null && !street.equals("")){
            edt_nome.setText(addressType);
        }
        if(city != null && !city.equals("")){
            edt_rua.setText(addressType);
        }
        if(number != null && !number.equals("")){
            edt_numero.setText(addressType);
        }
        if(number != null && !number.equals("")){
            edt_complemento.setText(addressType);
        }


    }

    private boolean validarCampos() {
        if(TextUtils.isEmpty(edt_rua.getText().toString())){
            edt_rua.setError("Insira uma Rua válida");
            return false;
        } else if (TextUtils.isEmpty(edt_bairro.getText().toString())){
            edt_bairro.setError("Insira um Bairro válido");
            return false;
        } else if (TextUtils.isEmpty(edt_numero.getText().toString())){
            edt_numero.setError("Insira um Número válido");
            return false;
        } else if(TextUtils.isEmpty(edt_cep.getText().toString())){
            edt_cep.setError("Insira um Cep válido");
            return false;
        } else if (TextUtils.isEmpty(edt_cidade.getText().toString())){
            edt_cidade.setError("Insira uma Cidade válida");
            return false;
        } else if (TextUtils.isEmpty(edt_estado.getText().toString())){
            edt_estado.setError("Insira um Estado válido");
            return false;
        } else {
            return true;
        }
    }

    private void maskController() {
        edt_cep.addTextChangedListener(MaskEditUtil.mask(edt_cep, MaskEditUtil.FORMAT_CEP));
    }


    private void inicializarComponentes() {
        btn_excluir = (Button) findViewById(R.id.btn_excluir_endereco_edicao);
        btn_salvar = (Button) findViewById(R.id.btn_salvar_endereco_edicao);
        edt_nome = (EditText) findViewById(R.id.edt_nome_endereco_edicao);
        edt_rua = (EditText) findViewById(R.id.edt_rua_endereco_edicao);
        edt_numero = (EditText) findViewById(R.id.edt_numero_endereco_edicao);
        edt_complemento = (EditText) findViewById(R.id.edt_complemento_endereco_edicao);
        edt_bairro = (EditText) findViewById(R.id.edt_bairro_endereco_edicao);
        edt_cep = (EditText) findViewById(R.id.edt_cep_endereco_edicao);
        edt_cidade = (EditText) findViewById(R.id.edt_cidade_endereco_edicao);
        edt_estado = (EditText) findViewById(R.id.edt_estado_endereco_edicao);
        edt_rua.setFocusable(false);
        edt_cidade.setFocusable(false);
        edt_estado.setFocusable(false);
    }
}