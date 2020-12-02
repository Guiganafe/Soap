package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.SvgSoftwareLayerSetter;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EditarServicoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class EditarServico extends AppCompatActivity {

//    private ImageView img_servico_edited;
//    private TextView tv_nome_servico_edited;
//    private TextView tv_nome_categoria_edited;
//    private TextView tv_custo_deslocamento_edited;
//    private EditText valor_servico_edited, duracao_servico_edited, custo_deslocamento_edited, descricao_servico_edited;
//    private Spinner fornece_material_edited, desloca_cliente_edited;
//    private Button salvar_Servico_edited;
//    private RequestBuilder<PictureDrawable> requestBuilder;
//
//    boolean material, desloca;
//
//    private EditarServicoViewModel editarServicoViewModel;
//    private ArrayList<ServiceUser> servicesUserlist = new ArrayList<>();
//    private ServiceUser serviceUser;
//    private int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servico);

//        Intent intent = getIntent();
//        serviceUser = (ServiceUser) intent.getSerializableExtra("serviceLis");
//        posicao = (Integer) intent.getSerializableExtra("posicao");
//
//        inicializarComponentes();
//
//        inicializarModel();
//
//        loadController();
    }

    private void inicializarModel() {
//         = editarServicoViewModel.getPosicao().getValue();
//        servicesUserlist = editarServicoViewModel.getServices_list().getValue();
//        serviceUser = servicesUserlist.get(posicao);
//
//        valor_servico_edited.setText(serviceUser.getPrice().toString());
//        duracao_servico_edited.setText(serviceUser.getAvgExecutionTime());
//        custo_deslocamento_edited.setText(serviceUser.getMovementCost().toString());
//        descricao_servico_edited.setText(serviceUser.getDescription().getPtbr());
    }

    private void loadController() {
//        requestBuilder = Glide.with(this).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());
//
//        Uri uri = Uri.parse(serviceUser.getCategory().get(0).getImageIconUrl().getSvg());
//        requestBuilder
//                .load(uri)
//                .into(img_servico_edited);
//
//        tv_nome_servico_edited.setText(serviceUser.getName().getPtbr());
//        tv_nome_categoria_edited.setText(serviceUser.getCategory().get(0).getName().getPtbr());
    }

    private void spinnerController() {
//        int moveToClient, offersMaterial;
//
//        if(serviceUser.isMoveToClient()){
//            moveToClient = 1;
//        } else {
//            moveToClient = 2;
//        }
//
//        if(serviceUser.isOffersMeterial()){
//            offersMaterial = 1;
//        } else {
//            offersMaterial = 2;
//        }
//
//        custo_deslocamento_edited.setVisibility(View.GONE);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sim_nao_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        fornece_material_edited.setAdapter(adapter);
//        fornece_material_edited.setSelection(offersMaterial);
//
//        desloca_cliente_edited.setAdapter(adapter);
//        desloca_cliente_edited.setSelection(moveToClient);
//
//        fornece_material_edited.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position == 1){
//                    material = true;
//                }else if(position == 2 || position == 0){
//                    material = false;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        desloca_cliente_edited.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position == 1){
//                    desloca = true;
//                    tv_custo_deslocamento_edited.setVisibility(View.VISIBLE);
//                    custo_deslocamento_edited.setVisibility(View.VISIBLE);
//                }else if(position == 2 || position == 0){
//                    desloca = false;
//                    tv_custo_deslocamento_edited.setVisibility(View.GONE);
//                    custo_deslocamento_edited.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void inicializarComponentes() {
//        tv_custo_deslocamento_edited = (TextView) findViewById(R.id.tv_custo_deslocamento_servico_editado);
//        img_servico_edited = (ImageView) findViewById(R.id.imageEditedService);
//        tv_nome_servico_edited = (TextView) findViewById(R.id.tx_nome_editedService);
//        tv_nome_categoria_edited = (TextView) findViewById(R.id.tx_nome_editedCategorie);
//        valor_servico_edited = (EditText) findViewById(R.id.edt_valor_servico_editado);
//        duracao_servico_edited = (EditText) findViewById(R.id.edt_duracao_servico_editado);
//        custo_deslocamento_edited = (EditText) findViewById(R.id.edt_custo_deslocamento_servico_editado);
//        descricao_servico_edited = (EditText) findViewById(R.id.edt_descricao_personalizada_servico_editado);
//        fornece_material_edited = (Spinner) findViewById(R.id.spinner_material_servico_editado);
//        desloca_cliente_edited = (Spinner) findViewById(R.id.spinner_deslocamento_servico_editado);
//        salvar_Servico_edited = (Button) findViewById(R.id.btn_salvar_servico_editado);
    }
}