package br.com.justworks.prestador.ServicoAki.HorizontalPicker;

import org.joda.time.DateTime;

public interface DatePickerListener {
    void onDateSelected(DateTime dateSelected);
}