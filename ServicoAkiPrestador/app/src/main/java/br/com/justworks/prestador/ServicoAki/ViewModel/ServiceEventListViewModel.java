package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;

public class ServiceEventListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ServiceUser>> services_list = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ServiceUser>> services_selected_list = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ServiceUser>> services_remaining_list = new MutableLiveData<>();

    public LiveData<ArrayList<ServiceUser>> getServices_list() {
        return services_list;
    }

    public void setServices_list(ArrayList<ServiceUser> services_list) {
        this.services_list.setValue(services_list);
    }

    public LiveData<ArrayList<ServiceUser>> getServices_selected_list() {
        return services_selected_list;
    }

    public void setServices_selected_list(ArrayList<ServiceUser> services_selected_list) {
        this.services_selected_list.setValue(services_selected_list);
    }

    public LiveData<ArrayList<ServiceUser>> getServices_remaining_list() {
        return services_remaining_list;
    }

    public void setServices_remaining_list(ArrayList<ServiceUser> services_remaining_list) {
        this.services_remaining_list.setValue(services_remaining_list);
    }
}
