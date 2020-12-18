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

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.ServiceUser;
import br.com.justworks.prestador.ServicoAki.R;

public class ServiceSelectedAdapter extends RecyclerView.Adapter<ServiceSelectedAdapter.ServiceSelectedViewHolder> {

    ArrayList<ServiceUser> servicesList;
    Context context;
    private onServiceSelectedListenner mOnServiceListenner;
    private RequestBuilder<PictureDrawable> requestBuilder;

    public ServiceSelectedAdapter(ArrayList<ServiceUser> servicesList, Context context, onServiceSelectedListenner onServiceListenner) {
        this.servicesList = servicesList;
        this.context = context;
        this.mOnServiceListenner = onServiceListenner;
    }

    @NonNull
    @Override
    public ServiceSelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servico_item_evento_selecionado,
                parent, false);
        return new ServiceSelectedViewHolder(v, mOnServiceListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceSelectedViewHolder holder, int position) {
        ArrayList<CategoriesServices> cat = servicesList.get(position).getCategory();

        requestBuilder = Glide.with(context).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(cat.get(0).getImageIconUrl().getSvg());
        requestBuilder
                .load(uri)
                .into(holder.img_selected_service_cat);
        holder.textView_service_titulo_evento.setText(servicesList.get(position).getName().getPtbr());
        holder.textView_duracao_service_evento.setText("Duração: " + servicesList.get(position).getAvgExecutionTime() + " min");
        holder.textView_preco_servico_evento.setText("Preço: R$ " + servicesList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public static class ServiceSelectedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_service_titulo_evento, textView_duracao_service_evento, textView_preco_servico_evento;
        ImageView img_selected_service, img_selected_service_cat;
        onServiceSelectedListenner onServiceEventoListenner;

        public ServiceSelectedViewHolder(@NonNull View itemView, onServiceSelectedListenner onServiceListenner) {
            super(itemView);
            textView_service_titulo_evento = itemView.findViewById(R.id.textView_servico_titulo_evento_selecionado);
            textView_duracao_service_evento = itemView.findViewById(R.id.textView_duracao_servico_evento_selecionado);
            img_selected_service = itemView.findViewById(R.id.img_servico_evento_selecionado);
            img_selected_service_cat = itemView.findViewById(R.id.imagem_servico_evento_selecionado);
            textView_preco_servico_evento = itemView.findViewById(R.id.textView_preco_servico_evento_selecionado);
            this.onServiceEventoListenner = onServiceListenner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onServiceEventoListenner.onServiceClick(getAdapterPosition());
        }
    }

    public interface onServiceSelectedListenner {
        void onServiceClick(int position);
    }
}
