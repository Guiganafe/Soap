package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import br.com.justworks.prestador.ServicoAki.Adapter.AddressItemAdapter;
import br.com.justworks.prestador.ServicoAki.Base.EnderecoBase;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.R;

public class MeusEnderecos extends AppCompatActivity implements AddressItemAdapter.onAddressListenner{

    private TextView tv_nenhum_endereco, tv_nenhum_endereco_sub;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Address> addressArrayList;
    private Context context = this;
    private FloatingActionButton floatButton;
    private AddressItemAdapter.onAddressListenner addressListenner = (AddressItemAdapter.onAddressListenner) this.context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_enderecos);

        inicializarComponentes();

        setUpReciclerView();

        onClickController();
    }

    private void onClickController() {
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEndereco = new Intent(context, AdicionarEndereco.class);
                startActivity(addEndereco);
            }
        });
    }

    private void setUpReciclerView() {
        addressArrayList = EnderecoBase.getInstance().getAddressItemsList();
        if(addressArrayList != null && addressArrayList.size() >= 1){
            recyclerView.setVisibility(View.VISIBLE);
            tv_nenhum_endereco.setVisibility(View.GONE);
            tv_nenhum_endereco_sub.setVisibility(View.GONE);
            adapter = new AddressItemAdapter(addressArrayList, context, addressListenner);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            tv_nenhum_endereco.setVisibility(View.VISIBLE);
            tv_nenhum_endereco_sub.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAddressClick(int position) {
        Toast.makeText(context, "position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpReciclerView();
    }

    @Override
    public void onStop() {
        super.onStop();
        setUpReciclerView();
    }

    private void inicializarComponentes() {
        tv_nenhum_endereco = (TextView) findViewById(R.id.tv_sem_endereco_titulo);
        tv_nenhum_endereco_sub = (TextView) findViewById(R.id.tv_sem_endereco_desc);
        recyclerView = (RecyclerView) findViewById(R.id.reciclerView_UserAddresses);
        floatButton = (FloatingActionButton) findViewById(R.id.btn_add_endereco);
    }
}