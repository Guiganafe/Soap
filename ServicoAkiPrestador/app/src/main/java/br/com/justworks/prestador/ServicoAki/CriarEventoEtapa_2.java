package br.com.justworks.prestador.ServicoAki;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.joda.time.DateTime;

import br.com.justworks.prestador.ServicoAki.HorizontalPicker.DatePickerListener;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.HorizontalPicker;

public class CriarEventoEtapa_2 extends Fragment implements DatePickerListener {

    private Button btn_voltar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.criar_evento_etapa_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        HorizontalPicker picker = (HorizontalPicker) view.findViewById(R.id.datePicker2);
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

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void inicializarComponentes(View view) {
        btn_voltar = (Button) view.findViewById(R.id.btn_voltar_etapa_1);
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.i("HorizontalPicker","Fecha seleccionada="+dateSelected.toString());
    }
}