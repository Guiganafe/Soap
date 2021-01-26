package br.com.justworks.prestador.ServicoAki.Base;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Schedules;

public class ConfigAgendaBase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String professionalId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private Schedules schedule;

    private String scheduleId;

    private static ConfigAgendaBase configAgendaBase;

    private ConfigAgendaBase (){
        firebaseListenner();
    }

    private void firebaseListenner() {
        db.collection("schedules").whereEqualTo("professionalId", professionalId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    DocumentSnapshot documentSnapshot = value.getDocuments().get(0);
                    schedule = documentSnapshot.toObject(Schedules.class);
                    scheduleId = documentSnapshot.getId();
                }
            }
        });
    }

    public static ConfigAgendaBase getInstance(){
        if(configAgendaBase == null){
            configAgendaBase = new ConfigAgendaBase();
        }
        return configAgendaBase;
    }

    public Schedules getSchedule(){
        return schedule;
    }
}
