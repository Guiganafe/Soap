package br.com.justworks.prestador.ServicoAki.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Adapter.ServiceListEventoAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServiceSelectedAdapter;
import br.com.justworks.prestador.ServicoAki.Base.AgendaBase;
import br.com.justworks.prestador.ServicoAki.Base.ConfigAgendaBase;
import br.com.justworks.prestador.ServicoAki.Base.ServicosBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.Model.CustomLocks;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.Model.ServiceDays;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MoneyTextWatcher;
import br.com.justworks.prestador.ServicoAki.Util.ValidarData;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServiceEventListViewModel;


public class CriarEvento extends AppCompatActivity implements ServiceSelectedAdapter.onServiceSelectedListenner {

    private Spinner spinner;
    private Button btn_avancar, btn_cancelar;
    private Calendar calendar;
    private int dia,mes, ano, hora, minuto;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private EndereçoViewModel endereçoViewModel;
    private ServiceEventListViewModel serviceEventListViewModel;
    private EditText titulo_evento, inicio_evento_hora, inicio_evento_data, fim_evento_hora, fim_evento_data, tipo_evento, valor_evento, local_evento;
    private TextView tv_local, tv_valor, tv_servicos, tv_servicos_op, tv_add_servico;
    private LinearLayout lista_servicos_vazia;
    public boolean atendimento = true;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<ServiceUser> servicesUser = new ArrayList<>();
    private ArrayList<ServiceUser> servicesEvent = new ArrayList<>();
    private Context context = this;
    private ServiceSelectedAdapter.onServiceSelectedListenner serviceListenner = (ServiceSelectedAdapter.onServiceSelectedListenner) this.context;

    private int horaInicio, minutoInicio, diaInicio, mesInicio, anoInicio, horaFim, minutoFim, diaFim, mesFim, anoFim;

    Map<String, Object> scheduleItems = new HashMap<>();

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);
        endereçoViewModel = new ViewModelProvider(this).get(EndereçoViewModel.class);
        builder = new AlertDialog.Builder(this);
        ServicosBase.getInstance();
        inicializarComponentes();
        setUpReciclerView();
        spinnerControl();
        placesAutocompleteControl();
        timeControl();
        clickControl();
        maskController();
    }

    private void maskController() {
        valor_evento.addTextChangedListener(new MoneyTextWatcher(valor_evento));
    }

    private void setUpReciclerView() {
        servicesUser = ServicosBase.getInstance().getServicos_selecionados();
        if(servicesUser != null && servicesUser.size() >= 1){
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ServiceSelectedAdapter(servicesUser, context, serviceListenner);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpReciclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpReciclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ServicosBase.getInstance().clear();
        finish();
    }

    @Override
    public void onServiceClick(int position) {
        Toast.makeText(context, "Serviço" + servicesUser.get(position).getName().getPtbr() + " removido do evento", Toast.LENGTH_SHORT).show();
        ServicosBase.getInstance().removeServico_selecionado(servicesUser.get(position));
        setUpReciclerView();
    }

    private void clickControl() {
        btn_avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    salvarController();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicosBase.getInstance().clear();
                finish();
            }
        });
        tv_add_servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selecionarEvento = new Intent(context, SelecionarEvento.class);
                startActivity(selecionarEvento);
            }
        });
    }


    public void salvarController() throws ParseException {
        boolean diaDeTrabalho = true;
        boolean horarioLivre = true;

        final Calendar dataInicio = Calendar.getInstance(Locale.getDefault()), dataFim = Calendar.getInstance(Locale.getDefault());
        dataInicio.set(anoInicio, mesInicio, diaInicio, horaInicio, minutoInicio);
        dataFim.set(anoFim, mesFim, diaFim, horaFim, minutoFim);

        if(atendimento){
            diaDeTrabalho = ValidarData.isDiaDeTrabalho(ConfigAgendaBase.getInstance().getSchedule(), dataInicio);
            horarioLivre = ValidarData.isHorarioBloqueado(ConfigAgendaBase.getInstance().getSchedule(), dataInicio, dataFim);
        }

        if(diaDeTrabalho && horarioLivre){
            salvarEvento(dataInicio, dataFim);
        } else {
            if(!diaDeTrabalho){
                builder.setTitle("Atenção!");
                builder.setMessage("Você não trabalha no dia selecionado! Deseja salvar mesmo assim?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            salvarEvento(dataInicio, dataFim);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setTitle("Atenção!");
                builder.setTitle("Horário bloqueado! Deseja salvar mesmo assim?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            salvarEvento(dataInicio, dataFim);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Não",    new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void salvarEvento(Calendar dataInicio, Calendar dataFim) throws ParseException {

        String tituloEvento, inicioEventoHora, fimEventoHora, inicioEventoData, fimEventoData, valorEvento, localEvento;
        tituloEvento = titulo_evento.getText().toString();
        inicioEventoData = inicio_evento_data.getText().toString();
        inicioEventoHora = inicio_evento_hora.getText().toString();
        fimEventoData = fim_evento_data.getText().toString();
        fimEventoHora = fim_evento_hora.getText().toString();

        if (TextUtils.isEmpty(tituloEvento)) {
            titulo_evento.setError("Insira um título para o evento");
            return;
        } else if (TextUtils.isEmpty(inicioEventoData)) {
            inicio_evento_data.setError("Insira uma data válida");
            return;
        } else if (TextUtils.isEmpty(inicioEventoHora)) {
            inicio_evento_hora.setError("Insira uma hora válida");
            return;
        } else if (TextUtils.isEmpty(fimEventoData)) {
            fim_evento_data.setError("Insira uma data válida");
            return;
        } else if (TextUtils.isEmpty(fimEventoHora)) {
            fim_evento_hora.setError("Insira uma hora válida");
            return;
        }

        String userId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

        if(atendimento){
            //Pega o valor e local do evento
            valorEvento = valor_evento.getText().toString();
            localEvento = local_evento.getText().toString();


            /*
             * Valor do evento é opcional, caso
             * seja preenchido, é adicionado ao Map
             */
            if (!TextUtils.isEmpty(valorEvento)) {
                NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                double valor = nf.parse (valorEvento).doubleValue();
                scheduleItems.put("price", valor);
            }

            /*
             * Endereço do evento é opcional, caso
             * seja preenchido, é adicionado ao Map
             */
            if (!TextUtils.isEmpty(localEvento)) {
                Boolean addres_active, default_address;
                String addressName, addressType, city, country, neighborhood, number, state, street, zipCode;
                double latitude, longitude;

                addres_active = true;
                addressName = "Endereço";
                addressType = "Event";
                city = endereçoViewModel.getCidade().getValue();
                country = endereçoViewModel.getPais().getValue();
                neighborhood = endereçoViewModel.getBairro().getValue();
                number = endereçoViewModel.getNumero().getValue();
                state = endereçoViewModel.getEstado().getValue();
                street = endereçoViewModel.getRua().getValue();
                zipCode = endereçoViewModel.getCep().getValue();
                longitude = endereçoViewModel.getLongitude().getValue();
                latitude = endereçoViewModel.getLatitude().getValue();

                final Address address = new Address(addres_active, addressName, addressType,
                        city, country, neighborhood, number, state, street, userId , zipCode, latitude, longitude);

                scheduleItems.put("address", address);
            }

            servicesEvent = ServicosBase.getInstance().getServicos_selecionados();
            if(servicesEvent != null && !servicesEvent.isEmpty()){
                scheduleItems.put("services", servicesEvent);
            }

            scheduleItems.put("active", true);
            scheduleItems.put("title", tituloEvento);
            scheduleItems.put("hourBegin", dataInicio.getTime());
            scheduleItems.put("hourEnd", dataFim.getTime());
            scheduleItems.put("professionalId", userId);
            scheduleItems.put("scheduleId", FirebaseService.getFirebaseAuth().getCurrentUser().getUid());

            // Add a new document with a generated ID
            db.collection("scheduleItems")
                    .add(scheduleItems)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            finish();
                            ServicosBase.getInstance().clear();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } else {
            Map<String, Object> customLocks = new HashMap<>();
            customLocks.put("title", tituloEvento);
            customLocks.put("startDate", dataInicio.getTime());
            customLocks.put("endDate", dataFim.getTime());
            String scheduleId = ConfigAgendaBase.getInstance().getScheduleId();

            // Add a new document with a generated ID
            db.collection("schedules").document(scheduleId)
                    .update("customLocks", FieldValue.arrayUnion(customLocks)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Horário bloqueado, adicionado!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void spinnerControl() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    atendimento = false;
                    valor_evento.setVisibility(View.INVISIBLE);
                    local_evento.setVisibility(View.INVISIBLE);
                    tv_servicos.setVisibility(View.INVISIBLE);
                    tv_servicos_op.setVisibility(View.INVISIBLE);
                    lista_servicos_vazia.setVisibility(View.INVISIBLE);
                    tv_local.setVisibility(View.INVISIBLE);
                    tv_valor.setVisibility(View.INVISIBLE);
                    tv_servicos.setVisibility(View.INVISIBLE);
                    tv_servicos_op.setVisibility(View.INVISIBLE);
                }else if(position == 0){
                    atendimento = true;
                    valor_evento.setVisibility(View.VISIBLE);
                    local_evento.setVisibility(View.VISIBLE);
                    tv_servicos.setVisibility(View.VISIBLE);
                    tv_servicos_op.setVisibility(View.VISIBLE);
                    lista_servicos_vazia.setVisibility(View.VISIBLE);
                    tv_local.setVisibility(View.VISIBLE);
                    tv_valor.setVisibility(View.VISIBLE);
                    tv_servicos.setVisibility(View.VISIBLE);
                    tv_servicos_op.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void timeControl() {
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);

        inicio_evento_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaInicio = hourOfDay;
                        minutoInicio = minute;
                        inicio_evento_hora.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hora, minuto, true);

                timePickerDialog.show();
            }
        });

        inicio_evento_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        diaInicio = dayOfMonth;
                        mesInicio = month;
                        anoInicio = year;
                        inicio_evento_data.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
                    }
                },ano, mes, dia);
                datePickerDialog.show();
            }
        });

        fim_evento_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaFim = hourOfDay;
                        minutoFim = minute;
                        fim_evento_hora.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hora, minuto, true);

                timePickerDialog.show();
            }
        });

        fim_evento_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        diaFim = dayOfMonth;
                        mesFim = month;
                        anoFim = year;
                        fim_evento_data.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
                    }
                },ano, mes, dia);
                datePickerDialog.show();
            }
        });
    }

    public void placesAutocompleteControl() {
        Places.initialize(this, "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        local_evento.setFocusable(false);
        local_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS);
                Intent placesIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(context);
                startActivityForResult(placesIntent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            local_evento.setText(place.getAddress());

             /*
                Salva a latitude e longitude do endereço na ViewModel
             */
            endereçoViewModel.setLatitude(place.getLatLng().latitude);
            endereçoViewModel.setLongitude(place.getLatLng().longitude);

            for (AddressComponent list_components: place.getAddressComponents().asList()) {

                String type = list_components.getTypes().get(0);

                if(TextUtils.equals(type, "route")){
                    endereçoViewModel.setRua(list_components.getName());
                } else if(TextUtils.equals(type, "country")){
                    endereçoViewModel.setPais(list_components.getName());
                } else if(TextUtils.equals(type, "street_number")){
                    endereçoViewModel.setNumero(list_components.getName());
                } else if(TextUtils.equals(type, "postal_code") || TextUtils.equals(type, "postal_code_prefix")){
                    endereçoViewModel.setCep(list_components.getName());
                } else if(TextUtils.equals(type, "sublocality") || TextUtils.equals(type, "sublocality_level_1")){
                    endereçoViewModel.setBairro(list_components.getName());
                } else if(TextUtils.equals(type, "administrative_area_level_2") || TextUtils.equals(type, "locality")){
                    endereçoViewModel.setCidade(list_components.getName());
                } else if(TextUtils.equals(type, "administrative_area_level_1")){
                    endereçoViewModel.setEstado(list_components.getShortName());
                }
            }

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializarComponentes() {
        calendar = Calendar.getInstance();
        recyclerView = findViewById(R.id.reciclerView_servicoEvento);
        spinner = (Spinner) findViewById(R.id.spinner_tipo_evento);
        btn_avancar = (Button) findViewById(R.id.btn_concluir_criar_evento);
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar_criar_evento);
        local_evento = (EditText) findViewById(R.id.local_evento);
        valor_evento = (EditText) findViewById(R.id.valor_evento);
        titulo_evento = (EditText) findViewById(R.id.titulo_evento);
        inicio_evento_hora = (EditText) findViewById(R.id.inicio_evento_hora);
        inicio_evento_data = (EditText) findViewById(R.id.inicio_evento_data);
        fim_evento_data = (EditText) findViewById(R.id.fim_evento_data);
        fim_evento_hora = (EditText) findViewById(R.id.fim_evento_hora);
        tv_local = (TextView) findViewById(R.id.tv_local);
        tv_valor = (TextView) findViewById(R.id.tv_valor);
        tv_servicos = (TextView) findViewById(R.id.tv_servicos);
        tv_servicos_op = (TextView) findViewById(R.id.tv_servicos_op);
        tv_add_servico = (TextView) findViewById(R.id.tv_add_setvico);
        lista_servicos_vazia = (LinearLayout) findViewById(R.id.lista_servicos_vazia);
    }
}