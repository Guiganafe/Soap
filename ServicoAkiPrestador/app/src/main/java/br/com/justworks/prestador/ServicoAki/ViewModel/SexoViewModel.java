package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SexoViewModel extends ViewModel {

    private MutableLiveData<String> sexoPtbR = new MutableLiveData<>();


    public LiveData<String> getSexoPtbr() {
        return sexoPtbR;
    }

    public void setSexoPtbR(String sexoPtbR) {
        this.sexoPtbR.setValue(sexoPtbR);
    }


}
