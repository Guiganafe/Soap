package br.com.justworks.prestador.ServicoAki.Menu;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.justworks.prestador.ServicoAki.Activity.CriarEvento;
import br.com.justworks.prestador.ServicoAki.Adapter.AdapterScheduleItem;
import br.com.justworks.prestador.ServicoAki.Adapter.SchedulesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.DatePickerListener;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.HorizontalPicker;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleDocument;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.R;

public class AgendaFragment extends Fragment implements DatePickerListener {

    private ImageView criar_evento, imgAgenda;
    private TextView tv_titulo, tv_descricao, tv_agendaCheia;
    private AdapterScheduleItem adapter;
    //private SchedulesItemAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<ScheduleItems> scheduleItemsList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference servicesReference = db.collection("scheduleItems");
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

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

        carregarAgendaDoDia();

        //setUpReciclerView();

        criar_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent criarEvento = new Intent(getContext(), CriarEvento.class);
                startActivity(criarEvento);
            }
        });
    }

    private void setUpReciclerView(int ano, int mes, int dia) {
        Calendar dataInicio = Calendar.getInstance(Locale.getDefault()), dataMax = Calendar.getInstance(Locale.getDefault());
        dataInicio.set(ano, mes, dia, 0, 0, 0);

        dataMax.set(ano, mes, dia+1, 0, 0, 0);
        //Toast.makeText(requireActivity(), ": " + dataInicio.getTime(), Toast.LENGTH_SHORT).show();

        Query query = servicesReference.whereEqualTo("scheduleId", userID).whereGreaterThan("hourBegin", dataInicio.getTime()).whereLessThan("hourEnd", dataMax);

        FirestoreRecyclerOptions<ScheduleItems> options = new FirestoreRecyclerOptions.Builder<ScheduleItems>()
                .setQuery(query, ScheduleItems.class)
                .build();

        if(!options.getSnapshots().isEmpty()){
            imgAgenda.setVisibility(View.GONE);
            tv_titulo.setVisibility(View.GONE);
            tv_descricao.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tv_agendaCheia.setVisibility(View.VISIBLE);
        }

//        adapter = new SchedulesItemAdapter(options, getContext());
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void carregarAgendaDoDia(){
        db.collection("scheduleItems").whereEqualTo("scheduleId", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        scheduleItemsList.add(document.toObject(ScheduleItems.class));
                        Toast.makeText(requireActivity(), document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(requireActivity(), "size: " + scheduleItemsList.size(), Toast.LENGTH_SHORT).show();
                    if(scheduleItemsList.size() > 0){
                        imgAgenda.setVisibility(View.GONE);
                        tv_titulo.setVisibility(View.GONE);
                        tv_descricao.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_agendaCheia.setVisibility(View.VISIBLE);
                    }
                    adapter = new AdapterScheduleItem(scheduleItemsList);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(requireActivity(), "erro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickerControl(@NonNull View view) {
        HorizontalPicker picker = (HorizontalPicker) view.findViewById(R.id.datePicker);
        picker.setListener(this)
                .setDays(30)
                .setOffset(4)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.DKGRAY)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY )
                .setUnselectedDayTextColor(getResources().getColor(R.color.colorWhite))
                .init();
        picker.setBackgroundColor(Color.WHITE);
        picker.setDate(new DateTime());
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.i("HorizontalPicker","Fecha seleccionada = " + dateSelected.toString());
//        int ano = dateSelected.year().get();
//        int mes = dateSelected.monthOfYear().get();
//        int dia = dateSelected.dayOfMonth().get();
//        setUpReciclerView(ano, mes, dia);
    }

    private void inicializarComponentes(View view) {
        criar_evento = (ImageView) view.findViewById(R.id.img_criar_evento);
        recyclerView = view.findViewById(R.id.reciclerView_servicoItem);
        imgAgenda = (ImageView) view.findViewById(R.id.imagemAgenda);
        tv_titulo = (TextView) view.findViewById(R.id.tv_semCompromisso);
        tv_descricao = (TextView) view.findViewById(R.id.tv_agendaVazia);
        tv_agendaCheia = (TextView) view.findViewById(R.id.tv_proxAtendimentos);

    }
}