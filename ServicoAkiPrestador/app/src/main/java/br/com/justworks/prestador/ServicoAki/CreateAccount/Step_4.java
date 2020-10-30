package br.com.justworks.prestador.ServicoAki.CreateAccount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import br.com.justworks.prestador.ServicoAki.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;


public class Step_4 extends Fragment {

    private Button btn_voltar_cadastro_step_3,  btn_avancar_cadastro_step_5;
    private ProfissionalViewModel profissionalViewModel;
    private ImageView foto_id;
    private Button tirar_foto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
    }

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
        loadController();
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

        tirar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Salva a imagem no bitmap
            profissionalViewModel.setFoto_selfie_doc(imageBitmap);
            foto_id.setImageBitmap(imageBitmap);
            tirar_foto.setText("Tirar outra");
        }
    }

    private void loadController() {
        if(profissionalViewModel.getFoto_selfie_doc().getValue() != null){
            foto_id.setImageBitmap(profissionalViewModel.getFoto_selfie_doc().getValue());
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_3 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_3);
        btn_avancar_cadastro_step_5 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_5);
        foto_id = (ImageView) view.findViewById(R.id.foto_id_cadastro);
        tirar_foto = (Button) view.findViewById(R.id.btn_tirar_foto);
    }
}