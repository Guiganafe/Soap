package br.com.justworks.prestador.ServicoAki.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.R;

public class AddressItemAdapter extends RecyclerView.Adapter<AddressItemAdapter.AddressItemViewHolder> {

    private onAddressListenner OnAdressListenner;
    private ArrayList<Address> addressArrayList;
    private Context context;

    public AddressItemAdapter(ArrayList<Address> addressArrayList, Context context, onAddressListenner onAddressListenner){
        this.addressArrayList = addressArrayList;
        this.context = context;
        this.OnAdressListenner = onAddressListenner;
    }

    @NonNull
    @Override
    public AddressItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.endereco_item,
                parent, false);
        return new AddressItemViewHolder(v, OnAdressListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressItemViewHolder holder, int position) {
        String nome_endereco = addressArrayList.get(position).getAddressType();
        String rua_endereco = addressArrayList.get(position).getStreet() + ", " + addressArrayList.get(position).getNumber();
        String bairro_endereco = addressArrayList.get(position).getNeighborhood();

        holder.textView_nome_endereco.setText(nome_endereco);
        holder.textView_rua_endereco.setText(rua_endereco);
        holder.textView_bairro_endereco.setText(bairro_endereco);
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public class AddressItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView_nome_endereco, textView_rua_endereco, textView_bairro_endereco;
        onAddressListenner onAddressListenner;

        public AddressItemViewHolder(@NonNull View itemView, onAddressListenner mOnAddressListenner) {
            super(itemView);
            textView_nome_endereco = itemView.findViewById(R.id.textView_nome_endereco);
            textView_rua_endereco = itemView.findViewById(R.id.textView_rua_endereco);
            textView_bairro_endereco = itemView.findViewById(R.id.textView_bairro_endereco);
            this.onAddressListenner = mOnAddressListenner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAddressListenner.onAddressClick(getAdapterPosition());
        }
    }

    public interface onAddressListenner {
        void onAddressClick(int position);
    }
}
