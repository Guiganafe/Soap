package br.com.justworks.prestador.ServicoAki.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
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

import br.com.justworks.prestador.ServicoAki.Adapter.ServiceListEventoAdapter;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.ServicesDocument;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServiceEventListViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServicoViewModel;

public class addEvento extends Fragment implements ServiceListEventoAdapter.onServiceEventoListenner{

    private Spinner spinner;
    private Button btn_avancar, btn_cancelar;
    private Calendar calendar;
    private int dia,mes, ano, hora, minuto;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private EndereçoViewModel endereçoViewModel;
    private ServiceEventListViewModel serviceEventListViewModel;
    private EditText titulo_evento, inicio_evento_hora, inicio_evento_data, fim_evento_hora, fim_evento_data, tipo_evento, valor_evento, local_evento, servicos_evento;
    private TextView tv_local, tv_valor, tv_servicos, tv_servicos_op, tv_add_servico;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<ServiceUser> servicesUser = new ArrayList<>();
    private ArrayList<ServiceUser> servicesEvent = new ArrayList<>();
    private Context context = getContext();
    private ServiceListEventoAdapter.onServiceEventoListenner serviceListenner = (ServiceListEventoAdapter.onServiceEventoListenner) this.context;

    private int horaInicio, minutoInicio, diaInicio, mesInicio, anoInicio, horaFim, minutoFim, diaFim, mesFim, anoFim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        endereçoViewModel = new ViewModelProvider(this).get(EndereçoViewModel.class);
        serviceEventListViewModel = new ViewModelProvider(requireActivity()).get(ServiceEventListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_evento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        setUpReciclerView();

        spinnerControl();

        placesAutocompleteControl();

        timeControl();

        clickControl();
    }

    @Override
    public void onStart() {
        super.onStart();
            setUpReciclerView();
    }

    @Override
    public void onStop() {
        super.onStop();
        setUpReciclerView();
    }

    //    private void filter(String text) {
//        ArrayList<ServiceUser> serviceUsersFiltered = new ArrayList<>();
//
//        for(ServiceUser item: servicesUser){
//            if(item.getName().getPtbr().toLowerCase().contains(text.toLowerCase())){
//                serviceUsersFiltered.add(item);
//            }
//        }
//
//        adapter = new ServiceListEventoAdapter(serviceUsersFiltered, context, serviceListenner);
//        recyclerView.setHasFixedSize(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(adapter);
//    }

    private void setUpReciclerView() {
//        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    servicesUser = documentSnapshot.toObject(ServicesDocument.class).services;
//                    adapter = new ServiceListEventoAdapter(servicesUser, context, serviceListenner);
//                    recyclerView.setHasFixedSize(false);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//        });

        servicesUser = serviceEventListViewModel.getServices_selected_list().getValue();
        if(servicesUser != null && servicesUser.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ServiceListEventoAdapter(servicesUser, context, serviceListenner);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
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
                requireActivity().finish();
            }
        });
        tv_add_servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_addEvento_to_addServico);
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
                        requireActivity().finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void spinnerControl() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    valor_evento.setVisibility(View.INVISIBLE);
                    local_evento.setVisibility(View.INVISIBLE);
                    //servicos_evento.setVisibility(View.INVISIBLE);
                    tv_local.setVisibility(View.INVISIBLE);
                    tv_valor.setVisibility(View.INVISIBLE);
                    tv_servicos.setVisibility(View.INVISIBLE);
                    tv_servicos_op.setVisibility(View.INVISIBLE);
                }else if(position == 0){
                    valor_evento.setVisibility(View.VISIBLE);
                    local_evento.setVisibility(View.VISIBLE);
                    //servicos_evento.setVisibility(View.VISIBLE);
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
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
        Places.initialize(getContext(), "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        local_evento.setFocusable(false);
        local_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS);
                Intent placesIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
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
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializarComponentes(View view) {
        calendar = Calendar.getInstance();
        recyclerView = view.findViewById(R.id.reciclerView_servicoEvento);
        spinner = (Spinner) view.findViewById(R.id.spinner_tipo_evento);
        btn_avancar = (Button) view.findViewById(R.id.btn_concluir_criar_evento);
        btn_cancelar = (Button) view.findViewById(R.id.btn_cancelar_criar_evento);
        local_evento = (EditText) view.findViewById(R.id.local_evento);
        valor_evento = (EditText) view.findViewById(R.id.valor_evento);
        titulo_evento = (EditText) view.findViewById(R.id.titulo_evento);
        inicio_evento_hora = (EditText) view.findViewById(R.id.inicio_evento_hora);
        inicio_evento_data = (EditText) view.findViewById(R.id.inicio_evento_data);
        fim_evento_data = (EditText) view.findViewById(R.id.fim_evento_data);
        fim_evento_hora = (EditText) view.findViewById(R.id.fim_evento_hora);
        tv_local = (TextView) view.findViewById(R.id.tv_local);
        tv_valor = (TextView) view.findViewById(R.id.tv_valor);
        tv_servicos = (TextView) view.findViewById(R.id.tv_servicos);
        tv_servicos_op = (TextView) view.findViewById(R.id.tv_servicos_op);
        tv_add_servico = (TextView) view.findViewById(R.id.tv_add_setvico);
    }
}