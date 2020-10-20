package br.com.justworks.prestador.ServicoAki.Menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import org.joda.time.DateTime;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.DatePickerListener;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.HorizontalPicker;
import br.com.justworks.prestador.ServicoAki.R;

public class AgendaFragment extends Fragment implements DatePickerListener {

    private ImageView criar_evento;

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

        criar_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_AgendaMenu_to_criarEventoEtapa_1);
            }
        });

    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.i("HorizontalPicker","Fecha seleccionada="+dateSelected.toString());
    }

    private void inicializarComponentes(View view) {
        criar_evento = (ImageView) view.findViewById(R.id.img_criar_evento);
    }
}