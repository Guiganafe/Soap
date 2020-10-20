package br.com.justworks.prestador.ServicoAki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class CriarEventoEtapa_1 extends Fragment {

    private Spinner spinner;
    private Button btn_avancar;
    private EditText titulo_evento, tipo_evento, valor_evento, local_evento, servicos_evento;
    private TextView tv_local, tv_local_op, tv_valor, tv_valor_op, tv_servicos, tv_servicos_op;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.criar_evento_etapa_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btn_avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_criarEventoEtapa_1_to_criarEventoEtapa_2);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "position " + position, Toast.LENGTH_SHORT).show();
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

        Places.initialize(getContext(), "AIzaSyDju8DWlyeC9dF7Yn3_GlNNOrzuQbGRKjI");

        local_evento.setFocusable(false);
        local_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
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
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializarComponentes(View view) {
        spinner = (Spinner) view.findViewById(R.id.spinner_tipo_evento);
        btn_avancar = (Button) view.findViewById(R.id.btn_avancar_etapa_2);
        local_evento = (EditText) view.findViewById(R.id.local_evento);
        valor_evento = (EditText) view.findViewById(R.id.valor_evento);
        titulo_evento = (EditText) view.findViewById(R.id.titulo_evento);
        servicos_evento = (EditText) view.findViewById(R.id.servicos_evento);
        tv_local = (TextView) view.findViewById(R.id.tv_local);
        tv_local_op = (TextView) view.findViewById(R.id.tv_local_op);
        tv_valor = (TextView) view.findViewById(R.id.tv_valor);
        tv_valor_op = (TextView) view.findViewById(R.id.tv_valor_op);
        tv_servicos = (TextView) view.findViewById(R.id.tv_servicos);
        tv_servicos_op = (TextView) view.findViewById(R.id.tv_servicos_op);
    }
}