package br.com.justworks.prestador.ServicoAki.Fragments;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Activity.MainActivity;
import br.com.justworks.prestador.ServicoAki.Adapter.SvgSoftwareLayerSetter;
import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.Description;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class ConfigurarServicos extends Fragment {

    private ImageView img_servico;
    private TextView tv_nome_servico;
    private TextView tv_nome_categoria;
    private TextView tv_custo_deslocamento;
    private EditText valor_servico, duracao_servico, custo_deslocamento, descricao_servico;
    private Spinner fornece_material, desloca_cliente;
    private Button salvar_Servico;
    private ServicoViewModel servicoViewModel;
    private RequestBuilder<PictureDrawable> requestBuilder;
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean material, desloca;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicoViewModel = new ViewModelProvider(requireActivity()).get(ServicoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configurar_servicos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        loadController();

        spinnerController();

        onClickController();

        maskController();
    }

    private void maskController() {
        duracao_servico.addTextChangedListener(MaskEditUtil.mask(duracao_servico, MaskEditUtil.FORMAT_HORA_MINUTOS));
    }

    private void spinnerController() {
        custo_deslocamento.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.sim_nao_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fornece_material.setAdapter(adapter);
        desloca_cliente.setAdapter(adapter);

        fornece_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    material = true;
                }else if(position == 2 || position == 0){
                    material = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        desloca_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    desloca = true;
                    tv_custo_deslocamento.setVisibility(View.VISIBLE);
                    custo_deslocamento.setVisibility(View.VISIBLE);
                }else if(position == 2 || position == 0){
                    desloca = false;
                    tv_custo_deslocamento.setVisibility(View.GONE);
                    custo_deslocamento.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onClickController() {
        salvar_Servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    addServico();
                }
            }
        });
    }

    private boolean validarCampos() {
        if(TextUtils.isEmpty(valor_servico.getText().toString())){
            valor_servico.setError("O valor do servico é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(duracao_servico.getText().toString())){
            duracao_servico.setError("A duração do servico é obrigatória");
            return false;
        } else if(TextUtils.equals(fornece_material.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione uma opção para o fornecimento de material", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.equals(desloca_cliente.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione uma opção para o deslocamento ao cliente", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.equals(desloca_cliente.getSelectedItem().toString(), "Sim") && TextUtils.isEmpty(custo_deslocamento.getText().toString())){
            custo_deslocamento.setError("O custo do deslocamento é obrigatório");
            return false;
        } else {
            return true;
        }
    }

    private void addServico() {
        String descricao, valor, custoDeslocamento;
        String duracao_txt = duracao_servico.getText().toString();
        int duracao;
        duracao = (Integer.parseInt(duracao_txt.substring(0,1)) * 60) + Integer.parseInt(duracao_txt.substring(3,4));

        valor = valor_servico.getText().toString();
        custoDeslocamento = custo_deslocamento.getText().toString();
        descricao = descricao_servico.getText().toString();

        Description description = new Description(descricao);

        Services service = servicoViewModel.getServices().getValue();

        final ServiceUser serviceUser = new ServiceUser();
        serviceUser.setAvgExecutionTime(duracao);
        serviceUser.setCategory(service.getCategory());
        serviceUser.setDescription(description);
        serviceUser.setId(servicoViewModel.getServiceId().getValue());
        serviceUser.setMoveToClient(desloca);
        if(desloca){
            serviceUser.setMovementCost(Double.parseDouble(custoDeslocamento));
        }
        serviceUser.setName(service.getName());
        serviceUser.setPrice(Double.parseDouble(valor));

        //servicoViewModel.addService(serviceUser);

        db.collection("users").document(userID).update("services", FieldValue.arrayUnion(serviceUser)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(requireActivity(), "Serviço adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                //UserBase.getInstance().addServiceUser(serviceUser);
                requireActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "Erro ao salvar o usuário", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadController() {
        Services service = servicoViewModel.getServices().getValue();
        CategoriesServices categoriesService = servicoViewModel.getCategoriesServices().getValue();

        requestBuilder = Glide.with(requireActivity()).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(categoriesService.getImageIconUrl().getSvg());
        requestBuilder
                .load(uri)
                .into(img_servico);

        tv_nome_servico.setText(service.getName().getPtbr());
        tv_nome_categoria.setText(categoriesService.getName().getPtbr());
    }

    private void inicializarComponentes(View view) {
        tv_custo_deslocamento = (TextView) view.findViewById(R.id.tv_custo_deslocamento_servicoConf);
        img_servico = (ImageView) view.findViewById(R.id.imageSelectedServiceConf);
        tv_nome_servico = (TextView) view.findViewById(R.id.tx_nome_selectedServiceConf);
        tv_nome_categoria = (TextView) view.findViewById(R.id.tx_nome_selectedCategoriaConf);
        valor_servico = (EditText) view.findViewById(R.id.edt_valor_servicoConf);
        duracao_servico = (EditText) view.findViewById(R.id.edt_duracao_servicoConf);
        custo_deslocamento = (EditText) view.findViewById(R.id.edt_custo_deslocamento_servicoConf);
        descricao_servico = (EditText) view.findViewById(R.id.edt_descricao_personalizada_servicoConf);
        fornece_material = (Spinner) view.findViewById(R.id.spinner_material_servicoConf);
        desloca_cliente = (Spinner) view.findViewById(R.id.spinner_deslocamento_servicoConf);
        salvar_Servico = (Button) view.findViewById(R.id.btn_salvar_servicoConf);
    }
}