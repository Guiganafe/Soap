package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.ServicesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.Model.ServicesDocument;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class MeusServicos extends AppCompatActivity implements ServicesListAdapter.onServiceListenner {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ServicoViewModel servicoViewModel;
    private ArrayList<Services> servicesUser;
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
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void onClickController() {
//        adapter.setOnItemClickListener(new ServicesItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position, View v) {
//                String serviceId = documentSnapshot.getId();
//                br.com.justworks.prestador.ServicoAki.Model.Services services = documentSnapshot.toObject(Services.class);
//                servicoViewModel.setServices(services);
//                servicoViewModel.setServiceId(serviceId);
//                Navigation.findNavController(v).navigate(R.id.action_step_8_to_step_9);
//            }
//        });
    }

    @Override
    public void onServiceClick(int position) {
        Toast.makeText(context, "a: " + servicesUser.get(position).getName().getPtbr(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, AdicionarServico.class);
//        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.reciclerView_UserServices);
        servicoViewModel = new ViewModelProvider(this).get(ServicoViewModel.class);
        addServico = (ImageView) findViewById(R.id.img_add_servico);
    }
}