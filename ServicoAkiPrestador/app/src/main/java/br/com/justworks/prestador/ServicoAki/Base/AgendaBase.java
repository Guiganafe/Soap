package br.com.justworks.prestador.ServicoAki.Base;

import androidx.annotation.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;

public class AgendaBase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private ArrayList<ScheduleItems> scheduleItemsList;

    private static AgendaBase mAgendaBase;

    private AgendaBase (){
        firebaseListenner();
    }

    private void firebaseListenner() {
        db.collection("scheduleItems").whereEqualTo("professionalId", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    scheduleItemsList = null;
                    scheduleItemsList = new ArrayList<>();
                    for (DocumentSnapshot document: value.getDocuments()) {
                        scheduleItemsList.add(document.toObject(ScheduleItems.class));
                    }
                }
            }
        });
    }

    public static AgendaBase getInstance(){
        if(mAgendaBase == null){
            mAgendaBase = new AgendaBase();
        }
        return mAgendaBase;
    }

    public ArrayList<ScheduleItems> getScheduleItemsList(){
        return scheduleItemsList;
    }

    public void limparAgenda() {
        mAgendaBase = null;
    }
}
