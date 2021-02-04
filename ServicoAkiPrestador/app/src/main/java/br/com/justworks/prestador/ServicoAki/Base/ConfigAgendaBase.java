package br.com.justworks.prestador.ServicoAki.Base;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CustomLocks;
import br.com.justworks.prestador.ServicoAki.Model.Schedules;
import br.com.justworks.prestador.ServicoAki.Model.ServiceDays;

public class ConfigAgendaBase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String professionalId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private boolean seg, ter, qua, qui, sex, sab, dom;

    private Schedules schedule;

    private String scheduleId = "";

    private static ConfigAgendaBase configAgendaBase;

    private ConfigAgendaBase (){
        firebaseListenner();
    }

    private void firebaseListenner() {
        db.collection("schedules").whereEqualTo("professionalId", professionalId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    if(value.getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = value.getDocuments().get(0);
                        schedule = documentSnapshot.toObject(Schedules.class);
                        scheduleId = documentSnapshot.getId();
                    }
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

    public String getScheduleId(){
        return scheduleId;
    }

    public Schedules getSchedule(){
        return schedule;
    }

    public void setServiceDays(ArrayList<ServiceDays> sd){
        this.schedule.setServiceDays(sd);
    }

    public void setDom(boolean domSet){
        this.dom = domSet;
    }

    public void setSeg(boolean segSet){
        this.seg = segSet;
    }

    public void setTer(boolean terSet){
        this.ter = terSet;
    }

    public void setQua(boolean quaSet){
        this.qua = quaSet;
    }

    public void setQui(boolean quiSet){
        this.qui = quiSet;
    }

    public void setSex(boolean sexSet){
        this.sex = sexSet;
    }

    public void setSab(boolean sabSet){
        this.sab = sabSet;
    }
}
