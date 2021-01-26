package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Base.ConfigAgendaBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Schedules;
import br.com.justworks.prestador.ServicoAki.Model.ServiceDays;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class ConfigurarAgenda extends AppCompatActivity {

    public CardView dom, seg, ter, qua, qui, sex, sab, cardDom, cardSeg, cardTer, cardQua, cardQui, cardSex, cardSab;

    public EditText hora_inicio_dom, inicio_intervalo_dom, fim_intervalo_dom, hora_termino_dom,
            hora_inicio_seg, inicio_intervalo_seg, fim_intervalo_seg, hora_termino_seg,
            hora_inicio_ter, inicio_intervalo_ter, fim_intervalo_ter, hora_termino_ter,
            hora_inicio_qua, inicio_intervalo_qua, fim_intervalo_qua, hora_termino_qua,
            hora_inicio_qui, inicio_intervalo_qui, fim_intervalo_qui, hora_termino_qui,
            hora_inicio_sex, inicio_intervalo_sex, fim_intervalo_sex, hora_termino_sex,
            hora_inicio_sab, inicio_intervalo_sab, fim_intervalo_sab, hora_termino_sab;

    public boolean dom_selected = false, seg_selected = false, ter_selected = false,
            qua_selected = false, qui_selected = false, sex_selected = false, sab_selected = false,
            dom_valid = false, seg_valid = false, ter_valid = false, qua_valid = false,
    qui_valid = false, sex_valid = false, sab_valid = false;

    public Button btn_salvar, btn_voltar;

    ArrayList<ServiceDays> serviceDays = new ArrayList<>();

    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_agenda);
        inicializarComponentes();
        loadAgenda();
        onClickController();
        maskController();
    }

    private void loadAgenda() {
        final Drawable drawable = ContextCompat.getDrawable(this, R.drawable.background_day_schedule_selected);
        Schedules agendaConfigurada = ConfigAgendaBase.getInstance().getSchedule();

        if(agendaConfigurada != null){
            for (ServiceDays serviceDays: agendaConfigurada.getServiceDays()) {

                if(serviceDays.day.equals("Sunday")){
                    dom.setBackground(drawable);
                    cardDom.setVisibility(View.VISIBLE);
                    dom_selected = true;

                    hora_inicio_dom.setText(configurarHora(serviceDays.startTime));
                    hora_termino_dom.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_dom.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_dom.setText(configurarHora(serviceDays.lunchEndTime));
                }

                if(serviceDays.day.equals("Monday")){
                    seg.setBackground(drawable);
                    cardSeg.setVisibility(View.VISIBLE);
                    seg_selected = true;

                    hora_inicio_seg.setText(configurarHora(serviceDays.startTime));
                    hora_termino_seg.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_seg.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_seg.setText(configurarHora(serviceDays.lunchEndTime));
                }

                if(serviceDays.day.equals("Tuesday")){
                    ter.setBackground(drawable);
                    cardTer.setVisibility(View.VISIBLE);
                    ter_selected = true;

                    hora_inicio_ter.setText(configurarHora(serviceDays.startTime));
                    hora_termino_ter.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_ter.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_ter.setText(configurarHora(serviceDays.lunchEndTime));
                }

                if(serviceDays.day.equals("Wednesday")){
                    qua.setBackground(drawable);
                    cardQua.setVisibility(View.VISIBLE);
                    qua_selected = true;

                    hora_inicio_qua.setText(configurarHora(serviceDays.startTime));
                    hora_termino_qua.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_qua.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_qua.setText(configurarHora(serviceDays.lunchEndTime));
                }

                if(serviceDays.day.equals("Thursday")){
                    qui.setBackground(drawable);
                    cardQui.setVisibility(View.VISIBLE);
                    qui_selected = true;

                    hora_inicio_qui.setText(configurarHora(serviceDays.startTime));
                    hora_termino_qui.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_qui.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_qui.setText(configurarHora(serviceDays.lunchEndTime));
                }

                if(serviceDays.day.equals("Friday")){
                    sex.setBackground(drawable);
                    cardSex.setVisibility(View.VISIBLE);
                    sex_selected = true;

                    hora_inicio_sex.setText(configurarHora(serviceDays.startTime));
                    hora_termino_sex.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_sex.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_sex.setText(configurarHora(serviceDays.lunchEndTime));
                }

                if(serviceDays.day.equals("Saturday")){
                    sab.setBackground(drawable);
                    cardSab.setVisibility(View.VISIBLE);
                    sab_selected = true;

                    hora_inicio_sab.setText(configurarHora(serviceDays.startTime));
                    hora_termino_sab.setText(configurarHora(serviceDays.endTime));
                    inicio_intervalo_sab.setText(configurarHora(serviceDays.lunchStartTime));
                    fim_intervalo_sab.setText(configurarHora(serviceDays.lunchEndTime));
                }
            }
        }
    }

    public String configurarHora(int horaEmMinutos){
        double d = horaEmMinutos;
        // vamos converter para segundos primeiro
        long h = Math.round(horaEmMinutos / 60);
        long m = Math.round(horaEmMinutos - (h * 60));

        long minutos = ( h * 60) + m;

        if(h < 9){
            if(m < 9){
                return "0" + h + ":0" + m;
            } else {
                return "0" + h + ":" + m;
            }
        } else {
            if(m < 9){
                return h + ":0" + m;
            } else {
                return h + ":" + m;
            }
        }
    }

    private void salvarConfigAgenda() {
        String startTime_txt, endTime_txt, lunchStartTime_txt, lunchEndTime_txt;
        int startTime, endTime, lunchStartTime, lunchEndTime;

        if(dom_selected){
            startTime_txt = hora_inicio_dom.getText().toString();
            endTime_txt = hora_termino_dom.getText().toString();
            lunchStartTime_txt = inicio_intervalo_dom.getText().toString();
            lunchEndTime_txt = fim_intervalo_dom.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")){
                startTime = (Integer.parseInt(startTime_txt.substring(0,2)) * 60) + Integer.parseInt(startTime_txt.substring(3,5));
                endTime = (Integer.parseInt(endTime_txt.substring(0,2)) * 60) + Integer.parseInt(endTime_txt.substring(3,5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0,2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3,5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0,2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3,5));

                ServiceDays sunday = new ServiceDays("Sunday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(sunday);

                dom_valid = true;
            }
        }

        if(seg_selected){
            startTime_txt = hora_inicio_seg.getText().toString();
            endTime_txt = hora_termino_seg.getText().toString();
            lunchStartTime_txt = inicio_intervalo_seg.getText().toString();
            lunchEndTime_txt = fim_intervalo_seg.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")) {
                startTime = (Integer.parseInt(startTime_txt.substring(0, 2)) * 60) + Integer.parseInt(startTime_txt.substring(3, 5));
                endTime = (Integer.parseInt(endTime_txt.substring(0, 2)) * 60) + Integer.parseInt(endTime_txt.substring(3, 5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3, 5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3, 5));

                ServiceDays monday = new ServiceDays("Monday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(monday);
                seg_valid = true;
            }
        }

        if(ter_selected){
            startTime_txt = hora_inicio_ter.getText().toString();
            endTime_txt = hora_termino_ter.getText().toString();
            lunchStartTime_txt = inicio_intervalo_ter.getText().toString();
            lunchEndTime_txt = fim_intervalo_ter.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")) {
                startTime = (Integer.parseInt(startTime_txt.substring(0, 2)) * 60) + Integer.parseInt(startTime_txt.substring(3, 5));
                endTime = (Integer.parseInt(endTime_txt.substring(0, 2)) * 60) + Integer.parseInt(endTime_txt.substring(3, 5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3, 5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3, 5));

                ServiceDays tuesday = new ServiceDays("Tuesday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(tuesday);
                ter_valid = true;
            }
        }

        if(qua_selected){
            startTime_txt = hora_inicio_qua.getText().toString();
            endTime_txt = hora_termino_qua.getText().toString();
            lunchStartTime_txt = inicio_intervalo_qua.getText().toString();
            lunchEndTime_txt = fim_intervalo_qua.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")) {
                startTime = (Integer.parseInt(startTime_txt.substring(0, 2)) * 60) + Integer.parseInt(startTime_txt.substring(3, 5));
                endTime = (Integer.parseInt(endTime_txt.substring(0, 2)) * 60) + Integer.parseInt(endTime_txt.substring(3, 5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3, 5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3, 5));

                ServiceDays wednesday = new ServiceDays("Wednesday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(wednesday);
                qua_valid = true;
            }
        }

        if(qui_selected){
            startTime_txt = hora_inicio_qui.getText().toString();
            endTime_txt = hora_termino_qui.getText().toString();
            lunchStartTime_txt = inicio_intervalo_qui.getText().toString();
            lunchEndTime_txt = fim_intervalo_qui.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")) {
                startTime = (Integer.parseInt(startTime_txt.substring(0, 2)) * 60) + Integer.parseInt(startTime_txt.substring(3, 5));
                endTime = (Integer.parseInt(endTime_txt.substring(0, 2)) * 60) + Integer.parseInt(endTime_txt.substring(3, 5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3, 5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3, 5));

                ServiceDays thursday = new ServiceDays("Thursday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(thursday);
                qui_valid = true;
            }
        }

        if(sex_selected){
            startTime_txt = hora_inicio_sex.getText().toString();
            endTime_txt = hora_termino_sex.getText().toString();
            lunchStartTime_txt = inicio_intervalo_sex.getText().toString();
            lunchEndTime_txt = fim_intervalo_sex.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")) {
                startTime = (Integer.parseInt(startTime_txt.substring(0, 2)) * 60) + Integer.parseInt(startTime_txt.substring(3, 5));
                endTime = (Integer.parseInt(endTime_txt.substring(0, 2)) * 60) + Integer.parseInt(endTime_txt.substring(3, 5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3, 5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3, 5));

                ServiceDays friday = new ServiceDays("Friday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(friday);
                sex_valid = true;
            }
        }

        if(sab_selected){
            startTime_txt = hora_inicio_sab.getText().toString();
            endTime_txt = hora_termino_sab.getText().toString();
            lunchStartTime_txt = inicio_intervalo_sab.getText().toString();
            lunchEndTime_txt = fim_intervalo_sab.getText().toString();

            if(!startTime_txt.equals("") || !endTime_txt.equals("") || !lunchStartTime_txt.equals("") || !lunchEndTime_txt.equals("")) {
                startTime = (Integer.parseInt(startTime_txt.substring(0, 2)) * 60) + Integer.parseInt(startTime_txt.substring(3, 5));
                endTime = (Integer.parseInt(endTime_txt.substring(0, 2)) * 60) + Integer.parseInt(endTime_txt.substring(3, 5));
                lunchStartTime = (Integer.parseInt(lunchStartTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchStartTime_txt.substring(3, 5));
                lunchEndTime = (Integer.parseInt(lunchEndTime_txt.substring(0, 2)) * 60) + Integer.parseInt(lunchEndTime_txt.substring(3, 5));

                ServiceDays saturday = new ServiceDays("Saturday", startTime, endTime, lunchStartTime, lunchEndTime);
                serviceDays.add(saturday);
                sab_valid = true;
            }
        }

        if(dom_valid || seg_valid || ter_valid || qua_valid || qui_valid || sex_valid || sab_valid){
            enviarDados();
        }
    }

    private void enviarDados() {
        Map<String, Object> data = new HashMap<>();
        data.put("active", true);
        data.put("professionalId", userID);
        data.put("serviceDays", serviceDays);

        String idAgenda = ConfigAgendaBase.getInstance().getScheduleId();
        if(idAgenda.equals("")){
            db.collection("schedules").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(ConfigurarAgenda.this, "Agenda criada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            db.collection("schedules").document(idAgenda).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ConfigAgendaBase.getInstance().setServiceDays(serviceDays);
                    Toast.makeText(ConfigurarAgenda.this, "Agenda atualizada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void maskController() {
        hora_inicio_dom.addTextChangedListener(MaskEditUtil.mask(hora_inicio_dom, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_dom.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_dom, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_dom.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_dom, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_dom.addTextChangedListener(MaskEditUtil.mask(hora_termino_dom, MaskEditUtil.FORMAT_HORA_MINUTOS));

        hora_inicio_seg.addTextChangedListener(MaskEditUtil.mask(hora_inicio_seg, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_seg.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_seg, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_seg.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_seg, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_seg.addTextChangedListener(MaskEditUtil.mask(hora_termino_seg, MaskEditUtil.FORMAT_HORA_MINUTOS));

        hora_inicio_ter.addTextChangedListener(MaskEditUtil.mask(hora_inicio_ter, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_ter.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_ter, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_ter.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_ter, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_ter.addTextChangedListener(MaskEditUtil.mask(hora_termino_ter, MaskEditUtil.FORMAT_HORA_MINUTOS));

        hora_inicio_qua.addTextChangedListener(MaskEditUtil.mask(hora_inicio_qua, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_qua.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_qua, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_qua.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_qua, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_qua.addTextChangedListener(MaskEditUtil.mask(hora_termino_qua, MaskEditUtil.FORMAT_HORA_MINUTOS));

        hora_inicio_qui.addTextChangedListener(MaskEditUtil.mask(hora_inicio_qui, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_qui.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_qui, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_qui.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_qui, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_qui.addTextChangedListener(MaskEditUtil.mask(hora_termino_qui, MaskEditUtil.FORMAT_HORA_MINUTOS));

        hora_inicio_sex.addTextChangedListener(MaskEditUtil.mask(hora_inicio_sex, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_sex.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_sex, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_sex.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_sex, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_sex.addTextChangedListener(MaskEditUtil.mask(hora_termino_sex, MaskEditUtil.FORMAT_HORA_MINUTOS));

        hora_inicio_sab.addTextChangedListener(MaskEditUtil.mask(hora_inicio_sab, MaskEditUtil.FORMAT_HORA_MINUTOS));
        inicio_intervalo_sab.addTextChangedListener(MaskEditUtil.mask(inicio_intervalo_sab, MaskEditUtil.FORMAT_HORA_MINUTOS));
        fim_intervalo_sab.addTextChangedListener(MaskEditUtil.mask(fim_intervalo_sab, MaskEditUtil.FORMAT_HORA_MINUTOS));
        hora_termino_sab.addTextChangedListener(MaskEditUtil.mask(hora_termino_sab, MaskEditUtil.FORMAT_HORA_MINUTOS));
    }

    public void onClickController(){
        final Drawable drawable = ContextCompat.getDrawable(this, R.drawable.background_day_schedule_selected);
        final Drawable drawableOriginal = ContextCompat.getDrawable(this, R.drawable.background_day_schedule);

        dom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dom.getBackground() == drawable){
                    dom.setBackground(drawableOriginal);
                    cardDom.setVisibility(View.GONE);
                    dom_selected = false;
                }else {
                    dom.setBackground(drawable);
                    cardDom.setVisibility(View.VISIBLE);
                    dom_selected = true;
                }
            }
        });

        seg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seg.getBackground() == drawable){
                    seg.setBackground(drawableOriginal);
                    cardSeg.setVisibility(View.GONE);
                    seg_selected = false;
                }else {
                    seg.setBackground(drawable);
                    cardSeg.setVisibility(View.VISIBLE);
                    seg_selected = true;
                }
            }
        });

        ter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ter.getBackground() == drawable){
                    ter.setBackground(drawableOriginal);
                    cardTer.setVisibility(View.GONE);
                    ter_selected = false;
                }else {
                    ter.setBackground(drawable);
                    cardTer.setVisibility(View.VISIBLE);
                    ter_selected = true;
                }
            }
        });

        qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qua.getBackground() == drawable){
                    qua.setBackground(drawableOriginal);
                    cardQua.setVisibility(View.GONE);
                    qua_selected = false;
                }else {
                    qua.setBackground(drawable);
                    cardQua.setVisibility(View.VISIBLE);
                    qua_selected = true;
                }
            }
        });

        qui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qui.getBackground() == drawable){
                    qui.setBackground(drawableOriginal);
                    cardQui.setVisibility(View.GONE);
                    qui_selected = false;
                }else {
                    qui.setBackground(drawable);
                    cardQui.setVisibility(View.VISIBLE);
                    qui_selected = true;
                }
            }
        });

        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sex.getBackground() == drawable){
                    sex.setBackground(drawableOriginal);
                    cardSex.setVisibility(View.GONE);
                    sex_selected = false;
                }else {
                    sex.setBackground(drawable);
                    cardSex.setVisibility(View.VISIBLE);
                    sex_selected = true;
                }
            }
        });

        sab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sab.getBackground() == drawable){
                    sab.setBackground(drawableOriginal);
                    cardSab.setVisibility(View.GONE);
                    sab_selected = false;
                }else {
                    sab.setBackground(drawable);
                    cardSab.setVisibility(View.VISIBLE);
                    sab_selected = true;
                }
            }
        });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarConfigAgenda();
            }
        });
    }


    public void inicializarComponentes(){
        dom = (CardView) findViewById(R.id.cardViewDom);
        seg = (CardView) findViewById(R.id.cardViewSeg);
        ter = (CardView) findViewById(R.id.cardViewTer);
        qua = (CardView) findViewById(R.id.cardViewQua);
        qui = (CardView) findViewById(R.id.cardViewQui);
        sex = (CardView) findViewById(R.id.cardViewSex);
        sab = (CardView) findViewById(R.id.cardViewSab);

        cardDom = (CardView) findViewById(R.id.cardDom);
        cardSeg = (CardView) findViewById(R.id.cardSeg);
        cardTer = (CardView) findViewById(R.id.cardTer);
        cardQua = (CardView) findViewById(R.id.cardQua);
        cardQui = (CardView) findViewById(R.id.cardQui);
        cardSex = (CardView) findViewById(R.id.cardSex);
        cardSab = (CardView) findViewById(R.id.cardSab);

        hora_inicio_dom = (EditText) findViewById(R.id.hora_inicio_dom);
        inicio_intervalo_dom = (EditText) findViewById(R.id.inicio_intervalo_dom);
        fim_intervalo_dom = (EditText) findViewById(R.id.fim_intervalo_dom);
        hora_termino_dom = (EditText) findViewById(R.id.hora_termino_dom);

        hora_inicio_seg = (EditText) findViewById(R.id.hora_inicio_seg);
        inicio_intervalo_seg = (EditText) findViewById(R.id.inicio_intervalo_seg);
        fim_intervalo_seg = (EditText) findViewById(R.id.fim_intervalo_seg);
        hora_termino_seg = (EditText) findViewById(R.id.hora_termino_seg);

        hora_inicio_ter = (EditText) findViewById(R.id.hora_inicio_ter);
        inicio_intervalo_ter = (EditText) findViewById(R.id.inicio_intervalo_ter);
        fim_intervalo_ter = (EditText) findViewById(R.id.fim_intervalo_ter);
        hora_termino_ter = (EditText) findViewById(R.id.hora_termino_ter);

        hora_inicio_qua = (EditText) findViewById(R.id.hora_inicio_qua);
        inicio_intervalo_qua = (EditText) findViewById(R.id.inicio_intervalo_qua);
        fim_intervalo_qua = (EditText) findViewById(R.id.fim_intervalo_qua);
        hora_termino_qua = (EditText) findViewById(R.id.hora_termino_qua);

        hora_inicio_qui = (EditText) findViewById(R.id.hora_inicio_qui);
        inicio_intervalo_qui = (EditText) findViewById(R.id.inicio_intervalo_qui);
        fim_intervalo_qui = (EditText) findViewById(R.id.fim_intervalo_qui);
        hora_termino_qui = (EditText) findViewById(R.id.hora_termino_qui);

        hora_inicio_sex = (EditText) findViewById(R.id.hora_inicio_sex);
        inicio_intervalo_sex = (EditText) findViewById(R.id.inicio_intervalo_sex);
        fim_intervalo_sex = (EditText) findViewById(R.id.fim_intervalo_sex);
        hora_termino_sex = (EditText) findViewById(R.id.hora_termino_sex);

        hora_inicio_sab = (EditText) findViewById(R.id.hora_inicio_sab);
        inicio_intervalo_sab = (EditText) findViewById(R.id.inicio_intervalo_sab);
        fim_intervalo_sab = (EditText) findViewById(R.id.fim_intervalo_sab);
        hora_termino_sab = (EditText) findViewById(R.id.hora_termino_sab);

        btn_salvar = (Button) findViewById(R.id.btn_salvar_config_agenda);
        btn_voltar = (Button) findViewById(R.id.btn_voltar_config_agenda);
    }
}