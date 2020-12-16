package br.com.justworks.prestador.ServicoAki.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;

public class ServiceListEventoAdapter extends RecyclerView.Adapter<ServiceListEventoAdapter.ServicesListEventoViewHolder>{

    ArrayList<ServiceUser> servicesList;
    Context context;
    private onServiceListenner mOnServiceListenner;

    public ServiceListEventoAdapter(ArrayList<ServiceUser> servicesList, Context context, onServiceListenner onServiceListenner){
        this.servicesList = servicesList;
        this.context = context;
        this.mOnServiceListenner = onServiceListenner;
    }

    @NonNull
    @Override
    public ServicesListEventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servico_item_evento,
                parent, false);
        return new ServicesListEventoViewHolder(v, mOnServiceListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesListEventoViewHolder holder, int position) {
        holder.textView_service_titulo_evento.setText(servicesList.get(position).getName().getPtbr());
        holder.textView_duracao_service_evento.setText(servicesList.get(position).getAvgExecutionTime() + "min");
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public void fillterList(ArrayList<ServiceUser> filteredArray){

    }


    public class ServicesListEventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_service_titulo_evento, textView_duracao_service_evento;
        ImageView img_selected_service;
        onServiceListenner onServiceListenner;

        public ServicesListEventoViewHolder(@NonNull View itemView, onServiceListenner onServiceListenner) {
            super(itemView);
            textView_service_titulo_evento = itemView.findViewById(R.id.textView_servico_titulo_evento);
            textView_duracao_service_evento = itemView.findViewById(R.id.textView_duracao_servico_evento);
            img_selected_service = itemView.findViewById(R.id.img_servico_evento);
            this.onServiceListenner = onServiceListenner;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Drawable drawableCheck = v.getResources().getDrawable(R.drawable.ic_check);
                    Drawable drawableAdd = v.getResources().getDrawable(R.drawable.ic_add_black);

                    if(img_selected_service.getDrawable().equals(drawableCheck)){
                        img_selected_service.setImageDrawable(drawableAdd);
                    } else if(img_selected_service.getDrawable().equals(drawableAdd)){
                        img_selected_service.setImageDrawable(drawableCheck);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            onServiceListenner.onServiceClick(getAdapterPosition());
        }
    }

    public interface onServiceListenner {
        void onServiceClick(int position);
    }
}
