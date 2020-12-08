package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.CivilState;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;

public class EditarPerfil extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
    private static User user = new User();

    private ImageView fotoPerfil, removerImage;
    private Button alterarImagem, cancelarEdicao, salvarEdicao;
    private EditText nome, email, telefone, cpf, nomeMae, rg, orgaoEmissor;
    private Spinner sexo, estadoCivil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        inicializarComponentes();

        carregarInfoUsuarios();

        onClicklistenner();

        textWatcherController();
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

        orgaoEmissor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setDispatchingAgency(s.toString());
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
        Glide.with(this).load(user.getImageUrl()).into(fotoPerfil);
        cpf.setText(user.getGovernmentId());
        nomeMae.setText(user.getMotherName());
        rg.setText(user.getIdentifyDocument());
        orgaoEmissor.setText(user.getDispatchingAgency());
    }

    public void inicializarComponentes(){
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
        orgaoEmissor = (EditText) findViewById(R.id.edt_orgao_emissor_edicao);
        sexo = (Spinner) findViewById(R.id.spinner_sexo_edicao);
        estadoCivil = (Spinner) findViewById(R.id.spinner_estado_civil_edicao);
    }
}