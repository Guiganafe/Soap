package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.ServiceListEventoAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Base.ServicosBase;
import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServiceEventListViewModel;

public class SelecionarEvento extends AppCompatActivity implements ServiceListEventoAdapter.onServiceEventoListenner{

    private ImageView semServicoImg;
    private TextView semServicoTitulo, semServicoDesc;
    private RecyclerView recyclerView;
    private ServiceListEventoAdapter adapter;
    private ArrayList<ServiceUser> servicos;
    private ArrayList<ServiceUser> servicosRestantes;
    private ArrayList<ServiceUser> servicosSelecionados;
    private Context context = this;
    private ServiceListEventoAdapter.onServiceEventoListenner serviceEventoListenner = (ServiceListEventoAdapter.onServiceEventoListenner) this.context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_evento);

        inicializarComponentes();

        setUpReciclerView();
    }

    private void setUpReciclerView() {
        servicos = ServicosBase.getInstance().getServicos(); //UserBase.getInstance().getServicesUserList();
        servicosRestantes = ServicosBase.getInstance().getServicos_restantes();
        if(servicos != null && servicos.size() >= 1){
            recyclerView.setVisibility(View.VISIBLE);
            semServicoImg.setVisibility(View.GONE);
            semServicoTitulo.setVisibility(View.GONE);
            semServicoDesc.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if(servicosRestantes != null && servicosRestantes.size() < servicos.size() && servicosRestantes.size() >= 1){
                adapter = new ServiceListEventoAdapter(servicosRestantes, context, serviceEventoListenner);
            } else {
                adapter = new ServiceListEventoAdapter(servicos, context, serviceEventoListenner);
            }

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            semServicoImg.setVisibility(View.VISIBLE);
            semServicoTitulo.setVisibility(View.VISIBLE);
            semServicoDesc.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onServiceClick(int position) {
        if(servicosRestantes != null && servicosRestantes.size() < servicos.size() && servicosRestantes.size() >= 1){
            Toast.makeText(context, "Serviço " + servicosRestantes.get(position).getName().getPtbr() + " adicionado ao evento.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Serviço " + servicos.get(position).getName().getPtbr() + " adicionado ao evento.", Toast.LENGTH_SHORT).show();
            ServicosBase.getInstance().addServico_selecionado(servicos.get(position));
            finish();
        }
    }

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.reciclerView_UserServices_evento);
        semServicoImg = (ImageView) findViewById(R.id.img_sem_servico_evento);
        semServicoTitulo = (TextView) findViewById(R.id.tv_sem_servico_titulo_evento);
        semServicoDesc = (TextView) findViewById(R.id.tv_sem_servico_desc_evento);
    }
}