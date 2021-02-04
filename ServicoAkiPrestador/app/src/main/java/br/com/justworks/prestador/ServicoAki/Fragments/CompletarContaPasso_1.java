package br.com.justworks.prestador.ServicoAki.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
import br.com.justworks.prestador.ServicoAki.ViewModel.EstadoCivilViewModel;
import br.com.justworks.prestador.ServicoAki.ViewModel.ProfissionalViewModel;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Util.userEnum;
import br.com.justworks.prestador.ServicoAki.Util.MaskEditUtil;
import br.com.justworks.prestador.ServicoAki.ViewModel.SexoViewModel;

public class CompletarContaPasso_1 extends Fragment {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private Button btn_voltar_cadastro_step_0,  btn_avancar_cadastro_step_2, adicionar_foto;
    private ImageView foto_perfil, remover_foto;
    private Spinner sexo_spinner, estado_civil_spinner;
    private EditText nome_cadastro, email_cadastro, telefone_cadastro, data_nascimento;
    private ProgressBar progressBar;
    ProfissionalViewModel profissionalViewModel;
    private SexoViewModel sexoViewModel;
    private EstadoCivilViewModel estadoCivilViewModel;
    private String currentPhotoPath;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        data_nascimento.addTextChangedListener(MaskEditUtil.mask(data_nascimento, MaskEditUtil.FORMAT_DATE));
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
                if(validarCampos()) {
                    enviarDados();
                    Navigation.findNavController(v).navigate(R.id.action_step_1_to_step_2);
               }
            }
        });

        adicionar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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

    private Task<String> enviarDados() {
        final StorageReference profileImageRef = storageRef.child("users/" + userID + "_profileImage.jpg");
        Bitmap bitmap = profissionalViewModel.getFoto_perfil().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileImageRef.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return profileImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    profissionalViewModel.setFoto_perfil_url(downloadUri.toString());
                }
            }
        });
        return null;
    }

    private boolean validarCampos() {
        String nome = nome_cadastro.getText().toString();
        String telefone = telefone_cadastro.getText().toString();
        String dataDeNascimento = data_nascimento.getText().toString();

        if(TextUtils.isEmpty(nome)){
            nome_cadastro.setError("O nome é obrigatório");
            return false;
        } else if(nome_cadastro.getTextSize() < 3){
            nome_cadastro.setError("Insira um nome válido");
            return false;
        } else if(TextUtils.isEmpty(telefone)){
            telefone_cadastro.setError("O telefone é obrigatório");
            return false;
        } else if(!telefone.matches("\\(\\d{2}\\)(\\d{4,5}\\-\\d{4})")){
            telefone_cadastro.setError("Insira um telefone válido");
            return false;
        } else if(TextUtils.isEmpty(dataDeNascimento)){
            data_nascimento.setError("A data de nascimento é obrigatória");
            return false;
        } else if(!dataDeNascimento.matches("^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$")){
            data_nascimento.setError("Insira uma data de nascimento válida");
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

                if(position == 1){
                    sexoViewModel.setSexoPtbR("M");
                } else if(position == 2){
                    sexoViewModel.setSexoPtbR("F");
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
                    estadoCivilViewModel.setEstadoCivilEn("Widowed");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void selectImage() {
        final CharSequence[] options = { "Tirar foto", "Selecionar da galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Adicionar foto");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Tirar foto"))
                {
                    cameraPermissao();
                }
                else if (options[item].equals("Selecionar da galeria"))
                {
                    Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galeria, GALLERY_REQUEST_CODE);
                }
                else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                File fileCam = new File(currentPhotoPath);
                foto_perfil.setPadding(0,0,0,0);
                foto_perfil.setBackground(null);
                foto_perfil.setImageURI(Uri.fromFile(fileCam));

                Bitmap fotoCamera = BitmapFactory.decodeFile(currentPhotoPath);
                profissionalViewModel.setFoto_perfil(fotoCamera);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileCam);
                mediaScanIntent.setData(contentUri);
                requireActivity().sendBroadcast(mediaScanIntent);

            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri contentUri = data.getData();
                foto_perfil.setPadding(0,0,0,0);
                foto_perfil.setBackground(null);
                foto_perfil.setImageURI(contentUri);
                Bitmap fotoGaleria = null;
                try {
                    fotoGaleria = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profissionalViewModel.setFoto_perfil(fotoGaleria);
            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }


    @Override
    public void onStart() {
        super.onStart();
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
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_step_1);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(15);
        email_cadastro.setFocusable(false);
    }
}