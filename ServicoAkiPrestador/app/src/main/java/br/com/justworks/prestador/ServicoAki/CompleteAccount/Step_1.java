package br.com.justworks.prestador.ServicoAki.CompleteAccount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.ViewModel.EstadoCivilViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Enum.userEnum;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;
import br.com.justworks.prestador.ServicoAki.ViewModel.SexoViewModel;

public class Step_1 extends Fragment {

    private Button btn_voltar_cadastro_step_0,  btn_avancar_cadastro_step_2, adicionar_foto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView foto_perfil, remover_foto;
    private Spinner sexo_spinner, estado_civil_spinner;
    private EditText nome_cadastro, email_cadastro, telefone_cadastro, data_nascimento;
    ProfissionalViewModel profissionalViewModel;
    private SexoViewModel sexoViewModel;
    private EstadoCivilViewModel estadoCivilViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private StorageReference storageRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
        sexoViewModel = new ViewModelProvider(requireActivity()).get(SexoViewModel.class);
        estadoCivilViewModel = new ViewModelProvider(requireActivity()).get(EstadoCivilViewModel.class);
        userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();
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
        loadUserInfo();
        maskController();
        onClickController();
        textWatcherController();
        loadController();
        spinnerController();
    }

    private void loadUserInfo() {
        db.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String email = documentSnapshot.getString(userEnum.USER_EMAIL.getDisplayName());
                    email_cadastro.setText(email);
                }else{

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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

        data_nascimento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profissionalViewModel.setData_nascimento(s.toString());
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
//                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_1_to_step_2);
              //  }
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

    private void enviarDados() {
        StorageReference profileImageRef = storageRef.child("users/" + userID + "_profileImage.jpg");
        Bitmap fotoPerfilBitmap = profissionalViewModel.getFoto_perfil().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fotoPerfilBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoPerfil = baos.toByteArray();

        UploadTask uploadTask = profileImageRef.putBytes(dataFotoPerfil);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                profissionalViewModel.setFoto_perfil_url(downloadUrl.toString());
            }
        });
    }

    private boolean validarCampos() {
        if(TextUtils.isEmpty(nome_cadastro.getText().toString())){
            nome_cadastro.setError("Insira um nome válido!");
            return false;
        } else if(TextUtils.isEmpty(telefone_cadastro.getText().toString())){
            telefone_cadastro.setError("Insira um telefone válido!");
            return false;
        } else if(profissionalViewModel.getFoto_perfil().getValue() == null){
            Toast.makeText(requireActivity(), "Insira uma foto de perfil!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.equals(sexo_spinner.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione o seu sexo", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.equals(estado_civil_spinner.getSelectedItem().toString(), "Selecione")){
            Toast.makeText(requireActivity(), "Selecione o seu estado cívil", Toast.LENGTH_SHORT).show();
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

    private void spinnerController() {
        ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.sexo_array, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo_spinner.setAdapter(sexoAdapter);

        sexo_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sexo = sexo_spinner.getSelectedItem().toString();
                sexoViewModel.setSexoPtbR(sexo);

                if(position == 1){
                    sexoViewModel.setSexoEn("Male");
                } else if(position == 2){
                    sexoViewModel.setSexoEn("Female");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> estadoCivilAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.estado_civil_array, android.R.layout.simple_spinner_item);
        estadoCivilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estado_civil_spinner.setAdapter(estadoCivilAdapter);

        estado_civil_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String estado_civil = estado_civil_spinner.getSelectedItem().toString();
                estadoCivilViewModel.setEstadoCivilPtBr(estado_civil);
                if(position == 1){
                    estadoCivilViewModel.setEstadoCivilEn("Married");
                } else if(position == 2){
                    estadoCivilViewModel.setEstadoCivilEn("Single");
                } else if(position == 3){
                    estadoCivilViewModel.setEstadoCivilEn("Divorced");
                } else if(position == 4){
                    estadoCivilViewModel.setEstadoCivilEn("Widower");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseService.getFirebaseAuth();
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
        sexo_spinner = (Spinner) view.findViewById(R.id.spinner_sexo_cadastro);
        estado_civil_spinner = (Spinner) view.findViewById(R.id.spinner_estado_civil_cadastro);
        data_nascimento = (EditText) view.findViewById(R.id.edt_data_nasc_cadastro);

        email_cadastro.setFocusable(false);
    }
}