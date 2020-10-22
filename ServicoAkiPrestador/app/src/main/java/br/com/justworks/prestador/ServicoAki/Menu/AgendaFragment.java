package br.com.justworks.prestador.ServicoAki.Menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.joda.time.DateTime;

import br.com.justworks.prestador.ServicoAki.Activity.CriarEvento;
import br.com.justworks.prestador.ServicoAki.Adapter.SchedulesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.DatePickerListener;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.HorizontalPicker;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.R;

public class AgendaFragment extends Fragment implements DatePickerListener {

    private ImageView criar_evento;
    private SchedulesItemAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference servicesReference = db.collection("scheduleItems");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        pickerControl(view);

        setUpReciclerView();

        criar_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent criarEvento = new Intent(getContext(), CriarEvento.class);
                startActivity(criarEvento);
            }
        });
    }

    private void setUpReciclerView() {
        Query query = servicesReference.whereEqualTo("scheduleId", FirebaseService.getFirebaseAuth().getCurrentUser().getUid().toString());

        FirestoreRecyclerOptions<ScheduleItems> options = new FirestoreRecyclerOptions.Builder<ScheduleItems>()
                .setQuery(query, ScheduleItems.class)
                .build();

        adapter = new SchedulesItemAdapter(options, getContext());
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

    private void pickerControl(@NonNull View view) {
        HorizontalPicker picker = (HorizontalPicker) view.findViewById(R.id.datePicker);
        picker.setListener(this)
                .setDays(15)
                .setOffset(7)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.DKGRAY)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY )
                .setUnselectedDayTextColor(getResources().getColor(R.color.colorWhite))
                .showTodayButton(true)
                .init();
        picker.setBackgroundColor(Color.WHITE);
        picker.setDate(new DateTime());
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.i("HorizontalPicker","Fecha seleccionada="+dateSelected.toString());
    }

    private void inicializarComponentes(View view) {
        criar_evento = (ImageView) view.findViewById(R.id.img_criar_evento);
        recyclerView = view.findViewById(R.id.reciclerView_servicoItem);
    }
}