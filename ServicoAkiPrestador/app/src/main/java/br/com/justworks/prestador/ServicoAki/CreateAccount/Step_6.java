package br.com.justworks.prestador.ServicoAki.CreateAccount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.Address;
import br.com.justworks.prestador.ServicoAki.Model.CivilState;
import br.com.justworks.prestador.ServicoAki.Model.Sex;
import br.com.justworks.prestador.ServicoAki.ViewModel.EndereçoViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.EstadoCivilViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Enum.userEnum;
import br.com.justworks.prestador.ServicoAki.ViewModel.SexoViewModel;

public class Step_6 extends Fragment {

    private static final String TAG = "Step_6";

    private Button btn_voltar_cadastro_step_5,  btn_finalizar_cadastro, btn_tirar_foto_comprovante;
    private ProfissionalViewModel profissionalViewModel;
    private SexoViewModel sexoViewModel;
    private EndereçoViewModel endereçoViewModel;
    private EstadoCivilViewModel estadoCivilViewModel;
    private ImageView foto_comprovante_cadastro;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private StorageReference storageRef;

    StorageReference profileImageRef = storageRef.child("users/" + userID + "_profileImage.jpg");
    StorageReference selfieImageRef = storageRef.child("users/" + userID + "_selfieImage.jpg");
    StorageReference backIdImageRef = storageRef.child("users/" + userID + "_backIdImage.jpg");
    StorageReference frontIdImageRef = storageRef.child("users/" + userID + "_frontIdImage.jpg");
    StorageReference governmentDocRef = storageRef.child("users/" + userID + "_governmentDoc.jpg");
    StorageReference profAddressRef = storageRef.child("users/" + userID + "_proofOfAddressImage.jpg");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
        endereçoViewModel = new ViewModelProvider(requireActivity()).get(EndereçoViewModel.class);
        sexoViewModel = new ViewModelProvider(requireActivity()).get(SexoViewModel.class);
        estadoCivilViewModel = new ViewModelProvider(requireActivity()).get(EstadoCivilViewModel.class);
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseService.getFirebaseAuth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_6, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarComponentes(view);

        onClick();
        loadController();
    }

    private void onClick() {
        btn_voltar_cadastro_step_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_finalizar_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    finalizarCadastro();
                }
            }
        });

        btn_tirar_foto_comprovante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 5);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Salva a imagem no bitmap
            profissionalViewModel.setFoto_comprovante_res(imageBitmap);
            foto_comprovante_cadastro.setImageBitmap(imageBitmap);
            btn_tirar_foto_comprovante.setText("Tirar outra");
        }
    }

    private boolean validarCampos() {
        if(profissionalViewModel.getFoto_comprovante_res().getValue() == null){
            Toast.makeText(requireActivity(), "A foto do comprovante de residência é obrigatória!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadController() {
        if(profissionalViewModel.getFoto_comprovante_res().getValue() != null){
            foto_comprovante_cadastro.setImageBitmap(profissionalViewModel.getFoto_comprovante_res().getValue());
        }
    }

    private void finalizarCadastro() {

        Bitmap fotoPerfilBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();
        Bitmap fotoSelfieBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();
        Bitmap fotoFrenteDocBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();
        Bitmap fotoVersoDocBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();
        Bitmap fotoComprovanteBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();

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

        fotoSelfieBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoSelfie = baos.toByteArray();

        UploadTask uploadTask2 = selfieImageRef.putBytes(dataFotoSelfie);
        uploadTask2.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                profissionalViewModel.setFoto_selfie_url(downloadUrl.toString());
            }
        });

        fotoFrenteDocBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoFrenteDoc = baos.toByteArray();

        UploadTask uploadTask3 = frontIdImageRef.putBytes(dataFotoFrenteDoc);
        uploadTask3.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                profissionalViewModel.setFoto_doc_frente_url(downloadUrl.toString());
            }
        });

        fotoVersoDocBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoVersoDoc = baos.toByteArray();

        UploadTask uploadTask4 = backIdImageRef.putBytes(dataFotoVersoDoc);
        uploadTask4.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                profissionalViewModel.setFoto_doc_verso_url(downloadUrl.toString());
            }
        });

        fotoComprovanteBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoComprovante = baos.toByteArray();

        UploadTask uploadTask5 = backIdImageRef.putBytes(dataFotoComprovante);
        uploadTask5.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                profissionalViewModel.setFoto_comprovante_res_url(downloadUrl.toString());
            }
        });

        String sexoPt, sexoEn;
        sexoPt = sexoViewModel.getSexoPtbr().getValue();
        sexoEn = sexoViewModel.getSexoEn().getValue();

        Sex sexo = new Sex(sexoPt, sexoEn);

        String estadoCivilPt, estadoCivilEn;

        estadoCivilPt = estadoCivilViewModel.getEstadoCivilPtBr().getValue();
        estadoCivilEn = estadoCivilViewModel.getEstadoCivilEn().getValue();

        CivilState civilState = new CivilState(estadoCivilPt, estadoCivilEn);

        String email = profissionalViewModel.getEmail().getValue();
        String senha = profissionalViewModel.getSenha().getValue();

        Boolean ativo;
        String cidade, pais, bairro, numero, estado, rua, cep;
        double latitude, longitude;

        ativo = endereçoViewModel.getActive().getValue();
        cidade = endereçoViewModel.getCidade().getValue();
        pais = endereçoViewModel.getPais().getValue();
        bairro = endereçoViewModel.getBairro().getValue();
        numero = endereçoViewModel.getNumero().getValue();
        estado = endereçoViewModel.getEstado().getValue();
        rua = endereçoViewModel.getRua().getValue();
        cep = endereçoViewModel.getCep().getValue();
        longitude = endereçoViewModel.getLongitude().getValue();
        latitude = endereçoViewModel.getLatitude().getValue();

        final Address address = new Address(ativo, cidade, pais, bairro, numero, estado, rua, cep, longitude, latitude);

        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    DocumentReference documentReference = db.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put(userEnum.USER_ADDRESS.getDisplayName(), address);
                    user.put(userEnum.USER_BIRTHDATE.getDisplayName(), profissionalViewModel.get);
                    user.put(Constants.KEY_PROFESSIONAL.getDisplayName(), true);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: usuário salvo no firestore: " + userID);
                        }
                    });

//                    Intent CompletarPerfilIntent = new Intent(CadastroActivity.this, MainActivity.class);
//                    startActivity(CompletarPerfilIntent);
//                    finish();
                }else{
                    Toast.makeText(requireActivity(), "Falha no cadastro", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_5 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_5);
        btn_finalizar_cadastro = (Button) view.findViewById(R.id.btn_finalizar_cadastro);
        foto_comprovante_cadastro = (ImageView) view.findViewById(R.id.foto_comprovante_cadastro);
        btn_tirar_foto_comprovante = (Button) view.findViewById(R.id.btn_tirar_foto_comprovante);
    }
}