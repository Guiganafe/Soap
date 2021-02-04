package br.com.justworks.prestador.ServicoAki.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;

import br.com.justworks.prestador.ServicoAki.Model.CustomLocks;
import br.com.justworks.prestador.ServicoAki.Model.Schedules;

public class ValidarData {

    private static boolean diaDeTrabalhoValido = false;
    private static boolean horarioDeTrabalhoValido = true;

    /*
      Método que valida o horário do evento em comparação
      com os dias de trabalho.
     */
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

    /*
      Método que valida o horário do evento em comparação
      com os horários bloqueados.

      ex => LKI 13:00 / LKF 14:00
     */
    public static boolean isHorarioBloqueado(@NotNull Schedules schedule, Calendar inicio, Calendar fim){
        Calendar lockStart = Calendar.getInstance(Locale.getDefault()), lockEnd = Calendar.getInstance(Locale.getDefault());
        for (CustomLocks locks: schedule.getCustomLocks()) {
            lockStart.setTime(locks.getStartDate().toDate());
            lockEnd.setTime(locks.getEndDate().toDate());

            /*
             Horário de início do evento depois do inicio do horário bloqueado => HI 13:01
             Horário de fim do evento antes do fim do horário bloqueado => HF 14:01
             */
            if(inicio.after(lockStart)) {
                if (fim.before(lockEnd)) {
                    horarioDeTrabalhoValido = false;
                    break;
                } else {
                    horarioDeTrabalhoValido = true;
                }
            }

            /*
             Horário de início do evento antes do início do horário bloqueado => HI 12:59
             Horário de fim do evento depois do fim do horário bloqueado => 14:01
             */
            if(inicio.before(lockStart)){
                if (fim.after(lockEnd)) {
                    horarioDeTrabalhoValido = false;
                    break;
                } else {
                    horarioDeTrabalhoValido = true;
                }
            }

            /*
             Horário de início do evento antes do início do horário bloqueado => 12:59
             Horário de fim do evento depois do início do horário bloqueado => 13:01
             Horário de fim do evento antes do fim do horário bloqueado => 13:01
             */
            if(inicio.before(lockStart)){
                if (fim.after(lockStart)){
                    if(fim.before(lockEnd)){
                        horarioDeTrabalhoValido = false;
                        break;
                    } else {
                        horarioDeTrabalhoValido = true;
                    }
                }
            }

             /*
             Horário de início do evento depois do início do horário bloqueado => 13:59
             Horário de início do evento antes do fim do horário bloqueado => 13:59
             Horário de fim do evento depois do fim do horário bloqueado => 14:01
             */
            if(inicio.after(lockStart)){
                if(inicio.before(lockEnd)){
                    if(fim.after(lockEnd)){
                        horarioDeTrabalhoValido = false;
                        break;
                    } else {
                        horarioDeTrabalhoValido = true;
                    }
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
