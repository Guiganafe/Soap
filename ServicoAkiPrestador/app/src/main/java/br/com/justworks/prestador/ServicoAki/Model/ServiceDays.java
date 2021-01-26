package br.com.justworks.prestador.ServicoAki.Model;

public class ServiceDays {
    public String day;
    public int startTime;
    public int endTime;
    public int lunchStartTime;
    public int lunchEndTime;

    public ServiceDays() {
    }

    public ServiceDays(String day, int startTime, int endTime, int lunchStartTime, int lunchEndTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lunchStartTime = lunchStartTime;
        this.lunchEndTime = lunchEndTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(int lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public int getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(int lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }
}
