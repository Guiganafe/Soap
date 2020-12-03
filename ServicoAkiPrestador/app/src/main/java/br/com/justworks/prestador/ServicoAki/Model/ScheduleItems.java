package br.com.justworks.prestador.ServicoAki.Model;

import com.google.firebase.Timestamp;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class ScheduleItems {

    private String scheduleId, title;
    private Boolean active;
    private Timestamp hourBegin, hourEnd;
    private String price;
    private Address address;
    private String professionalId;
    private ArrayList<ServiceUser> services;

    public ScheduleItems() {
    }

    public ScheduleItems(String scheduleId, Boolean active, Timestamp hourBegin, Timestamp hourEnd, String title, String price, Address address, String professionalId, ArrayList<ServiceUser> services) {
        this.scheduleId = scheduleId;
        this.active = active;
        this.hourBegin = hourBegin;
        this.hourEnd = hourEnd;
        this.title = title;
        this.price = price;
        this.address = address;
        this.professionalId = professionalId;
        this.services = services;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Timestamp getHourBegin() {
        return hourBegin;
    }

    public void setHourBegin(Timestamp hourBegin) {
        this.hourBegin = hourBegin;
    }

    public Timestamp getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(Timestamp hourEnd) {
        this.hourEnd = hourEnd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public ArrayList<ServiceUser> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceUser> services) {
        this.services = services;
    }
}
