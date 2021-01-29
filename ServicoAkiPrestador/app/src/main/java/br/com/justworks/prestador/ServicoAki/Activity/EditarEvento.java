package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.justworks.prestador.ServicoAki.Adapter.ServiceSelectedAdapter;
import br.com.justworks.prestador.ServicoAki.Base.AgendaBase;
import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ServiceEventListViewModel;

public class EditarEvento extends AppCompatActivity {

    private Spinner spinner;
    private Button btn_avancar, btn_cancelar;
    private Calendar calendar;
    private int dia,mes, ano, hora, minuto;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private EndereçoViewModel endereçoViewModel;
    private ServiceEventListViewModel serviceEventListViewModel;
    private EditText titulo_evento, inicio_evento_hora, inicio_evento_data, fim_evento_hora, fim_evento_data, tipo_evento, valor_evento, local_evento;
    private TextView tv_local, tv_local_op, tv_valor, tv_valor_op, tv_servicos, tv_servicos_op, tv_add_servico;
    private LinearLayout lista_servicos_vazia;
    public boolean atendimento = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<ServiceUser> servicesUser = new ArrayList<>();
    private ArrayList<ServiceUser> servicesEvent = new ArrayList<>();

    private int horaInicio, minutoInicio, diaInicio, mesInicio, anoInicio, horaFim, minutoFim, diaFim, mesFim, anoFim;
    private ScheduleItems scheduleItem;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        inicializarComponentes();
        carregarDadosController();
    }

    private void carregarDadosController() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        scheduleItem = AgendaBase.getInstance().getScheduleItemsList().get(position);

        titulo_evento.setText(scheduleItem.getTitle());

        inicio_evento_hora
                .setText(returnTime(
                        (scheduleItem.getHourBegin().toDate().getHours()*60)
                        + (scheduleItem.getHourBegin().toDate().getMinutes()*60)
                ));

        inicio_evento_data.setText(
                scheduleItem.getHourBegin().toDate().getDay() +
                        "/" + scheduleItem.getHourBegin().toDate().getMonth() +
                        "/" + scheduleItem.getHourBegin().toDate().getYear()
        );

        fim_evento_hora
                .setText(returnTime(
                        (scheduleItem.getHourEnd().toDate().getHours() * 60)
                        + (scheduleItem.getHourEnd().toDate().getMinutes() * 60)
                ));

        fim_evento_data.setText(
                scheduleItem.getHourEnd().toDate().getDay() +
                        "/" + scheduleItem.getHourEnd().toDate().getMonth() +
                        "/" + scheduleItem.getHourEnd().toDate().getYear()
        );

        if(scheduleItem.getPrice() != null){
            //spinner..setText("Atendimento interno");
            atendimento = true;
        } else {
            tipo_evento.setText("Horário bloqueado");
            atendimento = false;
        }

        if(atendimento){
            valor_evento.setText(scheduleItem.getPrice().toString());
            local_evento.setText(
                    scheduleItem.getAddress().getStreet()
            );
        }



    }

    private String returnTime(int time){
        long h = Math.round(time / 60);
        long m = Math.round(time - (h * 60));

        if(h < 9){
            if(m < 9){
                return ("0" + h + ":0" + m);
            } else {
                return ("0" + h + ":" + m);
            }
        } else {
            if(m < 9){
                return (h + ":0" + m);
            } else {
                return (h + ":" + m);
            }
        }
    }

    private void inicializarComponentes() {
        calendar = Calendar.getInstance();
        recyclerView = findViewById(R.id.reciclerView_servicoEvento_edicao);
        spinner = (Spinner) findViewById(R.id.spinner_tipo_evento_edicao);
        btn_avancar = (Button) findViewById(R.id.btn_concluir_criar_evento_edicao);
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar_criar_evento_edicao);
        local_evento = (EditText) findViewById(R.id.local_evento_edicao);
        valor_evento = (EditText) findViewById(R.id.valor_evento_edicao);
        titulo_evento = (EditText) findViewById(R.id.titulo_evento_edicao);
        inicio_evento_hora = (EditText) findViewById(R.id.inicio_evento_hora_edicao);
        inicio_evento_data = (EditText) findViewById(R.id.inicio_evento_data_edicao);
        fim_evento_data = (EditText) findViewById(R.id.fim_evento_data_edicao);
        fim_evento_hora = (EditText) findViewById(R.id.fim_evento_hora_edicao);
        tv_local = (TextView) findViewById(R.id.tv_local_edicao);
        tv_local_op = (TextView) findViewById(R.id.tv_local_op_edicao);
        tv_valor = (TextView) findViewById(R.id.tv_valor_edicao);
        tv_valor_op = (TextView) findViewById(R.id.tv_valor_op_edicao);
        tv_servicos = (TextView) findViewById(R.id.tv_servicos_edicao);
        tv_servicos_op = (TextView) findViewById(R.id.tv_servicos_op_edicao);
        tv_add_servico = (TextView) findViewById(R.id.tv_add_setvico_edicao);
        lista_servicos_vazia = (LinearLayout) findViewById(R.id.lista_servicos_vazia_edicao);
    }
}