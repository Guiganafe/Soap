package br.com.justworks.prestador.ServicoAki;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfissionalViewModel extends ViewModel {

    private MutableLiveData<Bitmap> foto_perfil = new MutableLiveData<>();
    private MutableLiveData<String> nome_completo = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> telefone = new MutableLiveData<>();
    private MutableLiveData<String> senha = new MutableLiveData<>();
}
