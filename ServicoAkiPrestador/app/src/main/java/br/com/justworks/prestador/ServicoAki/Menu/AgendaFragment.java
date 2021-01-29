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
import android.widget.ToggleButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import br.com.justworks.prestador.ServicoAki.Activity.EditarEvento;
import br.com.justworks.prestador.ServicoAki.Adapter.AdapterScheduleItem;
import br.com.justworks.prestador.ServicoAki.Adapter.SchedulesItemAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.Base.AgendaBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.DatePickerListener;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.HorizontalPicker;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleDocument;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.R;

public class AgendaFragment extends Fragment implements DatePickerListener, AdapterScheduleItem.onScheduleItemListenner{

    private ImageView imgAgenda;
    private FloatingActionButton criar_evento;
    private TextView tv_titulo, tv_descricao, tv_agendaCheia;
    private AdapterScheduleItem adapter;
    private HorizontalPicker picker;
    private RecyclerView recyclerView;
    private ArrayList<ScheduleItems> scheduleItemsList;
    private ArrayList<ScheduleItems> scheduleItemsListByDay;
    private DateTime dateTimeSelected;

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

        pickerControl();

        criar_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent criarEvento = new Intent(getContext(), CriarEvento.class);
                startActivity(criarEvento);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(dateTimeSelected != null){
            onDateSelected(dateTimeSelected);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void carregarAgendaDoDia(int dia, int mes, int ano){

        final Calendar dataDoDia = Calendar.getInstance(Locale.getDefault()), dataDiaSeguinte = Calendar.getInstance(Locale.getDefault());
        dataDoDia.set(ano, mes-1, dia, 0, 0);
        dataDiaSeguinte.set(ano, mes-1, dia, 23 ,59, 59);

        scheduleItemsList = new ArrayList<>();

        scheduleItemsList = AgendaBase.getInstance().getScheduleItemsList();

            if (scheduleItemsList != null && scheduleItemsList.size() > 0) {

                scheduleItemsListByDay = new ArrayList<>();

                for(ScheduleItems scheduleItemsByDay: scheduleItemsList){
                    if (scheduleItemsByDay.getHourBegin().toDate().after(dataDoDia.getTime())) {
                        if(scheduleItemsByDay.getHourEnd().toDate().before(dataDiaSeguinte.getTime())){
                            scheduleItemsListByDay.add(scheduleItemsByDay);
                        }
                    }
                }

                if(scheduleItemsListByDay.size() > 0){
                    imgAgenda.setVisibility(View.GONE);
                    tv_titulo.setVisibility(View.GONE);
                    tv_descricao.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_agendaCheia.setVisibility(View.VISIBLE);
                } else {
                    imgAgenda.setVisibility(View.VISIBLE);
                    tv_titulo.setVisibility(View.VISIBLE);
                    tv_descricao.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tv_agendaCheia.setVisibility(View.GONE);
                }

                adapter = new AdapterScheduleItem(scheduleItemsListByDay, this);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                recyclerView.setAdapter(adapter);
            } else {
                imgAgenda.setVisibility(View.VISIBLE);
                tv_titulo.setVisibility(View.VISIBLE);
                tv_descricao.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tv_agendaCheia.setVisibility(View.GONE);
            }
    }

    private void pickerControl() {
        picker.setListener(this)
                .setDays(15)
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
                .showTodayButton(false)
                .init();
        picker.setBackgroundColor(Color.WHITE);
        picker.setDate(new DateTime());
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        carregarAgendaDoDia(dateSelected.dayOfMonth().get(), dateSelected.monthOfYear().get(), dateSelected.year().get());
        dateTimeSelected = dateSelected;
    }

    private void inicializarComponentes(View view) {
        picker = (HorizontalPicker) view.findViewById(R.id.datePicker);
        criar_evento = (FloatingActionButton) view.findViewById(R.id.fb_criar_evento);
        recyclerView = view.findViewById(R.id.reciclerView_servicoItem);
        imgAgenda = (ImageView) view.findViewById(R.id.imagemAgenda);
        tv_titulo = (TextView) view.findViewById(R.id.tv_semCompromisso);
        tv_descricao = (TextView) view.findViewById(R.id.tv_agendaVazia);
        tv_agendaCheia = (TextView) view.findViewById(R.id.tv_proxAtendimentos);

    }

    @Override
    public void onScheduleItemClick(int position) {
        AgendaBase.getInstance().setScheduleItemsListByDay(scheduleItemsListByDay);
        Intent editarEvento = new Intent(requireActivity(), EditarEvento.class);
        editarEvento.putExtra("position", position);
        startActivity(editarEvento);
    }
}