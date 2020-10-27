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

import java.lang.reflect.Array;
import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.ScheduleItems;
import br.com.justworks.prestador.ServicoAki.R;

public class SchedulesItemAdapter extends FirestoreRecyclerAdapter<ScheduleItems, SchedulesItemAdapter.SchedulesItemHolder> {
    private static final String TAG = "ShopsAdapter";
    private Context context;

    public SchedulesItemAdapter(@NonNull FirestoreRecyclerOptions<ScheduleItems> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SchedulesItemHolder holder, int position, @NonNull ScheduleItems model) {
        //Glide.with(context).load(model.getImageUrl()).into(holder.imgShops);
        String[] horaInicio = model.getHourBegin().split(" ");
        String[] horaFim = model.getHourEnd().split(" ");
//        holder.textView_servico_hora.setText("Das: " + horaInicio[3] + " as " + horaFim[3]);
        holder.textView_servico_titulo.setText(model.getTitle());
    }

    @NonNull
    @Override
    public SchedulesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_item,
                parent, false);
        return new SchedulesItemHolder(v);
    }

    class SchedulesItemHolder extends RecyclerView.ViewHolder{
        ImageView imagem_servico;
        TextView textView_servico_hora, textView_servico_titulo, textView_servico_quantidade;

        public SchedulesItemHolder(@NonNull View itemView) {
            super(itemView);

            imagem_servico = itemView.findViewById(R.id.imagem_servico);
            textView_servico_hora = itemView.findViewById(R.id.textView_servico_hora);
            textView_servico_titulo = itemView.findViewById(R.id.textView_servico_titulo);
            textView_servico_quantidade = itemView.findViewById(R.id.textView_servico_quantidade);
        }
    }
}
