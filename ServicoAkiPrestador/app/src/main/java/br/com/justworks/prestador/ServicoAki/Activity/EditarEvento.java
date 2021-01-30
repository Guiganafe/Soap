package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Adapter.ServiceSelectedAdapter;
import br.com.justworks.prestador.ServicoAki.Base.AgendaBase;
import br.com.justworks.prestador.ServicoAki.Base.ServicosBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServiceEventListViewModel;

public class EditarEvento extends AppCompatActivity implements ServiceSelectedAdapter.onServiceSelectedListenner{

    private Button btn_salvar, btn_cancelar;
    private Calendar calendar;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private EditText titulo_evento, inicio_evento_hora, inicio_evento_data, fim_evento_hora, fim_evento_data, valor_evento, local_evento;
    private TextView tv_add_servico;
    private LinearLayout lista_servicos_vazia;
    private ServiceSelectedAdapter.onServiceSelectedListenner serviceListenner = (ServiceSelectedAdapter.onServiceSelectedListenner) this;

    private boolean servicoAdd = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<ServiceUser> servicesUser = new ArrayList<>();
    private ArrayList<ServiceUser> servicesEvent = new ArrayList<>();

    private int horaInicio, minutoInicio, diaInicio, mesInicio, anoInicio, horaFim, minutoFim, diaFim, mesFim, anoFim;
    private ScheduleItems scheduleItem;
    private int position;
    private String scheduleItemId;

    Map<String, Object> scheduleItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        inicializarComponentes();
        carregarDadosController();
        onClickController();
        placesAutocompleteControl();
        recyclerViewController();
        timeControl();
    }

    private void recyclerViewController() {
        servicesUser = ServicosBase.getInstance().getServicos_selecionados();
        if(servicesUser != null && servicesUser.size() >= 1){
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ServiceSelectedAdapter(servicesUser, EditarEvento.this, serviceListenner);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(EditarEvento.this));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewController();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewController();
    }

    private void timeControl() {
        inicio_evento_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(EditarEvento.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaInicio = hourOfDay;
                        minutoInicio = minute;
                        inicio_evento_hora.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, horaInicio, minutoInicio, true);

                timePickerDialog.show();
            }
        });

        inicio_evento_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditarEvento.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        diaInicio = dayOfMonth;
                        mesInicio = month;
                        anoInicio = year;
                        inicio_evento_data.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
                    }
                },anoInicio, mesInicio-1, diaInicio);
                datePickerDialog.show();
            }
        });

        fim_evento_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(EditarEvento.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaFim = hourOfDay;
                        minutoFim = minute;
                        fim_evento_hora.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, horaFim, minutoFim, true);

                timePickerDialog.show();
            }
        });

        fim_evento_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditarEvento.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        diaFim = dayOfMonth;
                        mesFim = month;
                        anoFim = year;
                        fim_evento_data.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
                    }
                },anoFim, mesFim-1, diaFim);
                datePickerDialog.show();
            }
        });
    }

    private void placesAutocompleteControl() {
        Places.initialize(this, "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        local_evento.setFocusable(false);
        local_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS);
                Intent placesIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(EditarEvento.this);
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
            scheduleItem.getAddress().setLatitude(place.getLatLng().latitude);
            scheduleItem.getAddress().setLongitude(place.getLatLng().longitude);

            for (AddressComponent list_components: place.getAddressComponents().asList()) {

                String type = list_components.getTypes().get(0);

                if(TextUtils.equals(type, "route")){
                    scheduleItem.getAddress().setStreet(list_components.getName());
                } else if(TextUtils.equals(type, "country")){
                    scheduleItem.getAddress().setCountry(list_components.getName());
                } else if(TextUtils.equals(type, "street_number")){
                    scheduleItem.getAddress().setNumber(list_components.getName());
                } else if(TextUtils.equals(type, "postal_code") || TextUtils.equals(type, "postal_code_prefix")){
                    scheduleItem.getAddress().setZipCode(list_components.getName());
                } else if(TextUtils.equals(type, "sublocality") || TextUtils.equals(type, "sublocality_level_1")){
                    scheduleItem.getAddress().setNeighborhood(list_components.getName());
                } else if(TextUtils.equals(type, "administrative_area_level_2") || TextUtils.equals(type, "locality")){
                    scheduleItem.getAddress().setCity(list_components.getName());
                } else if(TextUtils.equals(type, "administrative_area_level_1")){
                    scheduleItem.getAddress().setState(list_components.getShortName());
                }
            }

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickController() {
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgendaBase.getInstance().deleteScheduleItemsByDay();
                ServicosBase.getInstance().clear();
            finish();
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarEvento();
            }
        });

        tv_add_servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selecionarEvento = new Intent(EditarEvento.this, SelecionarEvento.class);
                startActivity(selecionarEvento);
            }
        });
    }

    private void salvarEvento() {
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

        Calendar dataInicio = Calendar.getInstance(Locale.getDefault()), dataFim = Calendar.getInstance(Locale.getDefault());
        dataInicio.set(anoInicio, mesInicio-1, diaInicio, horaInicio, minutoInicio);
        final DateFormat df = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss z");
        String dataInicioFinal = df.format(dataInicio.getTime());

        dataFim.set(anoFim, mesFim-1, diaFim, horaFim, minutoFim);
        String dataFimFinal = df.format(dataFim.getTime());
        String userId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

        valorEvento = valor_evento.getText().toString();
        localEvento = local_evento.getText().toString();

        if (!TextUtils.isEmpty(valorEvento)) {
            String valorDoEvento = valorEvento.replace(",", ".");
            scheduleItems.put("price", Double.parseDouble(valorDoEvento));
        }

        if (!TextUtils.isEmpty(localEvento)) {
            Boolean addres_active, default_address;
            String addressName, addressType, city, country, neighborhood, number, state, street, zipCode;
            double latitude, longitude;

            addres_active = true;
            addressName = "Endereço";
            addressType = "Event";
            city = scheduleItem.getAddress().getCity();
            country =scheduleItem.getAddress().getCountry();
            neighborhood = scheduleItem.getAddress().getNeighborhood();
            number = scheduleItem.getAddress().getNumber();
            state = scheduleItem.getAddress().getState();
            street = scheduleItem.getAddress().getStreet();
            zipCode = scheduleItem.getAddress().getZipCode();
            longitude = scheduleItem.getAddress().getLongitude();
            latitude = scheduleItem.getAddress().getLatitude();

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
        db.collection("scheduleItems").document(scheduleItemId).update(scheduleItems).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditarEvento.this, "Evento atualizado", Toast.LENGTH_SHORT).show();
//                AgendaBase.getInstance().removeScheduleItemByDay(position);
//                AgendaBase.getInstance().addScheduleItem(scheduleItem);
//                AgendaBase.getInstance().updateScheduleItem(scheduleItem, position);
                finish();
            }
        });
    }

    private void carregarDadosController() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        scheduleItem = AgendaBase.getInstance().getScheduleItemsListByDay().get(position);
        scheduleItemId = AgendaBase.getInstance().getScheduleItemsIdByDay().get(position);

        //Recebe o título do evento
        titulo_evento.setText(scheduleItem.getTitle());

        //Carrega e converte os dados da hora de inicio
        horaInicio = scheduleItem.getHourBegin().toDate().getHours();
        minutoInicio = scheduleItem.getHourBegin().toDate().getMinutes();

        if(horaInicio < 9){
            if(minutoInicio < 9){
                inicio_evento_hora.setText(String.format(Locale.getDefault(), "0%d:0%d", horaInicio, minutoInicio));
            } else {
                inicio_evento_hora.setText(String.format(Locale.getDefault(), "0%d:%d", horaInicio, minutoInicio));
            }
        } else {
            if(minutoInicio < 9){
                inicio_evento_hora.setText(String.format(Locale.getDefault(), "%d:0%d", horaInicio, minutoInicio));
            } else {
                inicio_evento_hora.setText(String.format(Locale.getDefault(), "%d:%d", horaInicio, minutoInicio));
            }
        }

        //Carrega e converte os dados da data inicial do evento
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTimeInMillis(scheduleItem.getHourBegin().toDate().getTime());
        diaInicio = calInicio.get(Calendar.DAY_OF_MONTH);
        mesInicio = calInicio.get(Calendar.MONTH) + 1;
        anoInicio = calInicio.get(Calendar.YEAR);

        if(diaInicio < 9){
            if(mesInicio < 9){
                inicio_evento_data.setText(String.format(Locale.getDefault(),"0%d/0%d/%d", diaInicio, mesInicio, anoInicio));
            } else {
                inicio_evento_data.setText(String.format(Locale.getDefault(),"0%d/%d/%d", diaInicio, mesInicio, anoInicio));
            }
        } else {
            if(mesInicio < 9){
                inicio_evento_data.setText(String.format(Locale.getDefault(),"%d/0%d/%d", diaInicio, mesInicio, anoInicio));
            } else {
                inicio_evento_data.setText(String.format(Locale.getDefault(),"%d/%d/%d", diaInicio, mesInicio, anoInicio));
            }
        }

        //Carrega e converte os dados da hora de finalização
        horaFim = scheduleItem.getHourEnd().toDate().getHours();
        minutoFim = scheduleItem.getHourEnd().toDate().getMinutes();

        if(horaFim < 9){
            if(minutoFim < 9){
                fim_evento_hora.setText(String.format(Locale.getDefault(), "0%d:0%d", horaFim, minutoFim));
            } else {
                fim_evento_hora.setText(String.format(Locale.getDefault(), "0%d:%d", horaFim, minutoFim));
            }
        } else {
            if(minutoFim < 9){
                fim_evento_hora.setText(String.format(Locale.getDefault(), "%d:0%d", horaFim, minutoFim));
            } else {
                fim_evento_hora.setText(String.format(Locale.getDefault(), "%d:%d", horaFim, minutoFim));
            }
        }

        //Carrega e converte os dados da data final do evento
        Calendar calFim = Calendar.getInstance();
        calFim.setTimeInMillis(scheduleItem.getHourEnd().toDate().getTime());
        diaFim = calFim.get(Calendar.DAY_OF_MONTH);
        mesFim = calFim.get(Calendar.MONTH) + 1;
        anoFim = calFim.get(Calendar.YEAR);

        if(diaFim < 9){
            if(mesFim < 9){
                fim_evento_data.setText(String.format(Locale.getDefault(),"0%d/0%d/%d", diaFim, mesFim, anoFim));
            } else {
                fim_evento_data.setText(String.format(Locale.getDefault(),"0%d/%d/%d", diaFim, mesFim, anoFim));
            }
        } else {
            if(mesFim < 9){
                fim_evento_data.setText(String.format(Locale.getDefault(),"%d/0%d/%d", diaFim, mesFim, anoFim));
            } else {
                fim_evento_data.setText(String.format(Locale.getDefault(),"%d/%d/%d", diaFim, mesFim, anoFim));
            }
        }

        //Caso o valor não seja nulo, é exibido
        String valor = scheduleItem.getPrice().toString();
        if(valor != null && !valor.equals("")){
            valor_evento.setText(valor);
        }

        //Caso o local não seja nulo, é exibido
        String rua = scheduleItem.getAddress().getStreet();
        String numero = scheduleItem.getAddress().getNumber();
        String bairro = scheduleItem.getAddress().getNeighborhood();

        if(rua != null){
            if(numero != null){
                if(bairro != null){
                    local_evento.setText(String.format("%s, %s, %s", rua, numero, bairro));
                } else {
                    local_evento.setText(String.format("%s, %s", rua, numero));
                }
            } else {
                if(bairro != null){
                    local_evento.setText(String.format("%s, %s", rua, bairro));
                } else {
                    local_evento.setText(String.format("%s", rua));
                }
            }
        }

        if(!servicoAdd){
            ArrayList<ServiceUser> servicesEvent = scheduleItem.getServices();
            if(servicesEvent != null && servicesEvent.size() > 0){
                for(int i = 0; i < servicesEvent.size(); i++)
                    ServicosBase.getInstance().addServico_selecionado(servicesEvent.get(i));
            }
            servicoAdd = true;
        }
    }

    @Override
    public void onServiceClick(int position) {
        Toast.makeText(EditarEvento.this, "Serviço" + servicesUser.get(position).getName().getPtbr() + " removido do evento", Toast.LENGTH_SHORT).show();
        ServicosBase.getInstance().removeServico_selecionado(servicesUser.get(position));
        recyclerViewController();
    }

    private void inicializarComponentes() {
        calendar = Calendar.getInstance();

        recyclerView = findViewById(R.id.reciclerView_servicoEvento_edicao);

        btn_salvar = (Button) findViewById(R.id.btn_concluir_criar_evento_edicao);
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar_criar_evento_edicao);

        local_evento = (EditText) findViewById(R.id.local_evento_edicao);
        valor_evento = (EditText) findViewById(R.id.valor_evento_edicao);
        titulo_evento = (EditText) findViewById(R.id.titulo_evento_edicao);
        inicio_evento_hora = (EditText) findViewById(R.id.inicio_evento_hora_edicao);
        inicio_evento_data = (EditText) findViewById(R.id.inicio_evento_data_edicao);
        fim_evento_data = (EditText) findViewById(R.id.fim_evento_data_edicao);
        fim_evento_hora = (EditText) findViewById(R.id.fim_evento_hora_edicao);

        tv_add_servico = (TextView) findViewById(R.id.tv_add_setvico_edicao);

        lista_servicos_vazia = (LinearLayout) findViewById(R.id.lista_servicos_vazia_edicao);
    }
}