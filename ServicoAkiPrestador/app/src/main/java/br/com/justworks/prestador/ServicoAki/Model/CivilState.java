package br.com.justworks.prestador.ServicoAki.Model;

public class CivilState {
    private String ptbr;
    private String en;

    public CivilState() {
    }

    public CivilState(String ptbr, String en) {
        this.ptbr = ptbr;
        this.en = en;
    }

    public String getPtbr() {
        return ptbr;
    }

    public void setPtbr(String ptbr) {
        this.ptbr = ptbr;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
