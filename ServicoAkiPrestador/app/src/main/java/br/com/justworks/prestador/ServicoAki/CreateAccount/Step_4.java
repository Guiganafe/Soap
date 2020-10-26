package br.com.justworks.prestador.ServicoAki.CreateAccount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.justworks.prestador.ServicoAki.R;


public class Step_4 extends Fragment {

    private Button btn_voltar_cadastro_step_3,  btn_avancar_cadastro_step_5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarComponentes(view);

        onClick();
    }

    private void onClick() {
        btn_voltar_cadastro_step_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_avancar_cadastro_step_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_step_4_to_step_5_1);
            }
        });
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_3 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_3);
        btn_avancar_cadastro_step_5 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_5);
    }
}