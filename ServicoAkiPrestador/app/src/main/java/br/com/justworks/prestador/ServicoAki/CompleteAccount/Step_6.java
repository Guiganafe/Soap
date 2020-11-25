package br.com.justworks.prestador.ServicoAki.CompleteAccount;

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

public class Step_6 extends Fragment {

    private static final String TAG = "Step_6";

    private Button btn_voltar_cadastro_step_5,  btn_avancar_cadastro_step_7, btn_tirar_foto_comprovante;
    private ProfissionalViewModel profissionalViewModel;
    private ImageView foto_comprovante_cadastro;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private String currentPhotoPath;

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
                if(validarCampos()){
                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_6_to_step_7);
                }
            }
        });

        btn_tirar_foto_comprovante.setOnClickListener(new View.OnClickListener() {
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
                foto_comprovante_cadastro.setImageURI(Uri.fromFile(fileCam));
                btn_tirar_foto_comprovante.setText("Tirar outra");

                Bitmap fotoCamera = BitmapFactory.decodeFile(currentPhotoPath);
                profissionalViewModel.setFoto_comprovante_res(fotoCamera);

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
            Toast.makeText(requireActivity(), "A permissão é necessária", Toast.LENGTH_SHORT).show();
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

    private Task<String> enviarDados() {
        final StorageReference profAddressRef = storageRef.child("users/" + userID + "_proofOfAddressImage.jpg");

        Bitmap fotoComprovanteBitmap = profissionalViewModel.getFoto_comprovante_res().getValue();

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
        return null;
    }

    private void inicializarComponentes(View view) {
        btn_voltar_cadastro_step_5 = (Button) view.findViewById(R.id.btn_voltar_cadastro_step_5);
        btn_avancar_cadastro_step_7 = (Button) view.findViewById(R.id.btn_avancar_cadastro_step_7);
        foto_comprovante_cadastro = (ImageView) view.findViewById(R.id.foto_comprovante_cadastro);
        btn_tirar_foto_comprovante = (Button) view.findViewById(R.id.btn_tirar_foto_comprovante);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_step_6);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(90);
    }
}