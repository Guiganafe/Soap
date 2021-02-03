package br.com.justworks.prestador.ServicoAki.Base;

import androidx.annotation.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;

public class AgendaBase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private ArrayList<ScheduleItems> scheduleItemsList;
    private ArrayList<ScheduleItems> scheduleItemsListByDay;
    private ArrayList<String> scheduleItemsId = new ArrayList<>();
    private ArrayList<String> scheduleItemsIdByDay = new ArrayList<>();


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
                        scheduleItemsId.add(document.getId());
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

    public ArrayList<ScheduleItems> getScheduleItemsListByDay(){
        return scheduleItemsListByDay;
    }

    public void setScheduleItemsListByDay(ArrayList<ScheduleItems> itemsByDay){
        this.scheduleItemsListByDay = itemsByDay;
    }

    public void deleteScheduleItemsByDay(){
        this.scheduleItemsListByDay = null;
    }

    public ArrayList<String> getScheduleItemsId(){
        return this.scheduleItemsId;
    }

    public ArrayList<String> getScheduleItemsIdByDay(){
        return this.scheduleItemsIdByDay;
    }

    public void setScheduleItemsIdByDay(ArrayList<String> scheduleItemsIdByDayNew){
        this.scheduleItemsIdByDay = scheduleItemsIdByDayNew;
    }

    public void limparAgenda() {
        mAgendaBase = null;
    }

    public void removeScheduleItemByDay(int position) {
        this.scheduleItemsListByDay.remove(position);
    }

    public void addScheduleItem(ScheduleItems item) {
        this.scheduleItemsListByDay.add(item);
    }

    public void updateScheduleItem(ScheduleItems item, int position){
        this.scheduleItemsListByDay.set(position, item);
    }

    public void removeScheduleItemsListByDay(int position) {
        this.scheduleItemsListByDay.remove(position);
        this.scheduleItemsIdByDay.remove(position);
    }
}
