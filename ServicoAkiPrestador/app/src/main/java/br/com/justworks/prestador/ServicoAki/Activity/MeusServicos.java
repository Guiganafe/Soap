package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.ServicesDocument;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class MeusServicos extends AppCompatActivity implements ServicesListAdapter.onServiceListenner {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ServicoViewModel servicoViewModel;
    private ArrayList<ServiceUser> servicesUser;
    private Context context = this;
    private ImageView addServico;
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
        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    servicesUser = documentSnapshot.toObject(ServicesDocument.class).services;
                    adapter = new ServicesListAdapter(servicesUser, context, serviceListenner);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void onClickController() {
        addServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addServicoIntent = new Intent(MeusServicos.this, AdicionarServico.class);
                startActivity(addServicoIntent);
            }
        });
    }

    @Override
    public void onServiceClick(int position) {
        Toast.makeText(context, "a: " + servicesUser.get(position).getName().getPtbr(), Toast.LENGTH_SHORT).show();
        Intent addServicoIntent = new Intent(MeusServicos.this, EditarServico.class);
        startActivity(addServicoIntent);
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
        servicoViewModel = new ViewModelProvider(this).get(ServicoViewModel.class);
        addServico = (ImageView) findViewById(R.id.img_add_servico);
    }
}