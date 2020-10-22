package br.com.justworks.prestador.ServicoAki.Model;

import java.sql.Timestamp;

public class ScheduleItems {

    private String scheduleId;
    private Boolean active;
    private Timestamp dateService;
    private String hourBegin, hourEnd, title;
    private String price;
    private Address address;
    private User user;

    public ScheduleItems() {
    }

    public ScheduleItems(String scheduleId, Boolean active, Timestamp dateService, String hourBegin, String hourEnd, String title, String price, Address address, User user) {
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

    public String getHourBegin() {
        return hourBegin;
    }

    public void setHourBegin(String hourBegin) {
        this.hourBegin = hourBegin;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
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
