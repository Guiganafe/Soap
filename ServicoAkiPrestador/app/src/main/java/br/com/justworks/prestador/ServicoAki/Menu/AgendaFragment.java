package br.com.justworks.prestador.ServicoAki.Menu;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.joda.time.DateTime;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.DatePickerListener;
import br.com.justworks.prestador.ServicoAki.HorizontalPicker.HorizontalPicker;
import br.com.justworks.prestador.ServicoAki.R;

public class AgendaFragment extends Fragment implements DatePickerListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
}