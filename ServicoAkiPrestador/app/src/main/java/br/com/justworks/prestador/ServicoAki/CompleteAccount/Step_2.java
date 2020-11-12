package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.EstadoCivilViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;
import br.com.justworks.prestador.ServicoAki.ViewModel.SexoViewModel;

public class Step_2 extends Fragment {

    private Button btn_voltar_cadastro_step_1,  btn_avancar_cadastro_step_3;
    private ImageView imagem_perfil;
    private TextView nome_exibir, email_exibir;
    private EditText edt_rua, edt_numero, edt_complemento, edt_bairro, edt_cep, edt_cidade, edt_estado;
    private ProfissionalViewModel profissionalViewModel;
    private EndereçoViewModel endereçoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
        endereçoViewModel = new ViewModelProvider(requireActivity()).get(EndereçoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarComponentes(view);
        maskController();
        onClick();
        textWatcherController();
        loadController();
        placesController();
    }

    private void placesController() {
        Places.initialize(requireActivity(), "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        edt_rua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS);
                Intent placesIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(requireContext());
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
            endereçoViewModel.setLatitude(place.getLatLng().latitude);
            endereçoViewModel.setLongitude(place.getLatLng().longitude);

            for (AddressComponent list_components: place.getAddressComponents().asList()) {

                String type = list_components.getTypes().get(0);

                if(TextUtils.equals(type, "route")){
                    edt_rua.setText(list_components.getName());
                } else if(TextUtils.equals(type, "country")){
                    endereçoViewModel.setPais(list_components.getName());
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
            Toast.makeText(requireActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClick() {
        btn_voltar_cadastro_step_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_avancar_cadastro_step_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(validarCampos()){
//                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_2_to_step_3);
                //}
            }
        });
    }

    private void enviarDados() {

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

    private void textWatcherController() {
        edt_rua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setRua(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_numero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setNumero(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_complemento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setComplemento(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_bairro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setBairro(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setCep(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_cidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setCidade(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_estado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endereçoViewModel.setEstado(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadController(){
        if(profissionalViewModel.getFoto_perfil().getValue() != null){
            imagem_perfil.setImageBitmap(profissionalViewModel.getFoto_perfil().getValue());
            imagem_perfil.setPadding(0,0,0,0);
        } else{
            imagem_perfil.setPadding(85,85,85,85);
            imagem_perfil.setImageResource(R.drawable.ic_person);
            imagem_perfil.setBackgroundResource(R.drawable.edit_text_gray);
        }
        if(profissionalViewModel.getNome_completo().getValue() != null){
            nome_exibir.setText(profissionalViewModel.getNome_completo().getValue());
        } else {
            nome_exibir.setText("Seu nome");
        }
        if(profissionalViewModel.getEmail().getValue() != null){
            email_exibir.setText(profissionalViewModel.getEmail().getValue());
        } else {
            nome_exibir.setText("Seu email");
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_1 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_1);
        btn_avancar_cadastro_step_3 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_3);
        imagem_perfil = (ImageView) view.findViewById(R.id.imageSelectedProfile);
        nome_exibir = (TextView) view.findViewById(R.id.nome_exibir);
        email_exibir = (TextView) view.findViewById(R.id.email_exibir);
        edt_rua = (EditText) view.findViewById(R.id.edt_rua_cadastro);
        edt_numero = (EditText) view.findViewById(R.id.edt_numero_cadastro);
        edt_complemento = (EditText) view.findViewById(R.id.edt_complemento_cadastro);
        edt_bairro = (EditText) view.findViewById(R.id.edt_bairro_cadastro);
        edt_cep = (EditText) view.findViewById(R.id.edt_cep_cadastro);
        edt_cidade = (EditText) view.findViewById(R.id.edt_cidade_cadastro);
        edt_estado = (EditText) view.findViewById(R.id.edt_estado_cadastro);

        edt_rua.setFocusable(false);
        edt_cidade.setFocusable(false);
        edt_estado.setFocusable(false);
    }
}