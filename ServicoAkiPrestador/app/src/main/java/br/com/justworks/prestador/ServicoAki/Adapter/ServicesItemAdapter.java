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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.Model.Services;
import br.com.justworks.prestador.ServicoAki.R;

public class ServicesItemAdapter extends FirestoreRecyclerAdapter<Services, ServicesItemAdapter.ServicesItemHolder> {
    private OnItemClickListener listener;
    private Context context;
    private RequestBuilder<PictureDrawable> requestBuilder;

    public ServicesItemAdapter(@NonNull FirestoreRecyclerOptions<Services> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ServicesItemHolder holder, int position, @NonNull Services model) {
        String nome_servico = model.getName().getPtbr();
        ArrayList<CategoriesServices> cat = model.getCategory();

        requestBuilder = Glide.with(context).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(cat.get(0).getImageIconUrl().getSvg());
        requestBuilder
                .load(uri)
                .into(holder.imagem_service);

        holder.textView_service_titulo.setText(nome_servico);
        holder.textView_categoria_service.setText(cat.get(0).getName().getPtbr());
    }

    @NonNull
    @Override
    public ServicesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servico_item,
                parent, false);
        return new ServicesItemHolder(v);
    }

    class ServicesItemHolder extends RecyclerView.ViewHolder{
        ImageView imagem_service;
        TextView textView_service_titulo, textView_categoria_service;

        public ServicesItemHolder(@NonNull View itemView) {
            super(itemView);
            imagem_service = itemView.findViewById(R.id.imagem_servico);
            textView_service_titulo = itemView.findViewById(R.id.textView_servico_titulo);
            textView_categoria_service = itemView.findViewById(R.id.textView_categoria_servico);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position, v);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}



