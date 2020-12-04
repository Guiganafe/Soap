package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;

public class EditarPerfil extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static User user;

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
    }

    private void carregarInfoUsuarios() {
        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user = documentSnapshot.toObject(User.class);
                    carregarDadosUsuario();
                }
            }
        });
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
    }
}