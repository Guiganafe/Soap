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

public class ServicesListAdapter extends RecyclerView.Adapter<ServicesListAdapter.ServicesListViewHolder> {

    ArrayList<ServiceUser> servicesList;
    Context context;
    private RequestBuilder<PictureDrawable> requestBuilder;
    private onServiceListenner mOnServiceListenner;

    public ServicesListAdapter(ArrayList<ServiceUser> servicesList, Context context, onServiceListenner onServiceListenner){
        this.servicesList = servicesList;
        this.context = context;
        this.mOnServiceListenner = onServiceListenner;
    }

    @NonNull
    @Override
    public ServicesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servico_item,
                parent, false);
        return new ServicesListViewHolder(v, mOnServiceListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesListViewHolder holder, int position) {
        ArrayList<CategoriesServices> cat = servicesList.get(position).getCategory();

        requestBuilder = Glide.with(context).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(cat.get(0).getImageIconUrl().getSvg());
        requestBuilder
                .load(uri)
                .into(holder.imagem_service);

        holder.textView_service_titulo.setText(servicesList.get(position).getName().getPtbr());
        holder.textView_categoria_service.setText(cat.get(0).getName().getPtbr());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public class ServicesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagem_service;
        TextView textView_service_titulo, textView_categoria_service;
        onServiceListenner onServiceListenner;

        public ServicesListViewHolder(@NonNull View itemView, onServiceListenner onServiceListenner) {
            super(itemView);
            imagem_service = itemView.findViewById(R.id.imagem_servico);
            textView_service_titulo = itemView.findViewById(R.id.textView_servico_titulo);
            textView_categoria_service = itemView.findViewById(R.id.textView_categoria_servico);
            this.onServiceListenner = onServiceListenner;

            itemView.setOnClickListener(this);
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
