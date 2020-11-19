package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.Services;

public class ServicoViewModel extends ViewModel {
    private MutableLiveData<String> categoriaId = new MutableLiveData<>();
    private MutableLiveData<String> serviceId = new MutableLiveData<>();
    private MutableLiveData<CategoriesServices> categoriesServices = new MutableLiveData<>();
    private MutableLiveData<Services> services = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ServiceUser>> services_list = new MutableLiveData<>();
    private MutableLiveData<Double> valor_servico = new MutableLiveData<>();
    private MutableLiveData<String> duracao_servico = new MutableLiveData<>();
    private MutableLiveData<Boolean> fornece_material = new MutableLiveData<>();
    private MutableLiveData<Boolean> desloca_cliente = new MutableLiveData<>();
    private MutableLiveData<Double> desloca_cliente_valor = new MutableLiveData<>();
    private MutableLiveData<String> descricao = new MutableLiveData<>();

    public LiveData<String> getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId.setValue(categoriaId);
    }

    public LiveData<String> getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId.setValue(serviceId);
    }

    public LiveData<CategoriesServices> getCategoriesServices() {
        return categoriesServices;
    }

    public void setCategoriesServices(CategoriesServices categoriesServices) {
        this.categoriesServices.setValue(categoriesServices);
    }

    public LiveData<Services> getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services.setValue(services);
    }

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

    public LiveData<Double> getValor_servico() {
        return valor_servico;
    }

    public void setValor_servico(Double valor_servico) {
        this.valor_servico.setValue(valor_servico);
    }

    public LiveData<String> getDuracao_servico() {
        return duracao_servico;
    }

    public void setDuracao_servico(String duracao_servico) {
        this.duracao_servico.setValue(duracao_servico);
    }

    public LiveData<Boolean> getFornece_material() {
        return fornece_material;
    }

    public void setFornece_material(Boolean fornece_material) {
        this.fornece_material.setValue(fornece_material);
    }

    public LiveData<Boolean> getDesloca_cliente() {
        return desloca_cliente;
    }

    public void setDesloca_cliente(Boolean desloca_cliente) {
        this.desloca_cliente.setValue(desloca_cliente);
    }

    public LiveData<Double> getDesloca_cliente_valor() {
        return desloca_cliente_valor;
    }

    public void setDesloca_cliente_valor(Double desloca_cliente_valor) {
        this.desloca_cliente_valor.setValue(desloca_cliente_valor);
    }

    public LiveData<String> getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.setValue(descricao);
    }
}
