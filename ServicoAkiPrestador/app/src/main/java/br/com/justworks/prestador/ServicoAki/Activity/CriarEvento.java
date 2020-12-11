package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Adapter.ServiceListEventoAdapter;
import br.com.justworks.prestador.ServicoAki.Adapter.ServicesListAdapter;
import br.com.justworks.prestador.ServicoAki.BuildConfig;
import br.com.justworks.prestador.ServicoAki.Enum.userEnum;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.ServicesDocument;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class CriarEvento extends AppCompatActivity implements ServiceListEventoAdapter.onServiceListenner{

    private static final String TAG = "CriarEvento";

    private Spinner spinner;
    private Button btn_avancar, btn_cancelar;
    private Calendar calendar;
    private int dia,mes, ano, hora, minuto;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private EndereçoViewModel endereçoViewModel;
    private EditText titulo_evento, inicio_evento_hora, inicio_evento_data, fim_evento_hora, fim_evento_data, tipo_evento, valor_evento, local_evento, servicos_evento;
    private TextView tv_local, tv_local_op, tv_valor, tv_valor_op, tv_servicos, tv_servicos_op;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ServicoViewModel servicoViewModel;
    private ArrayList<ServiceUser> servicesUser = new ArrayList<>();
    private ArrayList<ServiceUser> servicesEvent = new ArrayList<>();
    private Context context = this;
    private ImageView addServico;
    private ServiceListEventoAdapter.onServiceListenner serviceListenner = (ServiceListEventoAdapter.onServiceListenner) this.context;

    private int horaInicio, minutoInicio, diaInicio, mesInicio, anoInicio, horaFim, minutoFim, diaFim, mesFim, anoFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        inicializarComponentes();

        setUpReciclerView();

        spinnerControl();

        placesAutocompleteControl();

        timeControl();

        clickControl();

        textWatcherController();
    }

    private void textWatcherController() {
        servicos_evento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<ServiceUser> serviceUsersFiltered = new ArrayList<>();

        for(ServiceUser item: servicesUser){
            if(item.getName().getPtbr().toLowerCase().contains(text.toLowerCase())){
                serviceUsersFiltered.add(item);
            }
        }

        adapter = new ServiceListEventoAdapter(serviceUsersFiltered, context, serviceListenner);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void setUpReciclerView() {
        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    servicesUser = documentSnapshot.toObject(ServicesDocument.class).services;
                    adapter = new ServiceListEventoAdapter(servicesUser, context, serviceListenner);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onServiceClick(int position) {
        servicesEvent.add(servicesUser.get(position));
        Toast.makeText(context, "Serviço adicionado ao evento", Toast.LENGTH_SHORT).show();
    }

    private void clickControl() {
        btn_avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarEvento();
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        valorEvento = valor_evento.getText().toString();
        localEvento = local_evento.getText().toString();
        Boolean active = true;

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
        } else if (TextUtils.isEmpty(valorEvento)) {
            valor_evento.setError("Insira um valor válido");
            return;
        }
        if (TextUtils.isEmpty(localEvento)) {
            local_evento.setError("Insira uma localização válida");
            return;
        }

        Calendar dataInicio = Calendar.getInstance(Locale.getDefault()), dataFim = Calendar.getInstance(Locale.getDefault());
        dataInicio.set(anoInicio, mesInicio, diaInicio, horaInicio, minutoInicio);
        final DateFormat df = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss z");
        String dataInicioFinal = df.format(dataInicio.getTime());

        dataFim.set(anoFim, mesFim, diaFim, horaFim, minutoFim);
        String dataFimFinal = df.format(dataFim.getTime());

        Boolean addres_active, default_address;
        String addressName, addressType, city, country, neighborhood, number, state, street, userId, zipCode;
        double latitude, longitude;

        addres_active = true;
        addressName = "Endereço";
        addressType = "Event";
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

        final Address address = new Address(addres_active, addressName, addressType, city, country, neighborhood, number, state, street, userId , zipCode, latitude, longitude);

        Map<String, Object> scheduleItems = new HashMap<>();
        scheduleItems.put("title", tituloEvento);
        scheduleItems.put("hourBegin", dataInicio.getTime());
        scheduleItems.put("hourEnd", dataFim.getTime());
        scheduleItems.put("price", Double.parseDouble(valorEvento));
        scheduleItems.put("address", address);
        scheduleItems.put("professionalId", userId);
        scheduleItems.put("scheduleId", FirebaseService.getFirebaseAuth().getCurrentUser().getUid());
        scheduleItems.put("active", active);
        scheduleItems.put("services", servicesEvent);

        // Add a new document with a generated ID
        db.collection("scheduleItems")
                .add(scheduleItems)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void spinnerControl() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    valor_evento.setVisibility(View.INVISIBLE);
                    local_evento.setVisibility(View.INVISIBLE);
                    servicos_evento.setVisibility(View.INVISIBLE);
                    tv_local.setVisibility(View.INVISIBLE);
                    tv_local_op.setVisibility(View.INVISIBLE);
                    tv_valor.setVisibility(View.INVISIBLE);
                    tv_valor_op.setVisibility(View.INVISIBLE);
                    tv_servicos.setVisibility(View.INVISIBLE);
                    tv_servicos_op.setVisibility(View.INVISIBLE);
                }else if(position == 0){
                    valor_evento.setVisibility(View.VISIBLE);
                    local_evento.setVisibility(View.VISIBLE);
                    servicos_evento.setVisibility(View.VISIBLE);
                    tv_local.setVisibility(View.VISIBLE);
                    tv_local_op.setVisibility(View.VISIBLE);
                    tv_valor.setVisibility(View.VISIBLE);
                    tv_valor_op.setVisibility(View.VISIBLE);
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
                timePickerDialog = new TimePickerDialog(CriarEvento.this, new TimePickerDialog.OnTimeSetListener() {
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
                datePickerDialog = new DatePickerDialog(CriarEvento.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        diaInicio = dayOfMonth;
                        mesInicio = month;
                        anoInicio = year;
                        inicio_evento_data.setText(String.format("%02d/%02d/%04d", dayOfMonth, month, year));
                    }
                },ano, mes, dia);
                datePickerDialog.show();
            }
        });

        fim_evento_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(CriarEvento.this, new TimePickerDialog.OnTimeSetListener() {
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
                datePickerDialog = new DatePickerDialog(CriarEvento.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        diaFim = dayOfMonth;
                        mesFim = month;
                        anoFim = year;
                        fim_evento_data.setText(String.format("%02d/%02d/%04d", dayOfMonth, month, year));
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
                Intent placesIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(CriarEvento.this);
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
            Toast.makeText(CriarEvento.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializarComponentes() {
        endereçoViewModel = new ViewModelProvider(this).get(EndereçoViewModel.class);
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
        servicos_evento = (EditText) findViewById(R.id.servicos_evento);
        tv_local = (TextView) findViewById(R.id.tv_local);
        tv_local_op = (TextView) findViewById(R.id.tv_local_op);
        tv_valor = (TextView) findViewById(R.id.tv_valor);
        tv_valor_op = (TextView) findViewById(R.id.tv_valor_op);
        tv_servicos = (TextView) findViewById(R.id.tv_servicos);
        tv_servicos_op = (TextView) findViewById(R.id.tv_servicos_op);
    }
}