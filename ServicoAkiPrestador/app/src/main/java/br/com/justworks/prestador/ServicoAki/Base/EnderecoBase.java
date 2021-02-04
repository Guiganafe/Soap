package br.com.justworks.prestador.ServicoAki.Base;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Activity.EditarEndereco;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;

public class EnderecoBase {

    /*
        Acesso aos dados do firebase
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    /*
        Estrutura de dados utilizada
     */
    private ArrayList<Address> addressArrayList;
    private ArrayList<String> addressIds;

    // Instância da classe singleton
    private static EnderecoBase mEnderecoBase;

    private EnderecoBase (){
        firebaseListenner();
    }

    /*
        Escuta por atualizações na base de dados
     */
    private void firebaseListenner() {
        db.collection("addresses").whereEqualTo("userId", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    addressArrayList = null;
                    addressIds = null;
                    addressArrayList = new ArrayList<>();
                    addressIds = new ArrayList<>();
                    for (DocumentSnapshot document: value.getDocuments()) {
                        //addressArrayList.add(document.toObject(Address.class));
                        addressIds.add(document.getId());

                        Address address = new Address(
                                document.getBoolean("active"),
                                document.getString("addressName"),
                                document.getString("addressType"),
                                document.getString("city"),
                                document.getString("country"),
                                document.getString("neighborhood"),
                                document.getString("number"),
                                document.getString("state"),
                                document.getString("street"),
                                document.getString("userId"),
                                document.getString("zipCode"),
                                document.getDouble("latitude"),
                                document.getDouble("longitude"));

                        addressArrayList.add(address);
                    }
                }
            }
        });
    }

    /*
       Retorna a instância da classe
    */
    public static EnderecoBase getInstance(){
        if(mEnderecoBase == null){
            mEnderecoBase = new EnderecoBase();
        }
        return mEnderecoBase;
    }

    /*
        Retorna a lista de endereços
     */
    public ArrayList<Address> getAddressItemsList(){
        return addressArrayList;
    }

    /*
        Remove um endereço em determinada posição
     */
    public void removerEndereco(int position){
        this.addressArrayList.remove(position);
        this.addressIds.remove(position);
    }

    /*
        Adiciona um endereço
     */
    public void adicionarEndereco(Address address){
        this.addressArrayList.add(address);
    }

    /*
        Retorna um endereço em determinada posição
     */
    public String getEnderecoId(int position){
        return this.addressIds.get(position);
    }

    /*
        Limpa o endereço
     */
    public void limparEndereco() {
        mEnderecoBase = null;
    }
}
