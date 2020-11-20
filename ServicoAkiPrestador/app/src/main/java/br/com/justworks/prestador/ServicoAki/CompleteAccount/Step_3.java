package br.com.justworks.prestador.ServicoAki.CompleteAccount;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;

import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class Step_3 extends Fragment {

    private Button btn_voltar_cadastro_step_2,  btn_avancar_cadastro_step_4_1;
    private Spinner spinner_tipo_doc;
    private EditText cpf, nome_mae, rg, orgao_emissor, data_emissao;
    private ProfissionalViewModel profissionalViewModel;
    private ProgressBar progressBar;

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
        maskController();
        textWatcherController();
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

        rg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setRg(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        orgao_emissor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setOrgao_emissor(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        data_emissao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setData_emissao(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void maskController() {
        cpf.addTextChangedListener(MaskEditUtil.mask(cpf, MaskEditUtil.FORMAT_CPF));
        data_emissao.addTextChangedListener(MaskEditUtil.mask(data_emissao, MaskEditUtil.FORMAT_DATE));
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
                if(validarCampos()) {
                    Navigation.findNavController(v).navigate(R.id.action_step_3_to_step_4);
                }
            }
        });
    }

    private boolean validarCampos() {
        if(TextUtils.isEmpty(cpf.getText().toString())){
            cpf.setError("O CPF é obrigatório");
            return false;
        } else if(!cpf.getText().toString().matches("^(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2})$")){
            cpf.setError("Insira um CPF válido");
            return false;
        }  else if(TextUtils.isEmpty(nome_mae.getText().toString())){
            nome_mae.setError("O nome da Mãe é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(rg.getText().toString())){
            rg.setError("O RG é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(orgao_emissor.getText().toString())){
            orgao_emissor.setError("O Orgão emissor é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(data_emissao.getText().toString())){
            data_emissao.setError("A Data de Emissão é obrigatória");
            return false;
        } else if(!data_emissao.getText().toString().matches("^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$")){
            data_emissao.setError("Insira uma Data de Emissão obrigatória");
            return false;
        } else {
            return true;
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_2 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_2);
        btn_avancar_cadastro_step_4_1 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_4_1);
        //spinner_tipo_doc = (Spinner) view.findViewById(R.id.spinner_tipo_doc_cadastro);
        nome_mae = (EditText) view.findViewById(R.id.edt_nome_mae_cadastro);
        cpf = (EditText) view.findViewById(R.id.edt_cpf_cadastro);
        rg = (EditText) view.findViewById(R.id.edt_rg_cadastro);
        orgao_emissor = (EditText) view.findViewById(R.id.edt_orgao_emissor_cadastro);
        data_emissao = (EditText) view.findViewById(R.id.edt_data_emissao_cadastro);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_step_3);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(45);
    }
}