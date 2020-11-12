package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.com.justworks.prestador.ServicoAki.Activity.Services;
import br.com.justworks.prestador.ServicoAki.Adapter.CategoriesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.SchedulesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.R;

public class Step_7 extends Fragment {

    private CategoriesItemAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categoriesReference = db.collection("categoriesServices");

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

        onClickController();
    }

    private void onClickController() {
        adapter.setOnItemClickListener(new CategoriesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //CategoriesServices categoriesServices = documentSnapshot.toObject(CategoriesServices.class);
                String id = documentSnapshot.getId();
                Toast.makeText(requireActivity(), "position: " + position + " id: " + id, Toast.LENGTH_SHORT).show();
                Intent servicesIntent = new Intent(requireActivity(), Services.class);
                servicesIntent.putExtra("id_categoria", id);
                startActivity(servicesIntent);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void inicializarComponentes(View view) {
        recyclerView = view.findViewById(R.id.reciclerView_categories);
    }
}