package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.com.justworks.prestador.ServicoAki.Adapter.ServicesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class Step_8 extends Fragment {

    private ServicesItemAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference servicesReference = db.collection("services");
    private ServicoViewModel servicoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        servicoViewModel = new ViewModelProvider(requireActivity()).get(ServicoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_8, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        setUpReciclerView();

        onClickController();
    }

    private void setUpReciclerView() {
        Query query = servicesReference.whereArrayContains("categoryIds", servicoViewModel.getCategoriaId().getValue());

        FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>()
                .setQuery(query, Services.class)
                .build();

        adapter = new ServicesItemAdapter(options, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void onClickController() {
        adapter.setOnItemClickListener(new ServicesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, View v) {
                String serviceId = documentSnapshot.getId();
                Services services = documentSnapshot.toObject(Services.class);
                servicoViewModel.setServices(services);
                servicoViewModel.setServiceId(serviceId);
                Navigation.findNavController(v).navigate(R.id.action_step_8_to_step_9);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void inicializarComponentes(View view) {
        recyclerView = view.findViewById(R.id.reciclerView_services);
    }
}