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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;


public class Step_4 extends Fragment {

    private Button btn_voltar_cadastro_step_3,  btn_avancar_cadastro_step_5;
    private ProfissionalViewModel profissionalViewModel;
    private ImageView foto_id;
    private Button tirar_foto;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private StorageReference storageRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
        userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();
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
        //btn_avancar_cadastro_step_5.setEnabled(false);
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
//                if(validarCampos()){
//                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_4_to_step_5_1);
              //  }
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
            //btn_avancar_cadastro_step_5.setEnabled(true);
        }
    }

    private void enviarDados() {
        final StorageReference selfieImageRef = storageRef.child("users/" + userID + "_selfieImage.jpg");
        Bitmap fotoSelfieBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fotoSelfieBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoSelfie = baos.toByteArray();

        UploadTask uploadTask = selfieImageRef.putBytes(dataFotoSelfie);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return selfieImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    profissionalViewModel.setFoto_selfie_url(downloadUri.toString());
                }
            }
        });
    }

    private boolean validarCampos() {
        if(profissionalViewModel.getFoto_selfie_doc().getValue() == null){
            Toast.makeText(requireActivity(), "A selfie com documento é obrigatória!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
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