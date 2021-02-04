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

    /*
        Acesso aos dados do firebase
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    /*
        Estrutura de dados utilizada
     */
    private ArrayList<ScheduleItems> scheduleItemsList;
    private ArrayList<ScheduleItems> scheduleItemsListByDay;
    private ArrayList<String> scheduleItemsId = new ArrayList<>();
    private ArrayList<String> scheduleItemsIdByDay = new ArrayList<>();

    // Instância da classe singleton
    private static AgendaBase mAgendaBase;

    private AgendaBase (){
        firebaseListenner();
    }

    /*
        Escuta por atualizações na base de dados
     */
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

    /*
        Retorna a instância da classe
     */
    public static AgendaBase getInstance(){
        if(mAgendaBase == null){
            mAgendaBase = new AgendaBase();
        }
        return mAgendaBase;
    }

    /*
        Retorna os eventos da agenda
     */
    public ArrayList<ScheduleItems> getScheduleItemsList(){
        return scheduleItemsList;
    }

    /*
        Retorna os eventos da agenda por dia
     */
    public ArrayList<ScheduleItems> getScheduleItemsListByDay(){
        return scheduleItemsListByDay;
    }

    /*
        Define os eventos por dia da agenda
     */
    public void setScheduleItemsListByDay(ArrayList<ScheduleItems> itemsByDay){
        this.scheduleItemsListByDay = itemsByDay;
    }

    /*
        Reseta os eventos por dia da agenda
     */
    public void deleteScheduleItemsByDay(){
        this.scheduleItemsListByDay = null;
    }

    /*
        Retorna os IDs dos eventos da agenda
     */
    public ArrayList<String> getScheduleItemsId(){
        return this.scheduleItemsId;
    }

    /*
        Retorna os IDs dos eventos por dia da agenda
     */
    public ArrayList<String> getScheduleItemsIdByDay(){
        return this.scheduleItemsIdByDay;
    }

    /*
        Retorna os IDs dos eventos da agenda
     */
    public void setScheduleItemsIdByDay(ArrayList<String> scheduleItemsIdByDayNew){
        this.scheduleItemsIdByDay = scheduleItemsIdByDayNew;
    }

    /*
        Remove um evento do dia de determinada posição
     */
    public void removeScheduleItemsListByDay(int position) {
        this.scheduleItemsListByDay.remove(position);
        this.scheduleItemsIdByDay.remove(position);
    }

    /*
        Limpa a agenda
     */
    public void limparAgenda() {
        mAgendaBase = null;
    }
}
