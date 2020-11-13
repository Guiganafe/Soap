package br.com.justworks.prestador.ServicoAki.Model;

public class ServiceType {
    private boolean budget;
    private boolean priceFixed;

    public ServiceType() {
    }

    public ServiceType(boolean budget, boolean priceFixed) {
        this.budget = budget;
        this.priceFixed = priceFixed;
    }

    public boolean isBudget() {
        return budget;
    }

    public void setBudget(boolean budget) {
        this.budget = budget;
    }

    public boolean isPriceFixed() {
        return priceFixed;
    }

    public void setPriceFixed(boolean priceFixed) {
        this.priceFixed = priceFixed;
    }
}
