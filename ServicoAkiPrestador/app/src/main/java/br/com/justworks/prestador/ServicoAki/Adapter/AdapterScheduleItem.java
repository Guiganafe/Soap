package br.com.justworks.prestador.ServicoAki.Adapter;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.R;

public class AdapterScheduleItem extends RecyclerView.Adapter<AdapterScheduleItem.SchedulesItemHolder>{

    ArrayList<ScheduleItems> schedulesList;
    private RequestBuilder<PictureDrawable> requestBuilder;
    private onScheduleItemListenner onScheduleItemListenner;

    public AdapterScheduleItem(ArrayList<ScheduleItems> servicesList, onScheduleItemListenner onScheduleItemListenner){
        this.schedulesList = servicesList;
        this.onScheduleItemListenner = onScheduleItemListenner;
    }

    @NonNull
    @Override
    public SchedulesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_item,
                parent, false);
        return new SchedulesItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SchedulesItemHolder holder, int position) {
        //Glide.with(context).load(model.getImageUrl()).into(holder.imgShops);

        int horaInicio = schedulesList.get(position).getHourBegin().toDate().getHours();
        int minInicio = schedulesList.get(position).getHourBegin().toDate().getMinutes();

        int horaFim = schedulesList.get(position).getHourEnd().toDate().getHours();
        int minFim = schedulesList.get(position).getHourEnd().toDate().getMinutes();

        if(schedulesList.get(position).getServices() != null){
            int qtdServico = schedulesList.get(position).getServices().size();
            if(qtdServico > 1){
                holder.textView_servico_quantidade.setText(String.format(Locale.getDefault(),"%d serviços", qtdServico));
            } else {
                holder.textView_servico_quantidade.setText(String.format(Locale.getDefault(),"%d serviço", qtdServico));
            }
        } else {
            holder.textView_servico_quantidade.setText(String.format(Locale.getDefault(),"Sem serviços"));
        }


        holder.textView_servico_titulo.setText(schedulesList.get(position).getTitle());

        if(minInicio < 9){
            if(minFim < 9){
                holder.textView_servico_hora.setText(String.format(Locale.getDefault(),"%d:0%dh às %d:0%dh", horaInicio, minInicio, horaFim, minFim));
            } else {
                holder.textView_servico_hora.setText(String.format(Locale.getDefault(),"%d:0%dh às %d:%dh", horaInicio, minInicio, horaFim, minFim));
            }
        } else if(minFim < 9){
            holder.textView_servico_hora.setText(String.format(Locale.getDefault(),"%d:%dh às %d:0%dh", horaInicio, minInicio, horaFim, minFim));
        } else {
            holder.textView_servico_hora.setText(String.format(Locale.getDefault(),"%d:%dh às %d:%dh", horaInicio, minInicio, horaFim, minFim));
        }
    }

    @Override
    public int getItemCount() {
        return schedulesList.size();
    }

    class SchedulesItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imagem_servico;
        TextView textView_servico_hora, textView_servico_titulo, textView_servico_quantidade;

        public SchedulesItemHolder(@NonNull View itemView) {
            super(itemView);

            imagem_servico = itemView.findViewById(R.id.imagem_servico);
            textView_servico_hora = itemView.findViewById(R.id.textView_servico_hora);
            textView_servico_titulo = itemView.findViewById(R.id.textView_servico_titulo);
            textView_servico_quantidade = itemView.findViewById(R.id.textView_servico_quantidade);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onScheduleItemListenner.onScheduleItemClick(getAdapterPosition());
        }
    }

    public interface onScheduleItemListenner {
        void onScheduleItemClick(int position);
    }
}
