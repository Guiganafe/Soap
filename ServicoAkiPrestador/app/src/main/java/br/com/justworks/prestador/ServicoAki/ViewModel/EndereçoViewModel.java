package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EndereçoViewModel extends ViewModel {
    private MutableLiveData<String> rua = new MutableLiveData<>();
    private MutableLiveData<String> numero = new MutableLiveData<>();
    private MutableLiveData<String> complemento = new MutableLiveData<>();
    private MutableLiveData<String> bairro = new MutableLiveData<>();
    private MutableLiveData<String> cep = new MutableLiveData<>();
    private MutableLiveData<String> cidade = new MutableLiveData<>();
    private MutableLiveData<String> estado = new MutableLiveData<>();
    private MutableLiveData<Boolean> active = new MutableLiveData<>();
    private MutableLiveData<String> pais = new MutableLiveData<>();
    private MutableLiveData<Double> latitude = new MutableLiveData<>();
    private MutableLiveData<Double> longitude = new MutableLiveData<>();
    private MutableLiveData<String> regionId = new MutableLiveData<>();

    public LiveData<String> getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua.setValue(rua);
    }

    public LiveData<String> getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero.setValue(numero);
    }

    public LiveData<String> getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento.setValue(complemento);
    }

    public LiveData<String> getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro.setValue(bairro);
    }

    public LiveData<String> getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep.setValue(cep);
    }

    public LiveData<String> getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade.setValue(cidade);
    }

    public LiveData<String> getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.setValue(estado);
    }

    public LiveData<Boolean> getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active.setValue(active);
    }

    public LiveData<String> getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais.setValue(pais);
    }

    public LiveData<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude.setValue(latitude);
    }

    public LiveData<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude.setValue(longitude);
    }

    public LiveData<String> getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId.setValue(regionId);
    }
}
