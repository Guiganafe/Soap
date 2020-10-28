package br.com.justworks.prestador.ServicoAki.CreateAccount;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
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

import br.com.justworks.prestador.ServicoAki.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class Step_2 extends Fragment {

    private Button btn_voltar_cadastro_step_1,  btn_avancar_cadastro_step_3;
    private ImageView imagem_perfil;
    private TextView nome_exibir, email_exibir;
    private Spinner sexo_spinner, estado_civil_spinner;
    private EditText rua, numero, complemento, bairro, cep, cidade, estado;
    private ProfissionalViewModel profissionalViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
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
                Navigation.findNavController(v).navigate(R.id.action_step_2_to_step_3);
            }
        });
    }

    private void maskController() {
        cep.addTextChangedListener(MaskEditUtil.mask(cep, MaskEditUtil.FORMAT_CEP));
    }

    private void textWatcherController() {

    }

    private void loadController(){
        if(profissionalViewModel.getFoto_perfil().getValue() != null){
            imagem_perfil.setImageBitmap(profissionalViewModel.getFoto_perfil().getValue());
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
                profissionalViewModel.setSexo(sexo);
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
                profissionalViewModel.setSexo(estado_civil);
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