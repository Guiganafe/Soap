package br.com.justworks.prestador.ServicoAki.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;

public class ServicoViewModel extends ViewModel {
    private MutableLiveData<String> categoriaId = new MutableLiveData<>();
    private MutableLiveData<CategoriesServices> categoriesServices = new MutableLiveData<>();

    public LiveData<String> getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId.setValue(categoriaId);
    }

    public LiveData<CategoriesServices> getCategoriesServices() {
        return categoriesServices;
    }

    public void setCategoriesServices(CategoriesServices categoriesServices) {
        this.categoriesServices.setValue(categoriesServices);
    }
}
