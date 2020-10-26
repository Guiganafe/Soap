package br.com.justworks.prestador.ServicoAki.CreateAccount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.justworks.prestador.ServicoAki.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class Step_1 extends Fragment {

    private Button btn_voltar_cadastro_step_0,  btn_avancar_cadastro_step_2, adicionar_foto, remover_foto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView foto_perfil;
    private EditText nome_cadastro, email_cadastro, telefone_cadastro, senha, confirmar_senha;
    ProfissionalViewModel profissionalViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarComponentes(view);
        maskController();
        onClick();
    }

    private void maskController() {
        telefone_cadastro.addTextChangedListener(MaskEditUtil.mask(telefone_cadastro, MaskEditUtil.FORMAT_TELL));
    }

    private void onClick() {
        btn_voltar_cadastro_step_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_avancar_cadastro_step_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_step_1_to_step_2);
            }
        });

        adicionar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        remover_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto_perfil.setImageBitmap(null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto_perfil.setImageBitmap(imageBitmap);
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_0 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_0);
        btn_avancar_cadastro_step_2 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_2);
        adicionar_foto = (Button) view.findViewById(R.id.btn_adicionar_foto);
        remover_foto = (Button) view.findViewById(R.id.btn_remover_foto);
        foto_perfil = (ImageView) view.findViewById(R.id.img_perfil);
        nome_cadastro = (EditText) view.findViewById(R.id.edt_nome_completo_cadastro);
        email_cadastro = (EditText) view.findViewById(R.id.edt_email_cadastro);
        telefone_cadastro = (EditText) view.findViewById(R.id.edt_telefone_cadastro);
        senha = (EditText) view.findViewById(R.id.edt_senha);
        confirmar_senha = (EditText) view.findViewById(R.id.edt_confirmar_senha_cadastro);
    }
}