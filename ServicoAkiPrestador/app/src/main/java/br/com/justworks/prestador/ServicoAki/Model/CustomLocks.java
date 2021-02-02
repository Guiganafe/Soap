package br.com.justworks.prestador.ServicoAki.Model;

import com.google.firebase.Timestamp;

public class CustomLocks {
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;

    public CustomLocks() {
    }

    public CustomLocks(String title, Timestamp startDate, Timestamp endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
