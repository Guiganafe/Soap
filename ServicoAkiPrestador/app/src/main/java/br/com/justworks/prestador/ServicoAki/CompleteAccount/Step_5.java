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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;

public class Step_5 extends Fragment {

    private Button btn_voltar_cadastro_step_4,  btn_avancar_cadastro_step_5_2;
    private ProfissionalViewModel profissionalViewModel;
    private Button tirar_foto_frente, tirar_foto_verso;
    private ImageView foto_frente, foto_verso;

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
        return inflater.inflate(R.layout.fragment_step_5, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarComponentes(view);

        onClick();
        loadController();
    }

    private void onClick() {
        btn_voltar_cadastro_step_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_avancar_cadastro_step_5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            if(validarCampos()){
//                enviarDados();
                Navigation.findNavController(v).navigate(R.id.action_step_5_to_step_6);
           // }
            }
        });

        tirar_foto_frente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 2);
                }
            }
        });

        tirar_foto_verso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 3);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Salva a imagem no bitmap
            profissionalViewModel.setFoto_doc_frente(imageBitmap);
            foto_frente.setImageBitmap(imageBitmap);
        }

        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Salva a imagem no bitmap
            profissionalViewModel.setFoto_doc_verso(imageBitmap);
            foto_verso.setImageBitmap(imageBitmap);
        }
    }

    private void enviarDados() {
        StorageReference backIdImageRef = storageRef.child("users/" + userID + "_backIdImage.jpg");
        StorageReference frontIdImageRef = storageRef.child("users/" + userID + "_frontIdImage.jpg");

        Bitmap fotoFrenteDocBitmap = profissionalViewModel.getFoto_doc_frente().getValue();
        Bitmap fotoVersoDocBitmap = profissionalViewModel.getFoto_doc_verso().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

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
    }

    private boolean validarCampos() {
        if(profissionalViewModel.getFoto_doc_frente().getValue() == null){
            Toast.makeText(requireActivity(), "A foto da frente do documento é obrigatória!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(profissionalViewModel.getFoto_doc_verso().getValue() == null){
            Toast.makeText(requireActivity(), "A foto do verso do documento é obrigatória!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadController() {
        if(profissionalViewModel.getFoto_doc_frente().getValue() != null){
            foto_frente.setImageBitmap(profissionalViewModel.getFoto_doc_frente().getValue());
        }

        if(profissionalViewModel.getFoto_doc_verso().getValue() != null){
            foto_verso.setImageBitmap(profissionalViewModel.getFoto_doc_verso().getValue());
        }
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_4 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_4);
        btn_avancar_cadastro_step_5_2 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_5_2);
        foto_frente = (ImageView) view.findViewById(R.id.foto_frente_Cadastro);
        tirar_foto_frente = (Button) view.findViewById(R.id.btn_tirar_foto_frente);
        foto_verso = (ImageView) view.findViewById(R.id.foto_verso_Cadastro);
        tirar_foto_verso = (Button) view.findViewById(R.id.btn_tirar_foto_verso);
    }
}