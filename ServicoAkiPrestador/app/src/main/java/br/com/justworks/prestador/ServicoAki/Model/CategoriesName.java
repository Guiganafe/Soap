package br.com.justworks.prestador.ServicoAki.Model;

public class CategoriesName {
    private String en;
    private String ptbr;

    public CategoriesName() {
    }

    public CategoriesName(String en, String ptbr) {
        this.en = en;
        this.ptbr = ptbr;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getPtbr() {
        return ptbr;
    }

    public void setPtbr(String ptbr) {
        this.ptbr = ptbr;
    }
}
