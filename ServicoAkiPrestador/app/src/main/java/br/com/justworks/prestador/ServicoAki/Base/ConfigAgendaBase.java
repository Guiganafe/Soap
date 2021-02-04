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

    /*
        Acesso aos dados do firebase
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String professionalId = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    // Instância da agenda
    private Schedules schedule;

    // Id da agenda
    private String scheduleId = "";

    // Instância da classe singleton
    private static ConfigAgendaBase configAgendaBase;

    private ConfigAgendaBase (){
        firebaseListenner();
    }

    /*
        Escuta por atualizações na base de dados
     */
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

    /*
       Retorna a instância da classe
    */
    public static ConfigAgendaBase getInstance(){
        if(configAgendaBase == null){
            configAgendaBase = new ConfigAgendaBase();
        }
        return configAgendaBase;
    }

    /*
        Retorna o id da agenda
     */
    public String getScheduleId(){
        return scheduleId;
    }

    /*
        Retorna a agenda
     */
    public Schedules getSchedule(){
        return schedule;
    }

    /*
        Define os dias úteis de trabalho
     */
    public void setServiceDays(ArrayList<ServiceDays> sd){
        this.schedule.setServiceDays(sd);
    }

    /*
        Limpa a agenda base
     */
    public void limparConfigAgendaBase() {
        configAgendaBase = null;
    }
}
