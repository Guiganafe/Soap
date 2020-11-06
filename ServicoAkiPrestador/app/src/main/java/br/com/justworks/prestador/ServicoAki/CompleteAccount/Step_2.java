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
    private Spinner sexo_spinner, estado_civil_spinner;
    private EditText rua, numero, complemento, bairro, cep, cidade, estado;
    private ProfissionalViewModel profissionalViewModel;
    private EndereçoViewModel endereçoViewModel;
    private SexoViewModel sexoViewModel;
    private EstadoCivilViewModel estadoCivilViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
        endereçoViewModel = new ViewModelProvider(requireActivity()).get(EndereçoViewModel.class);
        sexoViewModel = new ViewModelProvider(requireActivity()).get(SexoViewModel.class);
        estadoCivilViewModel = new ViewModelProvider(requireActivity()).get(EstadoCivilViewModel.class);
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
        spinnerController();
        placesController();
    }

    private void placesController() {
        Places.initialize(requireActivity(), "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        rua.setFocusable(false);
        rua.setOnClickListener(new View.OnClickListener() {
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
            List<AddressComponent> components = place.getAddressComponents().asList();
            rua.setText(place.getAddress());
//            List<String> rua = components.get(0).getTypes();
//            Toast.makeText(requireActivity(), "Tamanho: " + components.size(), Toast.LENGTH_LONG).show();
//            Toast.makeText(requireActivity(), "item 0: " + components.get(0), Toast.LENGTH_LONG).show();
//            Toast.makeText(requireActivity(), "item 0: " + components.get(1), Toast.LENGTH_LONG).show();
//            Toast.makeText(requireActivity(), "item 0: " + components.get(2), Toast.LENGTH_LONG).show();
//            Toast.makeText(requireActivity(), "item 0: " + components.get(3), Toast.LENGTH_LONG).show();
//            Toast.makeText(requireActivity(), "item 0: " + components.get(4), Toast.LENGTH_LONG).show();
//            Toast.makeText(requireActivity(), "item 0: " + components.get(5), Toast.LENGTH_LONG).show();

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
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
                if(validarCampos()){
                    Navigation.findNavController(v).navigate(R.id.action_step_2_to_step_3);
                }
            }
        });
    }

    private boolean validarCampos() {
        if(TextUtils.equals(sexo_spinner.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione o seu sexo", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.equals(estado_civil_spinner.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione o seu estado cívil", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.isEmpty(rua.getText().toString())){
            rua.setError("Insira uma Rua válida");
            return false;
        } else if (TextUtils.isEmpty(bairro.getText().toString())){
            bairro.setError("Insira um Bairro válido");
            return false;
        } else if (TextUtils.isEmpty(numero.getText().toString())){
            numero.setError("Insira um Número válido");
            return false;
        } else if(TextUtils.isEmpty(cep.getText().toString())){
            cep.setError("Insira um Cep válido");
            return false;
        } else if (TextUtils.isEmpty(cidade.getText().toString())){
            cidade.setError("Insira uma Cidade válida");
            return false;
        } else if (TextUtils.isEmpty(estado.getText().toString())){
            estado.setError("Insira um Estado válido");
            return false;
        } else {
            return true;
        }
    }

    private void maskController() {
        cep.addTextChangedListener(MaskEditUtil.mask(cep, MaskEditUtil.FORMAT_CEP));
    }

    private void textWatcherController() {
        rua.addTextChangedListener(new TextWatcher() {
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

        numero.addTextChangedListener(new TextWatcher() {
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

        complemento.addTextChangedListener(new TextWatcher() {
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

        bairro.addTextChangedListener(new TextWatcher() {
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

        cep.addTextChangedListener(new TextWatcher() {
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

        cidade.addTextChangedListener(new TextWatcher() {
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

        estado.addTextChangedListener(new TextWatcher() {
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

    private void spinnerController() {
        ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.sexo_array, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo_spinner.setAdapter(sexoAdapter);

        sexo_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sexo = sexo_spinner.getSelectedItem().toString();
                sexoViewModel.setSexoPtbR(sexo);

                if(position == 1){
                    sexoViewModel.setSexoEn("Male");
                } else if(position == 2){
                    sexoViewModel.setSexoEn("Female");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> estadoCivilAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.estado_civil_array, android.R.layout.simple_spinner_item);
        estadoCivilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estado_civil_spinner.setAdapter(estadoCivilAdapter);

        estado_civil_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String estado_civil = estado_civil_spinner.getSelectedItem().toString();
                estadoCivilViewModel.setEstadoCivilPtBr(estado_civil);
                if(position == 1){
                    estadoCivilViewModel.setEstadoCivilEn("Married");
                } else if(position == 2){
                    estadoCivilViewModel.setEstadoCivilEn("Single");
                } else if(position == 3){
                    estadoCivilViewModel.setEstadoCivilEn("Divorced");
                } else if(position == 4){
                    estadoCivilViewModel.setEstadoCivilEn("Widower");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_1 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_1);
        btn_avancar_cadastro_step_3 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_3);
        imagem_perfil = (ImageView) view.findViewById(R.id.imageSelectedProfile);
        nome_exibir = (TextView) view.findViewById(R.id.nome_exibir);
        email_exibir = (TextView) view.findViewById(R.id.email_exibir);
        sexo_spinner = (Spinner) view.findViewById(R.id.spinner_sexo_cadastro);
        estado_civil_spinner = (Spinner) view.findViewById(R.id.spinner_estado_civil_cadastro);
        rua = (EditText) view.findViewById(R.id.edt_rua_cadastro);
        numero = (EditText) view.findViewById(R.id.edt_numero_cadastro);
        complemento = (EditText) view.findViewById(R.id.edt_complemento_cadastro);
        bairro = (EditText) view.findViewById(R.id.edt_bairro_cadastro);
        cep = (EditText) view.findViewById(R.id.edt_cep_cadastro);
        cidade = (EditText) view.findViewById(R.id.edt_cidade_cadastro);
        estado = (EditText) view.findViewById(R.id.edt_estado_cadastro);
    }
}