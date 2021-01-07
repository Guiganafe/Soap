package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.Arrays;
import java.util.List;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;

public class AdicionarEndereco extends AppCompatActivity {

    private Button btn_cancelar,  btn_salvar;
    private EditText edt_nome, edt_rua, edt_numero, edt_complemento, edt_bairro, edt_cep, edt_cidade, edt_estado;
    private String pais;
    private Double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_endereco);
        inicializarComponentes();
        placesController();
        onClick();
        maskController();
    }

    private void placesController() {
        Places.initialize(AdicionarEndereco.this, "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        edt_rua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS);
                Intent placesIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AdicionarEndereco.this);
                startActivityForResult(placesIntent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);

            /*
                Salva a latitude e longitude do endereço na ViewModel
             */
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

            for (AddressComponent list_components: place.getAddressComponents().asList()) {

                String type = list_components.getTypes().get(0);

                if(TextUtils.equals(type, "route")){
                    edt_rua.setText(list_components.getName());
                } else if(TextUtils.equals(type, "country")){
                    pais = list_components.getName();
                } else if(TextUtils.equals(type, "street_number")){
                    edt_numero.setText(list_components.getName());
                } else if(TextUtils.equals(type, "postal_code") || TextUtils.equals(type, "postal_code_prefix")){
                    edt_cep.setText(list_components.getName());
                } else if(TextUtils.equals(type, "sublocality") || TextUtils.equals(type, "sublocality_level_1")){
                    edt_bairro.setText(list_components.getName());
                } else if(TextUtils.equals(type, "administrative_area_level_2") || TextUtils.equals(type, "locality")){
                    edt_cidade.setText(list_components.getName());
                } else if(TextUtils.equals(type, "administrative_area_level_1")){
                    edt_estado.setText(list_components.getShortName());
                }
            }

            edt_numero.setFocusable(true);
            edt_bairro.setFocusable(true);
            edt_cep.setFocusable(true);

        } else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(AdicionarEndereco.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClick() {
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    salvarEndereco();
                }
            }
        });
    }

    private void salvarEndereco() {
        Boolean active;
        String addressName, addressType, city, country, neighborhood, number, state, street, userId, zipCode;
        double latitude, longitude;

        active = true;
        addressName = "Endereço";
        addressType = edt_nome.getText().toString();
        city = edt_cidade.getText().toString();
        country = this.pais;
        neighborhood = edt_bairro.getText().toString();
        number = edt_numero.getText().toString();
        state = edt_estado.getText().toString();
        userId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
        street = edt_rua.getText().toString();
        zipCode = edt_cep.getText().toString();
        longitude = this.longitude;
        latitude = this.latitude;

        final Address address = new Address(active, addressName, addressType, city, country, neighborhood, number, state, street, userId , zipCode, latitude, longitude);

        sendAddress(address);

        finish();
    }

    private Task<String> sendAddress(Address address) {
        FirebaseFunctions functions = FirebaseFunctions.getInstance();

        return functions
                .getHttpsCallable("users-setAddress")
                .call(address.toMap())
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        Toast.makeText(AdicionarEndereco.this, "Endereço enviado", Toast.LENGTH_SHORT).show();
                        return result;
                    }
                });
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
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar_novo_endereco);
        btn_salvar = (Button) findViewById(R.id.btn_salvar_novo_endereco);
        edt_nome = (EditText) findViewById(R.id.edt_nome_endereco); 
        edt_rua = (EditText) findViewById(R.id.edt_rua_endereco);
        edt_numero = (EditText) findViewById(R.id.edt_numero_endereco);
        edt_complemento = (EditText) findViewById(R.id.edt_complemento_endereco);
        edt_bairro = (EditText) findViewById(R.id.edt_bairro_endereco);
        edt_cep = (EditText) findViewById(R.id.edt_cep_endereco);
        edt_cidade = (EditText) findViewById(R.id.edt_cidade_endereco);
        edt_estado = (EditText) findViewById(R.id.edt_estado_endereco);
        edt_rua.setFocusable(false);
        edt_cidade.setFocusable(false);
        edt_estado.setFocusable(false);
    }
}