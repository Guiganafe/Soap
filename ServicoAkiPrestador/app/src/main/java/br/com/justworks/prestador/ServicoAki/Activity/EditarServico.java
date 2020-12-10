package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.SvgSoftwareLayerSetter;
import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.Description;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class EditarServico extends AppCompatActivity {

    private ServiceUser serviceUser;
    private ImageView img_servico_edicao;
    private TextView tv_nome_servico_edicao;
    private TextView tv_nome_categoria_edicao;
    private TextView tv_custo_deslocamento_edicao;
    private EditText valor_servico_edicao, duracao_servico_edicao, custo_deslocamento_edicao, descricao_servico_edicao;
    private Spinner fornece_material_edicao, desloca_cliente_edicao;
    private Button salvar_Servico_edicao, excluir_servico_edicao;
    private RequestBuilder<PictureDrawable> requestBuilder;
    boolean material, desloca;
    private int position;
    private long minutos;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servico);

        inicializarComponentes();

        Intent intent = getIntent();

        position = intent.getIntExtra("position", 0);

        serviceUser = UserBase.getInstance().getServiceUser(position);

        loadController();

        maskController();

        spinnerController();

        onClickController();
    }

    private void onClickController() {
        salvar_Servico_edicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serviceUser.setPrice(Double.parseDouble(valor_servico_edicao.getText().toString()));
                if(desloca){
                    serviceUser.setMovementCost(Double.parseDouble(custo_deslocamento_edicao.getText().toString()));
                } else {
                    serviceUser.setMovementCost(0.0);
                }

                String duracao_txt = duracao_servico_edicao.getText().toString();
                int duracao;
                duracao = (Integer.parseInt(duracao_txt.substring(0,2)) * 60) + Integer.parseInt(duracao_txt.substring(3,5));

                serviceUser.setAvgExecutionTime(duracao);
                Description description = new Description();
                description.setPtbr(descricao_servico_edicao.getText().toString());
                serviceUser.setDescription(description);

                UserBase.getInstance().removeService(position);
                UserBase.getInstance().addServiceUser(serviceUser);
                User user = UserBase.getInstance().getUser();

                db.collection("users").document(userID).update(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditarServico.this, "Serviço alterado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        excluir_servico_edicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBase.getInstance().removeService(position);
                User user = UserBase.getInstance().getUser();
                db.collection("users").document(userID).update(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditarServico.this, "Serviço removido com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private void maskController() {
        duracao_servico_edicao.addTextChangedListener(MaskEditUtil.mask(duracao_servico_edicao, MaskEditUtil.FORMAT_HORA_MINUTOS));
    }

    private void spinnerController() {
        custo_deslocamento_edicao.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sim_nao_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fornece_material_edicao.setAdapter(adapter);
        desloca_cliente_edicao.setAdapter(adapter);

        if(material){
            fornece_material_edicao.setSelection(1);
        } else {
            fornece_material_edicao.setSelection(2);
            tv_custo_deslocamento_edicao.setVisibility(View.GONE);
            custo_deslocamento_edicao.setVisibility(View.GONE);
        }

        if(desloca){
            desloca_cliente_edicao.setSelection(1);
            tv_custo_deslocamento_edicao.setVisibility(View.VISIBLE);
            custo_deslocamento_edicao.setVisibility(View.VISIBLE);
        } else {
            desloca_cliente_edicao.setSelection(2);
        }


        fornece_material_edicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    material = true;
                }else if(position == 2 || position == 0){
                    material = false;
                }

                serviceUser.setOffersMeterial(material);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        desloca_cliente_edicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    desloca = true;
                    tv_custo_deslocamento_edicao.setVisibility(View.VISIBLE);
                    custo_deslocamento_edicao.setVisibility(View.VISIBLE);
                }else if(position == 2 || position == 0){
                    desloca = false;
                    tv_custo_deslocamento_edicao.setVisibility(View.GONE);
                    custo_deslocamento_edicao.setVisibility(View.GONE);
                }

                serviceUser.setMoveToClient(desloca);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadController() {
        CategoriesServices categoriesService = serviceUser.getCategory().get(0);

        requestBuilder = Glide.with(this).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(categoriesService.getImageIconUrl().getSvg());
        requestBuilder
                .load(uri)
                .into(img_servico_edicao);
        String nomeServico, nomeCategoria, descricaoServico;
        int duracaoServico;
        Double valorServico, valorDeslocamento;

        nomeServico = serviceUser.getName().getPtbr();
        nomeCategoria = categoriesService.getName().getPtbr();
        descricaoServico = serviceUser.getDescription().getPtbr();

        valorServico = serviceUser.getPrice();
        duracaoServico = serviceUser.getAvgExecutionTime();

        if(nomeServico != null){
            tv_nome_servico_edicao.setText(nomeServico);
        }

        if(nomeCategoria != null){
            tv_nome_categoria_edicao.setText(nomeCategoria);
        }

        if(descricaoServico != null){
            descricao_servico_edicao.setText(descricaoServico);
        }

        if(valorServico != null){
            valor_servico_edicao.setText(valorServico.toString());
        }

        double d = duracaoServico;
        // vamos converter para segundos primeiro
        long h = Math.round(duracaoServico / 60);
        long m = Math.round(duracaoServico - (h * 60));

        minutos = ( h * 60) + m;

        if(h < 9){
            if(m < 9){
                duracao_servico_edicao.setText("0" + h + ":0" + m);
            } else {
                duracao_servico_edicao.setText("0" + h + ":" + m);
            }
        } else {
            if(m < 9){
                duracao_servico_edicao.setText(h + ":0" + m);
            } else {
                duracao_servico_edicao.setText(h + ":" + m);
            }
        }


        material = serviceUser.isOffersMeterial();
        desloca = serviceUser.isMoveToClient();

        if(material){
            custo_deslocamento_edicao.setText(serviceUser.getMovementCost().toString());
        }

    }

    private void inicializarComponentes() {
        tv_custo_deslocamento_edicao = (TextView) findViewById(R.id.tv_custo_deslocamento_servico_edicao);
        img_servico_edicao = (ImageView) findViewById(R.id.imageSelectedService_edicao);
        tv_nome_servico_edicao = (TextView) findViewById(R.id.tx_nome_selectedService_edicao);
        tv_nome_categoria_edicao = (TextView) findViewById(R.id.tx_nome_selectedCategorie_edicao);
        valor_servico_edicao = (EditText) findViewById(R.id.edt_valor_servico_edicao);
        duracao_servico_edicao = (EditText) findViewById(R.id.edt_duracao_servico_edicao);
        custo_deslocamento_edicao = (EditText) findViewById(R.id.edt_custo_deslocamento_servico_edicao);
        descricao_servico_edicao = (EditText) findViewById(R.id.edt_descricao_personalizada_servico_edicao);
        fornece_material_edicao = (Spinner) findViewById(R.id.spinner_material_servico_edicao);
        desloca_cliente_edicao = (Spinner) findViewById(R.id.spinner_deslocamento_servico_edicao);
        salvar_Servico_edicao = (Button) findViewById(R.id.btn_salvar_servico_edicao);
        excluir_servico_edicao = (Button) findViewById(R.id.btn_excluir_servico_edicao);
    }
}