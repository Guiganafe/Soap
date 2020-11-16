package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Activity.MainActivity;
import br.com.justworks.prestador.ServicoAki.Activity.Services;
import br.com.justworks.prestador.ServicoAki.Adapter.CategoriesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.SchedulesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.CivilState;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.Model.Sex;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.EstadoCivilViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.SexoViewModel;

public class Step_7 extends Fragment {

    private CategoriesItemAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categoriesReference = db.collection("categoriesServices");
    private ServicoViewModel servicoViewModel;
    private Button concluirCadastro;
    private ProfissionalViewModel profissionalViewModel;
    private SexoViewModel sexoViewModel;
    private EndereçoViewModel endereçoViewModel;
    private EstadoCivilViewModel estadoCivilViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        servicoViewModel = new ViewModelProvider(requireActivity()).get(ServicoViewModel.class);
        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
        endereçoViewModel = new ViewModelProvider(requireActivity()).get(EndereçoViewModel.class);
        sexoViewModel = new ViewModelProvider(requireActivity()).get(SexoViewModel.class);
        estadoCivilViewModel = new ViewModelProvider(requireActivity()).get(EstadoCivilViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_7, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        setUpReciclerView();

        loadController();

        onClickController();
    }

    private void loadController() {
//        if(servicoViewModel.getServices_list().getValue() == null){
//            concluirCadastro.setBackgroundColor(R.drawable.edit_text_gray);
//            concluirCadastro.setEnabled(false);
//        } else {
//            concluirCadastro.setBackgroundColor(R.drawable.button_orange);
//            concluirCadastro.setEnabled(true);
//        }
    }

    private void onClickController() {
        adapter.setOnItemClickListener(new CategoriesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, View v) {
                CategoriesServices categoriesServices = documentSnapshot.toObject(CategoriesServices.class);
                String id = documentSnapshot.getId();
                servicoViewModel.setCategoriaId(id);
                servicoViewModel.setCategoriesServices(categoriesServices);
                Navigation.findNavController(v).navigate(R.id.action_step_7_to_step_8);
            }
        });

        concluirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concluirCadastro();
            }
        });
    }

    private void concluirCadastro() {
        String sexoPt, sexoEn;
        sexoPt = sexoViewModel.getSexoPtbr().getValue();
        sexoEn = sexoViewModel.getSexoEn().getValue();

        Sex sexo = new Sex(sexoPt, sexoEn);

        String estadoCivilPt, estadoCivilEn;
        estadoCivilPt = estadoCivilViewModel.getEstadoCivilPtBr().getValue();
        estadoCivilEn = estadoCivilViewModel.getEstadoCivilEn().getValue();

        CivilState civilState = new CivilState(estadoCivilPt, estadoCivilEn);

        Boolean active, default_address;
        String addressName, addressType, city, country, neighborhood, number, state, street, userId, zipCode;
        double latitude, longitude;

        active = true;

        addressName = "Endereço";
        addressType = "Home";
        city = endereçoViewModel.getCidade().getValue();
        country = endereçoViewModel.getPais().getValue();
        neighborhood = endereçoViewModel.getBairro().getValue();
        number = endereçoViewModel.getNumero().getValue();
        state = endereçoViewModel.getEstado().getValue();
        userId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
        street = endereçoViewModel.getRua().getValue();
        zipCode = endereçoViewModel.getCep().getValue();
        longitude = endereçoViewModel.getLongitude().getValue();
        latitude = endereçoViewModel.getLatitude().getValue();

        final Address address = new Address(active, addressName, addressType, city, country, neighborhood, number, state, userId, street, zipCode, longitude, latitude);

        addAddress(address);

        Intent mainIntent = new Intent(requireActivity(), MainActivity.class);
        startActivity(mainIntent);
        requireActivity().finish();
    }

    private Task<String> addAddress(Address address) {
        FirebaseFunctions functions = FirebaseFunctions.getInstance();

        return functions
                .getHttpsCallable("users-setAddress")
                .call(address.toMap())
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        Toast.makeText(requireActivity(), "Endereço enviado", Toast.LENGTH_SHORT).show();
                        return result;
                    }
                });
    }

    private void setUpReciclerView() {
        Query query = categoriesReference.whereGreaterThan("qtdServices", 0).orderBy("qtdServices").orderBy("name.ptbr");

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
        loadController();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void inicializarComponentes(View view) {
        recyclerView = view.findViewById(R.id.reciclerView_categories);
        concluirCadastro = (Button) view.findViewById(R.id.btn_concluir_cadastro);
    }
}