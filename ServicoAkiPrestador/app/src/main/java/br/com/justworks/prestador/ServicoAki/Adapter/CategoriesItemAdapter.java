package br.com.justworks.prestador.ServicoAki.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import br.com.justworks.prestador.ServicoAki.Model.CategoriesServices;
import br.com.justworks.prestador.ServicoAki.R;

public class CategoriesItemAdapter extends FirestoreRecyclerAdapter<CategoriesServices, CategoriesItemAdapter.CategoriesItemHolder> {
    private static final String TAG = "CategoriesAdapter";
    private Context context;

    public CategoriesItemAdapter(@NonNull FirestoreRecyclerOptions<CategoriesServices> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoriesItemHolder holder, int position, @NonNull CategoriesServices model) {
        //Glide.with(context).load(model.getImageIconUrl().getSvg()).into(holder.imagem_categoria);
        String nome_categoria = model.getName().getPtbr();
        int quantidade_servico = model.getQtdServices();

        holder.textView_categoria_nome.setText(nome_categoria);
        holder.textView_categoria_qtd.setText(quantidade_servico);
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
        }
    }
}
