package br.com.justworks.prestador.ServicoAki.Util;

import java.util.Calendar;
import java.util.Locale;

import br.com.justworks.prestador.ServicoAki.Model.CustomLocks;
import br.com.justworks.prestador.ServicoAki.Model.Schedules;

public class ValidarData {

    private static boolean diaDeTrabalhoValido = false;
    private static boolean horarioDeTrabalhoValido = true;

    public static boolean isDiaDeTrabalho(Schedules schedule, Calendar inicio){
        String dia = diaDaSemana(inicio);
        for(int i = 0; i < schedule.getServiceDays().size(); i ++){
            if(schedule.getServiceDays().get(i).getDay().equals(dia)){
                diaDeTrabalhoValido = true;
                break;
            }else{
                diaDeTrabalhoValido = false;
            }
        }
        return diaDeTrabalhoValido;
    }

    public static boolean isHorarioBloqueado(Schedules schedule, Calendar inicio, Calendar fim){
        Calendar lockStart = Calendar.getInstance(Locale.getDefault()), lockEnd = Calendar.getInstance(Locale.getDefault());
        for (CustomLocks locks: schedule.getCustomLocks()) {
            lockStart.setTime(locks.getStartDate().toDate());
            lockEnd.setTime(locks.getEndDate().toDate());
            if(inicio.after(lockStart)){
                if(fim.before(lockEnd)){
                    horarioDeTrabalhoValido = false;
                }else{
                    horarioDeTrabalhoValido = true;
                }
            }
        }
        return horarioDeTrabalhoValido;
    }

    public static String diaDaSemana(Calendar inicio){
        int dia = inicio.getTime().getDay();
        String diaDoEvento = "";
        switch (dia){
            case 0:
                diaDoEvento = "Sunday";
                break;
            case 1:
                diaDoEvento = "Monday";
                break;
            case 2:
                diaDoEvento = "Tuesday";
                break;
            case 3:
                diaDoEvento = "Wednesday";
                break;
            case 4:
                diaDoEvento = "Thursday";
                break;
            case 5:
                diaDoEvento = "Friday";
                break;
            case 6:
                diaDoEvento = "Saturday";
                break;
        }
        return diaDoEvento;
    }
}
