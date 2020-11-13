package br.com.justworks.prestador.ServicoAki.Model;

public class ServiceLocation {
    private boolean home;
    private boolean remote;
    private boolean work;

    public ServiceLocation() {
    }

    public ServiceLocation(boolean home, boolean remote, boolean work) {
        this.home = home;
        this.remote = remote;
        this.work = work;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }
}
