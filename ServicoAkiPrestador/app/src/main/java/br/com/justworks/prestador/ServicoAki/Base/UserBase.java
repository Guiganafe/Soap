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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private User user;

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

        db.collection("users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                if (value != null && value.exists()) {
                    user = value.toObject(User.class);
                }
            }
        });
    }

    public static UserBase getInstance(){
        if(mUserBase == null){
            mUserBase = new UserBase();
        }
        return mUserBase;
    }

    public User getUser(){
        return user;
    }

    public ArrayList<ServiceUser> getServicesUserList(){
        return user.getServices();
    }

    public ServiceUser getServiceUser(int position){
        return user.getServices().get(position);
    }

    public void addServiceUser(ServiceUser serviceUser){
        user.getServices().add(serviceUser);
    }

    public void removeService(int position){
        user.getServices().remove(position);
    }
}
