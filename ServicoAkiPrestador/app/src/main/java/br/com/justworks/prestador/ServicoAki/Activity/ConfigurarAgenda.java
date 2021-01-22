package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
            qua_selected = false, qui_selected = false, sex_selected = false, sab_selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_agenda);
        inicializarComponentes();
        onClickController();
        maskController();
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
                    qui_selected = false;
                }else {
                    sex.setBackground(drawable);
                    cardSex.setVisibility(View.VISIBLE);
                    qui_selected = true;
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
    }
}