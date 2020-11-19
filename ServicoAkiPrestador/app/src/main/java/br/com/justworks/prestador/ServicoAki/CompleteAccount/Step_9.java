package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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

import br.com.justworks.prestador.ServicoAki.Adapter.SvgSoftwareLayerSetter;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.Description;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class Step_9 extends Fragment {

    private ImageView img_servico;
    private TextView tv_nome_servico;
    private TextView tv_nome_categoria;
    private EditText valor_servico, duracao_servico, custo_deslocamento, descricao_servico;
    private Spinner fornece_material, desloca_cliente;
    private Button salvar_Servico;
    private ServicoViewModel servicoViewModel;
    private RequestBuilder<PictureDrawable> requestBuilder;

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
        return inflater.inflate(R.layout.fragment_step_9, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        loadController();

        spinnerController();

        onClickController();
    }

    private void spinnerController() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.sim_nao_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fornece_material.setAdapter(adapter);
        desloca_cliente.setAdapter(adapter);

        fornece_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    material = true;
                }else if(position == 2){
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
                    material = true;
                }else if(position == 2){
                    material = false;
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
                addServico();
            }
        });
    }

    private void addServico() {
        String descricao;
        int duracao, custoDeslocamento, valor;

        valor = Integer.parseInt(valor_servico.getText().toString());
        duracao = Integer.parseInt(duracao_servico.getText().toString());
        custoDeslocamento = Integer.parseInt(custo_deslocamento.getText().toString());
        descricao = descricao_servico.getText().toString();

        Description description = new Description(descricao);

        Services service = servicoViewModel.getServices().getValue();

        ServiceUser serviceUser = new ServiceUser();
        serviceUser.setAvgExecutionTime(duracao);
        serviceUser.setCategory(service.getCategory());
        serviceUser.setDescription(description);
        serviceUser.setId(servicoViewModel.getServiceId().getValue());
        serviceUser.setMoveToClient(material);
        serviceUser.setMovementCost(custoDeslocamento);
        serviceUser.setName(service.getName());
        serviceUser.setPrice(valor);

        servicoViewModel.addService(serviceUser);
        Toast.makeText(requireActivity(), "Serviço adicionado com sucesso!", Toast.LENGTH_SHORT).show();
        Toast.makeText(requireActivity(), "Tamanho da lista" + servicoViewModel.getServices_list().getValue().size(), Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
        getActivity().onBackPressed();
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
        img_servico = (ImageView) view.findViewById(R.id.imageSelectedService);
        tv_nome_servico = (TextView) view.findViewById(R.id.tx_nome_selectedService);
        tv_nome_categoria = (TextView) view.findViewById(R.id.tx_nome_selectedCategorie);
        valor_servico = (EditText) view.findViewById(R.id.edt_valor_servico);
        duracao_servico = (EditText) view.findViewById(R.id.edt_duracao_servico);
        custo_deslocamento = (EditText) view.findViewById(R.id.edt_custo_deslocamento_servico);
        descricao_servico = (EditText) view.findViewById(R.id.edt_descricao_personalizada_servico);
        fornece_material = (Spinner) view.findViewById(R.id.spinner_material_servico);
        desloca_cliente = (Spinner) view.findViewById(R.id.spinner_deslocamento_servico);
        salvar_Servico = (Button) view.findViewById(R.id.btn_salvar_servico);
    }
}