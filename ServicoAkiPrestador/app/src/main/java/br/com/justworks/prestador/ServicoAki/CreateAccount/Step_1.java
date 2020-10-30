package br.com.justworks.prestador.ServicoAki.CreateAccount;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.justworks.prestador.ServicoAki.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;

public class Step_1 extends Fragment {

    private Button btn_voltar_cadastro_step_0,  btn_avancar_cadastro_step_2, adicionar_foto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView foto_perfil, remover_foto;
    private EditText nome_cadastro, email_cadastro, telefone_cadastro, senha, confirmar_senha;
    ProfissionalViewModel profissionalViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
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
        onClickController();
        textWatcherController();
        loadController();
    }

    private void textWatcherController() {
        nome_cadastro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setNome_completo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email_cadastro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        telefone_cadastro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setTelefone(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        senha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setSenha(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmar_senha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setConfirmarSenha(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void maskController() {
        telefone_cadastro.addTextChangedListener(MaskEditUtil.mask(telefone_cadastro, MaskEditUtil.FORMAT_TELL));
    }

    private void onClickController() {
        btn_voltar_cadastro_step_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_avancar_cadastro_step_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(validarCampos()) {
                    Navigation.findNavController(v).navigate(R.id.action_step_1_to_step_2);
                //}
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
                profissionalViewModel.setFoto_perfil(null);
                foto_perfil.setPadding(85,85,85,85);
                foto_perfil.setImageResource(R.drawable.ic_person);
                foto_perfil.setBackgroundResource(R.drawable.edit_text_gray);
            }
        });
    }

    private boolean validarCampos() {
        if(TextUtils.isEmpty(nome_cadastro.getText().toString())){
            nome_cadastro.setError("Insira um nome válido!");
            return false;
        } else if(TextUtils.isEmpty(email_cadastro.getText().toString())){
            email_cadastro.setError("Insira um email válido!");
            return false;
        } else if(TextUtils.isEmpty(telefone_cadastro.getText().toString())){
            telefone_cadastro.setError("Insira um telefone válido!");
            return false;
        } else if(TextUtils.isEmpty(senha.getText().toString())){
            senha.setError("Insira uma senha válida");
            return false;
        } else if (!TextUtils.equals(senha.getText().toString(), confirmar_senha.getText().toString())){
            confirmar_senha.setError("As senhas não batem!");
            return false;
        } else if(profissionalViewModel.getFoto_perfil().getValue() == null){
            Toast.makeText(requireActivity(), "Insira uma foto de perfil!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadController() {
        if(profissionalViewModel.getFoto_perfil().getValue() != null){
            foto_perfil.setImageBitmap(profissionalViewModel.getFoto_perfil().getValue());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Salva a imagem no bitmap
            profissionalViewModel.setFoto_perfil(imageBitmap);
            foto_perfil.setImageBitmap(imageBitmap);
            foto_perfil.setPadding(0,0,0,0);
            foto_perfil.setBackground(null);
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_0 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_0);
        btn_avancar_cadastro_step_2 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_2);
        adicionar_foto = (Button) view.findViewById(R.id.btn_adicionar_foto);
        remover_foto = (ImageView) view.findViewById(R.id.btn_remover_foto);
        foto_perfil = (ImageView) view.findViewById(R.id.img_perfil);
        nome_cadastro = (EditText) view.findViewById(R.id.edt_nome_completo_cadastro);
        email_cadastro = (EditText) view.findViewById(R.id.edt_email_cadastro);
        telefone_cadastro = (EditText) view.findViewById(R.id.edt_telefone_cadastro);
        senha = (EditText) view.findViewById(R.id.edt_senha_cadastro);
        confirmar_senha = (EditText) view.findViewById(R.id.edt_confirmar_senha_cadastro);
    }
}