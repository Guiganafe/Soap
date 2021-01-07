package br.com.justworks.prestador.ServicoAki.Base;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;

public class EnderecoBase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private ArrayList<Address> addressArrayList;

    private static EnderecoBase mEnderecoBase;

    private EnderecoBase (){
        firebaseListenner();
    }

    private void firebaseListenner() {
        db.collection("addresses").whereEqualTo("userId", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    addressArrayList = null;
                    addressArrayList = new ArrayList<>();
                    for (DocumentSnapshot document: value.getDocuments()) {
                        addressArrayList.add(document.toObject(Address.class));
                    }
                }
            }
        });
    }

    public static EnderecoBase getInstance(){
        if(mEnderecoBase == null){
            mEnderecoBase = new EnderecoBase();
        }
        return mEnderecoBase;
    }

    public ArrayList<Address> getAddressItemsList(){
        return addressArrayList;
    }

    public void limparEndereco() {
        mEnderecoBase = null;
    }
}
