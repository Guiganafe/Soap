package br.com.justworks.prestador.ServicoAki.Model;

import java.sql.Timestamp;

public class ScheduleItems {

    private String scheduleId;
    private Boolean active;
    private Timestamp dateService;
    private String hourBegin, hourEnd;
    private Professional professional;
    private Services service;
    private User user;

}
