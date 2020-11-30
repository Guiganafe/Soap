package br.com.justworks.prestador.ServicoAki.Model;

import com.google.firebase.Timestamp;

import org.joda.time.DateTime;

public class ScheduleItems {

    private String scheduleId, title;
    private Boolean active;
    private Timestamp dateService;
    private Timestamp hourBegin, hourEnd;
    private String price;
    private Address address;
    private User user;

    public ScheduleItems() {
    }

    public ScheduleItems(String scheduleId, Boolean active, Timestamp dateService, Timestamp hourBegin, Timestamp hourEnd, String title, String price, Address address, User user) {
        this.scheduleId = scheduleId;
        this.active = active;
        this.dateService = dateService;
        this.hourBegin = hourBegin;
        this.hourEnd = hourEnd;
        this.title = title;
        this.price = price;
        this.address = address;
        this.user = user;
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

    public Timestamp getDateService() {
        return dateService;
    }

    public void setDateService(Timestamp dateService) {
        this.dateService = dateService;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
