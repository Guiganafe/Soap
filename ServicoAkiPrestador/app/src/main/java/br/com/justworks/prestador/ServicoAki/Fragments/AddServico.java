package br.com.justworks.prestador.ServicoAki.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Activity.AdicionarServico;
import br.com.justworks.prestador.ServicoAki.Activity.EditarServico;
import br.com.justworks.prestador.ServicoAki.Adapter.ServiceListEventoAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServiceEventListViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class AddServico extends Fragment implements ServiceListEventoAdapter.onServiceListenner {

    private ImageView semServicoImg;
    private TextView semServicoTitulo, semServicoDesc;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ServiceUser> servicesUser;
    private ServiceUser serviceUserSelected;
    private Context context = getContext();
    private ServiceEventListViewModel serviceEventListViewModel;
    private ServiceListEventoAdapter.onServiceListenner serviceEventoListenner = (ServiceListEventoAdapter.onServiceListenner) this.context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceEventListViewModel = new ViewModelProvider(requireActivity()).get(ServiceEventListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_servico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        setUpReciclerView();
    }

    private void setUpReciclerView() {
        servicesUser = UserBase.getInstance().getServicesUserList();
        if(servicesUser != null && servicesUser.size() >= 1){
            recyclerView.setVisibility(View.VISIBLE);
            semServicoImg.setVisibility(View.GONE);
            semServicoTitulo.setVisibility(View.GONE);
            semServicoDesc.setVisibility(View.GONE);
            adapter = new ServiceListEventoAdapter(servicesUser, context, serviceEventoListenner);
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

    private void inicializarComponentes(View view) {
        recyclerView = view.findViewById(R.id.reciclerView_UserServices_evento);
        semServicoImg = (ImageView) view.findViewById(R.id.img_sem_servico_evento);
        semServicoTitulo = (TextView) view.findViewById(R.id.tv_sem_servico_titulo_evento);
        semServicoDesc = (TextView) view.findViewById(R.id.tv_sem_servico_desc_evento);
    }
}