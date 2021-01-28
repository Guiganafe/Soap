package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CivilState;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;

public class EditarPerfil extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private StorageReference storageRef;
    private static User user = new User();

    private ImageView fotoPerfil, removerImage;
    private Button alterarImagem, cancelarEdicao, salvarEdicao;
    private EditText nome, email, telefone, cpf, nomeMae, rg, dataNascimento;
    private Spinner sexo, estadoCivil;

    //Data de nascimento
    private Calendar calendar;
    private int dia, mes, ano;
    private DatePickerDialog datePickerDialog;

    //Camera
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private String currentPhotoPath;
    private Bitmap fotoCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        inicializarComponentes();

        carregarInfoUsuarios();

        onClicklistenner();

        textWatcherController();

        storageRef = FirebaseStorage.getInstance().getReference();
        
        timeController();

    }

    private void textWatcherController() {
        nome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        telefone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setPhoneNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setGovernmentId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nomeMae.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setMotherName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setIdentifyDocument(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onClicklistenner() {
        cancelarEdicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        salvarEdicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(userID).update(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditarPerfil.this, "Usuário alterado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        removerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoPerfil.setImageBitmap(null);
                user.setImageUrl("");
                fotoPerfil.setPadding(85,85,85,85);
                fotoPerfil.setImageResource(R.drawable.ic_person);
                fotoPerfil.setBackgroundResource(R.drawable.edit_text_gray);
            }
        });

        alterarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = { "Tirar foto", "Selecionar da galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
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
            Toast.makeText(this, "A permissão é necessária", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                File fileCam = new File(currentPhotoPath);
                fotoPerfil.setPadding(0,0,0,0);
                fotoPerfil.setBackground(null);
                fotoPerfil.setImageURI(Uri.fromFile(fileCam));

                fotoCamera = BitmapFactory.decodeFile(currentPhotoPath);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileCam);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                enviarDados();

            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri contentUri = data.getData();
                fotoPerfil.setPadding(0,0,0,0);
                fotoPerfil.setBackground(null);
                fotoPerfil.setImageURI(contentUri);
                Bitmap fotoGaleria = null;
                try {
                    fotoGaleria = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fotoCamera = fotoGaleria;
                enviarDados();
            }
        }
    }

    private Task<String> enviarDados() {
        final StorageReference profileImageRef = storageRef.child("users/" + userID + "_profileImage.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fotoCamera.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
                    user.setImageUrl(downloadUri.toString());
                }
            }
        });
        return null;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.com.justworks.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    private void spinnerController() {
        ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(this, R.array.sexo_array, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(sexoAdapter);

        if(user.getSex().equals("M")){
            sexo.setSelection(1);
        } else if(user.getSex().equals("F")){
            sexo.setSelection(2);
        }

        sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){
                    user.setSex("M");
                } else if(position == 2){
                    user.setSex("F");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> estadoCivilAdapter = ArrayAdapter.createFromResource(this, R.array.estado_civil_array, android.R.layout.simple_spinner_item);
        estadoCivilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoCivil.setAdapter(estadoCivilAdapter);

        if(user.getCivilState().getPtbr().equals("Casado(a)")){
            estadoCivil.setSelection(1);
        } else if(user.getCivilState().getPtbr().equals("Solteiro(a)")){
            estadoCivil.setSelection(2);
        } else if(user.getCivilState().getPtbr().equals("Separado(a)")){
            estadoCivil.setSelection(3);
        } else if(user.getCivilState().getPtbr().equals("Viúvo(a)")){
            estadoCivil.setSelection(4);
        }

        estadoCivil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String estado_civil = estadoCivil.getSelectedItem().toString();
                CivilState cv = new CivilState();
                cv.setPtbr(estado_civil);

                if(position == 1){
                    cv.setEn("Married");
                } else if(position == 2){
                    cv.setEn("Single");
                } else if(position == 3){
                    cv.setEn("Divorced");
                } else if(position == 4){
                    cv.setEn("Widowed");
                }
                user.setCivilState(cv);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void carregarInfoUsuarios() {
        user = UserBase.getInstance().getUser();
        carregarDadosUsuario();
        spinnerController();
    }

    private void carregarDadosUsuario() {
        nome.setText(user.getName());
        email.setText(user.getEmail());
        telefone.setText(user.getPhoneNumber());
        if(!user.getImageUrl().equals("") || user.getImageUrl() != null){
            Glide.with(this).load(user.getImageUrl()).into(fotoPerfil);
        } else {
            fotoPerfil.setImageBitmap(null);
            fotoPerfil.setPadding(85,85,85,85);
            fotoPerfil.setImageResource(R.drawable.ic_person);
            fotoPerfil.setBackgroundResource(R.drawable.edit_text_gray);
        }
        cpf.setText(user.getGovernmentId());
        nomeMae.setText(user.getMotherName());
        rg.setText(user.getIdentifyDocument());
        dataNascimento.setText(user.getBirthDate());
    }

    private void timeController() {
        dia = Integer.parseInt(user.getBirthDate().substring(0,2));
        mes = Integer.parseInt(user.getBirthDate().substring(3,5)) -1;
        ano = Integer.parseInt(user.getBirthDate().substring(6,10));

        dataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditarPerfil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dataNascimento.setText(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
                        user.setBirthDate(dayOfMonth + "/" + month+1 + "/" + year);
                    }
                },ano, mes, dia);
                datePickerDialog.show();
            }
        });
    }

    public void inicializarComponentes(){
        calendar = Calendar.getInstance();
        fotoPerfil = (ImageView) findViewById(R.id.img_perfil_edicao);
        alterarImagem = (Button) findViewById(R.id.btn_editar_foto);
        removerImage = (ImageView) findViewById(R.id.btn_remover_foto_atual);
        cancelarEdicao = (Button) findViewById(R.id.btn_cancelar_edicao);
        salvarEdicao = (Button) findViewById(R.id.btn_atualizar_edicao);
        nome = (EditText) findViewById(R.id.edt_nome_completo_edicao);
        email = (EditText) findViewById(R.id.edt_email_edicao);
        telefone = (EditText) findViewById(R.id.edt_telefone_edicao);
        cpf = (EditText) findViewById(R.id.edt_cpf_edicao);
        nomeMae = (EditText) findViewById(R.id.edt_nome_mae_edicao);
        rg = (EditText) findViewById(R.id.edt_rg_edicao);
        sexo = (Spinner) findViewById(R.id.spinner_sexo_edicao);
        estadoCivil = (Spinner) findViewById(R.id.spinner_estado_civil_edicao);
        dataNascimento = (EditText) findViewById(R.id.edt_data_nasc_edicao);
    }
}