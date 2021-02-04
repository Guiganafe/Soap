package br.com.justworks.prestador.ServicoAki.Base;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.Model.User;

public class UserBase {

    /*
        Acesso aos dados do firebase
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private User user;

    // Instância da classe singleton
    private static UserBase mUserBase;

    private UserBase(){
        user = new User();
        db.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user = documentSnapshot.toObject(User.class);
                }
            }
        });

        firebaseListenner();
    }

    /*
       Escuta por atualizações na base de dados
    */
    private void firebaseListenner() {
        db.collection("users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                if (value != null && value.exists()) {
                    user = value.toObject(User.class);
                }
            }
        });
    }

    /*
       Retorna a instância da classe
    */
    public static UserBase getInstance(){
        if(mUserBase == null){
            mUserBase = new UserBase();
        }
        return mUserBase;
    }

    /*
        Retorna o usuário
     */
    public User getUser(){
        return user;
    }

    /*
        Retorna a lista de serviços do usuário
     */
    public ArrayList<ServiceUser> getServicesUserList(){
        return user.getServices();
    }

    /*
        Retorna um serviço do usuário, em determonada posção
     */
    public ServiceUser getServiceUser(int position){
        return user.getServices().get(position);
    }

    /*
        Adiciona um serviço ao usuário
     */
    public void addServiceUser(ServiceUser serviceUser){
        user.getServices().add(serviceUser);
    }

    /*
        Remove um serviço do usuário
     */
    public void removeService(int position){
        user.getServices().remove(position);
    }

    /*
        Limpa o usuário
     */
    public void limparUser(){
        mUserBase = null;
    }
}
