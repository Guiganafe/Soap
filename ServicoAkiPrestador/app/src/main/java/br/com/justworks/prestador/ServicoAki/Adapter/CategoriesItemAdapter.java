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

import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.R;

public class CategoriesItemAdapter extends FirestoreRecyclerAdapter<CategoriesServices, CategoriesItemAdapter.CategoriesItemHolder> {
    private static final String TAG = "CategoriesAdapter";
    private OnItemClickListener listener;
    private Context context;
    private RequestBuilder<PictureDrawable> requestBuilder;

    public CategoriesItemAdapter(@NonNull FirestoreRecyclerOptions<CategoriesServices> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoriesItemHolder holder, int position, @NonNull CategoriesServices model) {
        requestBuilder = Glide.with(context).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(model.getImageIconUrl().getSvg());
        requestBuilder
                .load(uri)
                .into(holder.imagem_categoria);

        String nome_categoria = model.getName().getPtbr();
        int quantidade_servico = model.getQtdServices();

        holder.textView_categoria_nome.setText(nome_categoria);
        holder.textView_categoria_qtd.setText(quantidade_servico + " Serviços disponíveis");
    }

    @NonNull
    @Override
    public CategoriesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_item,
                parent, false);
        return new CategoriesItemHolder(v);
    }

    class CategoriesItemHolder extends RecyclerView.ViewHolder{
        ImageView imagem_categoria;
        TextView textView_categoria_nome, textView_categoria_qtd;

        public CategoriesItemHolder(@NonNull View itemView) {
            super(itemView);
            imagem_categoria = itemView.findViewById(R.id.imagem_categoria);
            textView_categoria_nome = itemView.findViewById(R.id.textView_categoria_titulo);
            textView_categoria_qtd = itemView.findViewById(R.id.textView_categoria_quantidade);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
