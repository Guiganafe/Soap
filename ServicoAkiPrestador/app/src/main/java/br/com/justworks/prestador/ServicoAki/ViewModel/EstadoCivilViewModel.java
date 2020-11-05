package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EstadoCivilViewModel extends ViewModel {


    private MutableLiveData<String> estadoCivilPtBr = new MutableLiveData<>();

    private MutableLiveData<String> estadoCivilEn = new MutableLiveData<>();

    public LiveData<String> getEstadoCivilPtBr() {
        return estadoCivilPtBr;
    }

    public void setEstadoCivilPtBr(String estadoCivilPtBr) {
        this.estadoCivilPtBr.setValue(estadoCivilPtBr);
    }

    public LiveData<String> getEstadoCivilEn() {
        return estadoCivilEn;
    }

    public void setEstadoCivilEn(String estadoCivilEn) {
        this.estadoCivilEn.setValue(estadoCivilEn);
    }

}
