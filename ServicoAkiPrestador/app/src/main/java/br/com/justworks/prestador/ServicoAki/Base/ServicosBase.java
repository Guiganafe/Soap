package br.com.justworks.prestador.ServicoAki.Base;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;

public class ServicosBase {

    // Instância da classe singleton
    private static ServicosBase mServicosBase;

    /*
        Estrutura de dados utilizada
     */
    private ArrayList<ServiceUser> servicos;
    private ArrayList<ServiceUser> servicos_selecionados;
    private ArrayList<ServiceUser> servicos_restantes;

    private ServicosBase(){
        servicos = UserBase.getInstance().getServicesUserList();
        servicos_restantes = new ArrayList<>();
        servicos_restantes.addAll(servicos);
        servicos_selecionados = new ArrayList<>();
    }

    /*
       Retorna a instância da classe
    */
    public static ServicosBase getInstance(){
        if(mServicosBase == null){
            mServicosBase = new ServicosBase();
        }
        return mServicosBase;
    }

    /*
        Retorna os serviços selecionados
     */
    public ArrayList<ServiceUser> getServicos_selecionados(){
        return this.servicos_selecionados;
    }

    /*
        Retorna os serviços restantes
     */
    public ArrayList<ServiceUser> getServicos_restantes(){
        return this.servicos_restantes;
    }

    /*
        Adiciona um serviço na lista de selecionados
        e remove o mesmo da lista de restantes
     */
    public void addServico_selecionado(ServiceUser servicoSelecionado){
        this.servicos_selecionados.add(servicoSelecionado);
        for(int i = 0; i < servicos_restantes.size(); i++){
            if(servicos_restantes.get(i).getId().equals(servicoSelecionado.getId())){
                this.servicos_restantes.remove(servicos_restantes.get(i));
            }
        }
    }

    /*
        Remove um serviço na lista de selecionados
        e adiciona o mesmo da lista de restantes
     */
    public void removeServico_selecionado(ServiceUser servicoRemovido){
        this.servicos_selecionados.remove(servicoRemovido);
        this.servicos_restantes.add(servicoRemovido);
    }

    /*
        Limpa os serviços
     */
    public void clear() {
        mServicosBase = null;
        ServicosBase.getInstance();
    }

    /*
        Limpa os serviços selecionados
     */
    public void clearServicos_selecionados() {
        this.servicos_selecionados = new ArrayList<>();
    }
}
