package br.com.justworks.prestador.ServicoAki.Base;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
        scheduleItemsList = new ArrayList<>();

        db.collection("scheduleItems").whereEqualTo("professionalId", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
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
}
