package br.com.justworks.prestador.ServicoAki.Base;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;

public class ServicosBase {

    private static ServicosBase mServicosBase;

    private ArrayList<ServiceUser> servicos;
    private ArrayList<ServiceUser> servicos_selecionados;
    private ArrayList<ServiceUser> servicos_restantes;

    private ServicosBase(){
        servicos = UserBase.getInstance().getServicesUserList();
        servicos_restantes = new ArrayList<>();
        servicos_selecionados = new ArrayList<>();
    }

    public static ServicosBase getInstance(){
        if(mServicosBase == null){
            mServicosBase = new ServicosBase();
        }
        return mServicosBase;
    }

    public ArrayList<ServiceUser> getServicos(){
        return this.servicos;
    }

    public ArrayList<ServiceUser> getServicos_selecionados(){
        return this.servicos_selecionados;
    }

    public void addServico_selecionado(ServiceUser servicoSelecionado){
        this.servicos_selecionados.add(servicoSelecionado);
        this.servicos_restantes = servicos;
        this.servicos_restantes.remove(servicoSelecionado);
    }

    public void removeServico_selecionado(ServiceUser servicoRemovido){
        this.servicos_selecionados.remove(servicoRemovido);
        this.servicos_restantes.add(servicoRemovido);
    }

    public ArrayList<ServiceUser> getServicos_restantes(){
        return this.servicos_restantes;
    }

    public void clear() {
        mServicosBase = null;
    }
}
