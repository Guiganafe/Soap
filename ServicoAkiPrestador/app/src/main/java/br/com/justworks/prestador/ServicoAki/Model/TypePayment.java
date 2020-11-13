package br.com.justworks.prestador.ServicoAki.Model;

public class TypePayment {
    private boolean app;
    private boolean presential;

    public TypePayment() {
    }

    public TypePayment(boolean app, boolean presential) {
        this.app = app;
        this.presential = presential;
    }

    public boolean isApp() {
        return app;
    }

    public void setApp(boolean app) {
        this.app = app;
    }

    public boolean isPresential() {
        return presential;
    }

    public void setPresential(boolean presential) {
        this.presential = presential;
    }
}
