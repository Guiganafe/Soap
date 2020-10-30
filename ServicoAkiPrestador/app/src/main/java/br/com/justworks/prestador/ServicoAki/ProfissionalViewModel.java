package br.com.justworks.prestador.ServicoAki;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfissionalViewModel extends ViewModel {

    private MutableLiveData<Bitmap> foto_perfil = new MutableLiveData<>();
    private MutableLiveData<String> nome_completo = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> telefone = new MutableLiveData<>();
    private MutableLiveData<String> senha = new MutableLiveData<>();
    private MutableLiveData<String> confirmarSenha = new MutableLiveData<>();
    private MutableLiveData<String> sexo = new MutableLiveData<>();
    private MutableLiveData<String> estadoCivil = new MutableLiveData<>();
    private MutableLiveData<String> rua = new MutableLiveData<>();
    private MutableLiveData<String> numero = new MutableLiveData<>();
    private MutableLiveData<String> complemento = new MutableLiveData<>();
    private MutableLiveData<String> bairro = new MutableLiveData<>();
    private MutableLiveData<String> cep = new MutableLiveData<>();
    private MutableLiveData<String> cidade = new MutableLiveData<>();
    private MutableLiveData<String> estado = new MutableLiveData<>();
    private MutableLiveData<String> cpf = new MutableLiveData<>();
    private MutableLiveData<String> nome_mae = new MutableLiveData<>();
    private MutableLiveData<String> doc_tipo = new MutableLiveData<>();
    private MutableLiveData<Bitmap> foto_selfie_doc = new MutableLiveData<>();
    private MutableLiveData<Bitmap> foto_doc_frente = new MutableLiveData<>();
    private MutableLiveData<Bitmap> foto_doc_verso = new MutableLiveData<>();
    private MutableLiveData<Bitmap> foto_comprovante_res = new MutableLiveData<>();

    public LiveData<Bitmap> getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(Bitmap foto_perfil) {
        this.foto_perfil.setValue(foto_perfil);
    }

    public LiveData<String> getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo.setValue(nome_completo);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public LiveData<String> getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone.setValue(telefone);
    }

    public LiveData<String> getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha.setValue(senha);
    }

    public LiveData<String> getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha.setValue(confirmarSenha);
    }

    public LiveData<String> getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo.setValue(sexo);
    }

    public LiveData<String> getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil.setValue(estadoCivil);
    }

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

    public LiveData<String> getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf.setValue(cpf);
    }

    public LiveData<String> getNome_mae() {
        return nome_mae;
    }

    public void setNome_mae(String nome_mae) {
        this.nome_mae.setValue(nome_mae);
    }

    public LiveData<String> getDoc_tipo() {
        return doc_tipo;
    }

    public void setDoc_tipo(String doc_tipo) {
        this.doc_tipo.setValue(doc_tipo);
    }

    public LiveData<Bitmap> getFoto_selfie_doc() {
        return foto_selfie_doc;
    }

    public void setFoto_selfie_doc(Bitmap foto_selfie_doc) {
        this.foto_selfie_doc.setValue(foto_selfie_doc);
    }

    public LiveData<Bitmap> getFoto_doc_frente() {
        return foto_doc_frente;
    }

    public void setFoto_doc_frente(Bitmap foto_doc_frente) {
        this.foto_doc_frente.setValue(foto_doc_frente);
    }

    public LiveData<Bitmap> getFoto_doc_verso() {
        return foto_doc_verso;
    }

    public void setFoto_doc_verso(Bitmap foto_doc_verso) {
        this.foto_doc_verso.setValue(foto_doc_verso);
    }

    public LiveData<Bitmap> getFoto_comprovante_res() {
        return foto_comprovante_res;
    }

    public void setFoto_comprovante_res(Bitmap foto_comprovante_res) {
        this.foto_comprovante_res.setValue(foto_comprovante_res);
    }
}
