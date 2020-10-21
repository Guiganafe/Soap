package br.com.justworks.prestador.ServicoAki.Model;

import java.sql.Timestamp;

public class Schedules {

    private Boolean active;
    private Timestamp createdAt, updatedAt;
    private String lunchBeginHour, lunchEndHour, name, serviceBeginHour, serviceEndHour;
    private CustomLocks customLocks;
    private ServiceDays serviceDays;
    private User user;

}
