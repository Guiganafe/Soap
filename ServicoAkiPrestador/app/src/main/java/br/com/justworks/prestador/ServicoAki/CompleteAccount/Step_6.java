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

public class Step_6 extends Fragment {

    private static final String TAG = "Step_6";

    private Button btn_voltar_cadastro_step_5,  btn_avancar_cadastro_step_7, btn_tirar_foto_comprovante;
    private ProfissionalViewModel profissionalViewModel;
    private ImageView foto_comprovante_cadastro;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();

    private StorageReference storageRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profissionalViewModel = new ViewModelProvider(requireActivity()).get(ProfissionalViewModel.class);
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

        btn_avancar_cadastro_step_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            if(validarCampos()){
//                enviarDados();
                Navigation.findNavController(v).navigate(R.id.action_step_6_to_step_7);
           // }
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

    private void enviarDados() {
        final StorageReference profAddressRef = storageRef.child("users/" + userID + "_proofOfAddressImage.jpg");

        Bitmap fotoComprovanteBitmap = profissionalViewModel.getFoto_selfie_doc().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        fotoComprovanteBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoComprovante = baos.toByteArray();

        UploadTask uploadTask5 = profAddressRef.putBytes(dataFotoComprovante);
        Task<Uri> urlTask2 = uploadTask5.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return profAddressRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    profissionalViewModel.setFoto_comprovante_res_url(downloadUri.toString());
                }
            }
        });
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_5 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_5);
        btn_avancar_cadastro_step_7 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_7);
        foto_comprovante_cadastro = (ImageView) view.findViewById(R.id.foto_comprovante_cadastro);
        btn_tirar_foto_comprovante = (Button) view.findViewById(R.id.btn_tirar_foto_comprovante);
    }
}