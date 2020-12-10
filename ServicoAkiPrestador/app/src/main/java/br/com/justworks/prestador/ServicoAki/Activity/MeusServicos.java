package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.ServicesDocument;
import br.com.justworks.prestador.ServicoAki.R;

public class MeusServicos extends AppCompatActivity implements ServicesListAdapter.onServiceListenner{

    private ImageView semServicoImg;
    private TextView semServicoTitulo, semServicoDesc;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<ServiceUser> servicesUser;
    private Context context = this;
    private FloatingActionButton floatButton;
    private int posicaoSelected;
    private ServicesListAdapter.onServiceListenner serviceListenner = (ServicesListAdapter.onServiceListenner) this.context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_servicos);

        inicializarComponentes();

        setUpReciclerView();

        onClickController();
    }

    private void setUpReciclerView() {
        servicesUser = UserBase.getInstance().getServicesUserList();
        if(servicesUser != null && servicesUser.size() >= 1){
            recyclerView.setVisibility(View.VISIBLE);
            semServicoImg.setVisibility(View.GONE);
            semServicoTitulo.setVisibility(View.GONE);
            semServicoDesc.setVisibility(View.GONE);
            adapter = new ServicesListAdapter(servicesUser, context, serviceListenner);
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

    private void onClickController() {
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addServicoIntent = new Intent(context, AdicionarServico.class);
                startActivity(addServicoIntent);
            }
        });
    }

    @Override
    public void onServiceClick(int position) {
        Intent editarServico = new Intent(this, EditarServico.class);
        editarServico.putExtra("position", position);
        startActivity(editarServico);
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpReciclerView();
    }

    @Override
    public void onStop() {
        super.onStop();
        setUpReciclerView();
    }

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.reciclerView_UserServices);
        floatButton = (FloatingActionButton) findViewById(R.id.btn_add_servico);
        semServicoImg = (ImageView) findViewById(R.id.img_sem_servico);
        semServicoTitulo = (TextView) findViewById(R.id.tv_sem_servico_titulo);
        semServicoDesc = (TextView) findViewById(R.id.tv_sem_servico_desc);
    }
}