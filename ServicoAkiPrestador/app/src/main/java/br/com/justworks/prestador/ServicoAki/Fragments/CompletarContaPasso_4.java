package br.com.justworks.prestador.ServicoAki.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;


public class CompletarContaPasso_4 extends Fragment {

    private Button btn_voltar_cadastro_step_3,  btn_avancar_cadastro_step_5;
    private ProfissionalViewModel profissionalViewModel;
    private ImageView foto_id;
    private Button tirar_foto;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private StorageReference storageRef;
    private ProgressBar progressBar;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private String currentPhotoPath;

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
                if(validarCampos()){
                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_4_to_step_5_1);
                }
            }
        });

        tirar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cameraPermissao();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                        "br.com.justworks.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                File fileCam = new File(currentPhotoPath);
                foto_id.setImageURI(Uri.fromFile(fileCam));
                tirar_foto.setText("Tirar outra");

                Bitmap fotoCamera = BitmapFactory.decodeFile(currentPhotoPath);
                profissionalViewModel.setFoto_selfie_doc(fotoCamera);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileCam);
                mediaScanIntent.setData(contentUri);
                requireActivity().sendBroadcast(mediaScanIntent);

            }
        }
    }

    private void cameraPermissao() {
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(requireActivity(), "A permiss??o ?? necess??ria", Toast.LENGTH_SHORT).show();
        }
    }

    private Task<String> enviarDados() {
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
        return null;
    }

    private boolean validarCampos() {
        if(profissionalViewModel.getFoto_selfie_doc().getValue() == null){
            Toast.makeText(requireActivity(), "A selfie com documento ?? obrigat??ria!", Toast.LENGTH_SHORT).show();
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
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_step_4);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(60);
    }
}