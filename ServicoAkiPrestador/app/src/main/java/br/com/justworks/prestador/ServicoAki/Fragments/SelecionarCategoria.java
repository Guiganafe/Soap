package br.com.justworks.prestador.ServicoAki.Fragments;

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
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.com.justworks.prestador.ServicoAki.Adapter.CategoriesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.EstadoCivilViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.SexoViewModel;

public class SelecionarCategoria extends Fragment {

    private CategoriesItemAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categoriesReference = db.collection("categoriesServices");
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

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
        return inflater.inflate(R.layout.fragment_selecionar_categoria, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        setUpReciclerView();

        onClickController();
    }

    private void onClickController() {
        adapter.setOnItemClickListener(new CategoriesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, View v) {
                CategoriesServices categoriesServices = documentSnapshot.toObject(CategoriesServices.class);
                String id = documentSnapshot.getId();
                servicoViewModel.setCategoriaId(id);
                servicoViewModel.setCategoriesServices(categoriesServices);
                Navigation.findNavController(v).navigate(R.id.action_selecionarCategoria_to_selecionarServico);
            }
        });
    }

    private void setUpReciclerView() {
        Query query = categoriesReference.whereGreaterThan("qtdServices", 0).whereEqualTo("active", true).orderBy("qtdServices").orderBy("name.ptbr");

        FirestoreRecyclerOptions<CategoriesServices> options = new FirestoreRecyclerOptions.Builder<CategoriesServices>()
                .setQuery(query, CategoriesServices.class)
                .build();

        adapter = new CategoriesItemAdapter(options, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
        recyclerView = view.findViewById(R.id.reciclerView_SelecionarCategoria);
    }
}