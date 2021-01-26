package br.com.justworks.prestador.ServicoAki.Model;

import java.util.ArrayList;

public class Schedules {
    private Boolean active;
    private String professionalId;
    private ArrayList<ServiceDays> serviceDays;

    public Schedules() {
    }

    public Schedules(Boolean active, String professionalId, ArrayList<ServiceDays> serviceDays) {
        this.active = active;
        this.professionalId = professionalId;
        this.serviceDays = serviceDays;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public ArrayList<ServiceDays> getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(ArrayList<ServiceDays> serviceDays) {
        this.serviceDays = serviceDays;
    }
}
