package br.com.justworks.prestador.ServicoAki.CreateAccount;

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
import android.widget.Spinner;
import android.widget.Toast;

import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class Step_3 extends Fragment {

    private Button btn_voltar_cadastro_step_2,  btn_avancar_cadastro_step_4_1;
    private Spinner spinner_tipo_doc;
    private EditText cpf, nome_mae;
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
        return inflater.inflate(R.layout.fragment_step_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarComponentes(view);

        onClick();
        spinnerController();
        textWatcherController();
    }

    private void spinnerController() {
        ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.tipo_doc_array, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_doc.setAdapter(sexoAdapter);

        spinner_tipo_doc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipo = spinner_tipo_doc.getSelectedItem().toString();
                profissionalViewModel.setDoc_tipo(tipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void textWatcherController() {
        cpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cpf = MaskEditUtil.unmask(s.toString());
                profissionalViewModel.setCpf(cpf);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nome_mae.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setNome_mae(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void maskController() {
        cpf.addTextChangedListener(MaskEditUtil.mask(cpf, MaskEditUtil.FORMAT_CPF));
    }

    private void onClick() {
        btn_voltar_cadastro_step_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_avancar_cadastro_step_4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(validarCampos()) {
                    Navigation.findNavController(v).navigate(R.id.action_step_3_to_step_4);
//                }
            }
        });
    }

    private boolean validarCampos() {
        if(TextUtils.isEmpty(cpf.getText().toString())){
            cpf.setError("Insira um CPF válido");
            return false;
        } else if(TextUtils.isEmpty(nome_mae.getText().toString())){
            nome_mae.setError("Insira o nome da sua mãe");
            return false;
        } else if(TextUtils.equals(spinner_tipo_doc.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione o documento", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_2 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_2);
        btn_avancar_cadastro_step_4_1 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_4_1);
        spinner_tipo_doc = (Spinner) view.findViewById(R.id.spinner_tipo_doc_cadastro);
        nome_mae = (EditText) view.findViewById(R.id.edt_nome_mae_cadastro);
        cpf = (EditText) view.findViewById(R.id.edt_cpf_cadastro);
    }
}