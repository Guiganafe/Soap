package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.Sex;
import br.com.justworks.prestador.ServicoAki.Model.User;
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
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private ProgressBar progressBar;

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
                if(validarCampos()){
                    concluirCadastro();   
                }
            }
        });
    }

    private boolean validarCampos() {
        if(servicoViewModel.getServices_list().getValue() == null){
            Toast.makeText(requireActivity(), "Selecione ao menos um serviço", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void concluirCadastro() {

        String name, email, birthDate, imageUrl, motherName, phoneNumber, dispatchingAgency, emissionDate,
                governmentId, identifyDocument, selfieUrl, frontUrl, backUrl, comprovanteUrl;
        name = profissionalViewModel.getNome_completo().getValue();
        email = profissionalViewModel.getEmail().getValue();
        birthDate = profissionalViewModel.getData_nascimento().getValue();
        imageUrl = profissionalViewModel.getFoto_perfil_url().getValue();
        motherName = profissionalViewModel.getNome_mae().getValue();
        phoneNumber = profissionalViewModel.getTelefone().getValue();
        dispatchingAgency = profissionalViewModel.getOrgao_emissor().getValue();
        emissionDate = profissionalViewModel.getDataEmissao().getValue();
        governmentId = profissionalViewModel.getCpf().getValue();
        identifyDocument = profissionalViewModel.getRg().getValue();
        selfieUrl = profissionalViewModel.getFoto_selfie_url().getValue();
        frontUrl = profissionalViewModel.getFoto_doc_frente_url().getValue();
        backUrl = profissionalViewModel.getFoto_doc_verso_url().getValue();
        comprovanteUrl = profissionalViewModel.getFoto_comprovante_res_url().getValue();

        Date data = new Date();
        try {
            data = dataTimestamp();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timestamp birthDateT = new Timestamp(data);

        String sexoPt;
        sexoPt = sexoViewModel.getSexoPtbr().getValue();

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

        final Address address = new Address(active, addressName, addressType, city, country, neighborhood, number, state, street, userId , zipCode, latitude, longitude);

        ArrayList<ServiceUser> serviceUser = servicoViewModel.getServices_list().getValue();

        sendAddress(address);

        User user = new User();

        user.setCreatedEnv("Android");
        user.setActive(true);
        user.setIsNew(true);
        user.setIsProfessional(true);
        user.setIsAuthenticated(false);
        user.setName(name);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setBirthDateTimestamp(birthDateT);
        user.setCivilState(civilState);
        user.setImageUrl(imageUrl);
        user.setMotherName(motherName);
        user.setPhoneNumber(phoneNumber);
        user.setSex(sexoPt);
        user.setDispatchingAgency(dispatchingAgency);
        user.setEmissionDate(emissionDate);
        user.setGovernmentId(governmentId);
        user.setIdentifyDocument(identifyDocument);
        user.setDocumentSelfie(selfieUrl);
        user.setIdFrontImage(frontUrl);
        user.setIdBackImage(backUrl);
        user.setProofOfAddressImage(comprovanteUrl);
        user.setServices(serviceUser);

        db.collection("users").document(userID).set(user.toMap(), SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent mainIntent = new Intent(requireActivity(), MainActivity.class);
                startActivity(mainIntent);
                requireActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "Erro ao salvar o usuário", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Date dataTimestamp() throws ParseException {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(profissionalViewModel.getData_nascimento().getValue());
        return date;
    }

    private Task<String> sendAddress(Address address) {
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
        loadController();
        if(servicoViewModel.getServices_list().getValue() != null){
            progressBar.setProgress(100);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void inicializarComponentes(View view) {
        recyclerView = view.findViewById(R.id.reciclerView_categories);
        concluirCadastro = (Button) view.findViewById(R.id.btn_concluir_cadastro);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_step_7);
        progressBar.setVisibility(View.VISIBLE);
        if(servicoViewModel.getServices_list().getValue() == null){
            progressBar.setProgress(95);
        }else{
            progressBar.setProgress(100);
        }

    }
}