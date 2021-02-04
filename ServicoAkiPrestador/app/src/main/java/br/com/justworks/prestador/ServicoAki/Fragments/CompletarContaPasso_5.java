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

public class CompletarContaPasso_5 extends Fragment {

    private Button btn_voltar_cadastro_step_4,  btn_avancar_cadastro_step_5_2;
    private ProfissionalViewModel profissionalViewModel;
    private Button tirar_foto_frente, tirar_foto_verso;
    private ImageView foto_frente, foto_verso;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private StorageReference storageRef;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE_FRENTE = 102;
    public static final int CAMERA_REQUEST_CODE_VERSO = 103;
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
                if(validarCampos()){
                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_5_to_step_6);
                }
            }
        });

        tirar_foto_frente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPermissao(CAMERA_REQUEST_CODE_FRENTE);
            }
        });

        tirar_foto_verso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPermissao(CAMERA_REQUEST_CODE_VERSO);
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

    private void dispatchTakePictureIntentFrente() {
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
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE_FRENTE);
            }
        }
    }

    private void dispatchTakePictureIntentVerso() {
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
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE_VERSO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE_FRENTE) {
                File fileCam = new File(currentPhotoPath);
                foto_frente.setImageURI(Uri.fromFile(fileCam));

                Bitmap fotoCamera = BitmapFactory.decodeFile(currentPhotoPath);
                profissionalViewModel.setFoto_doc_frente(fotoCamera);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileCam);
                mediaScanIntent.setData(contentUri);
                requireActivity().sendBroadcast(mediaScanIntent);
                currentPhotoPath = "";
            }

            if (requestCode == CAMERA_REQUEST_CODE_VERSO) {
                File fileCam = new File(currentPhotoPath);
                foto_verso.setImageURI(Uri.fromFile(fileCam));

                Bitmap fotoCamera = BitmapFactory.decodeFile(currentPhotoPath);
                profissionalViewModel.setFoto_doc_verso(fotoCamera);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileCam);
                mediaScanIntent.setData(contentUri);
                requireActivity().sendBroadcast(mediaScanIntent);
                currentPhotoPath = "";
            }
        }
    }

    private void cameraPermissao(int REQUEST_CODE) {
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            if(REQUEST_CODE == 102) {
                dispatchTakePictureIntentFrente();
            } else if(REQUEST_CODE == 103) {
                dispatchTakePictureIntentVerso();
            }
        }
    }

    private Task<String> enviarDados() {
        final StorageReference backIdImageRef = storageRef.child("users/" + userID + "_backIdImage.jpg");
        final StorageReference frontIdImageRef = storageRef.child("users/" + userID + "_frontIdImage.jpg");

        Bitmap fotoFrenteDocBitmap = profissionalViewModel.getFoto_doc_frente().getValue();
        Bitmap fotoVersoDocBitmap = profissionalViewModel.getFoto_doc_verso().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        fotoFrenteDocBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoFrenteDoc = baos.toByteArray();

        UploadTask uploadTask3 = frontIdImageRef.putBytes(dataFotoFrenteDoc);
        Task<Uri> urlTask = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return frontIdImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    profissionalViewModel.setFoto_doc_frente_url(downloadUri.toString());
                }
            }
        });

        fotoVersoDocBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataFotoVersoDoc = baos.toByteArray();

        UploadTask uploadTask4 = backIdImageRef.putBytes(dataFotoVersoDoc);
        Task<Uri> urlTask2 = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return backIdImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    profissionalViewModel.setFoto_doc_verso_url(downloadUri.toString());
                }
            }
        });
        return null;
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
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_step_5);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(75);
    }
}