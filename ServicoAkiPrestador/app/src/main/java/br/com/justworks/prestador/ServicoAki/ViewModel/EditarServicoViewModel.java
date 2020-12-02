package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;

public class EditarServicoViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ServiceUser>> services_list = new MutableLiveData<>();
    private MutableLiveData<Integer> posicao = new MutableLiveData<>();

    public LiveData<ArrayList<ServiceUser>> getServices_list() {
        return services_list;
    }

    public void setServices_list(ArrayList<ServiceUser> services_list) {
        this.services_list.setValue(services_list);
    }

    public void addService(ServiceUser service){
        if(services_list.getValue() != null){
            this.services_list.getValue().add(service);
        } else {
            ArrayList<ServiceUser> services_list = new ArrayList<>();
            services_list.add(service);
            this.services_list.setValue(services_list);
        }
    }

    public LiveData<Integer> getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao.setValue(posicao);
    }
}
