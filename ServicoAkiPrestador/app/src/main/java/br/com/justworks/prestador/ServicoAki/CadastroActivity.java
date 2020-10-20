package br.com.justworks.prestador.ServicoAki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;

public class CadastroActivity extends AppCompatActivity {

    private TextInputLayout edt_email_cadastro, edt_senha_cadastro;
    private Button btn_cadastrar;
    private ProgressBar progressBar;
    private static final String TAG = "CadastroActivity";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_AUTENTICATED = "isAuthenticated";
    private static final String KEY_PROFESSIONAL = "isProfessional";
    private String userID;
    /**
     * Instancia do firebase
     */
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        /**
         * Conexão com os componentes de layout
         */
        InicializarComponentes();

        /**
         * Gerencia todos os cliques da tela
         */
        onClick();

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseService.getFirebaseAuth();
    }

    private void onClick() {
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarUsuario();
            }
        });
    }

    private void CadastrarUsuario() {
        final String email, senha;

        email = edt_email_cadastro.getEditText().getText().toString();
        senha = edt_senha_cadastro.getEditText().getText().toString();

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(senha)) {
            edt_email_cadastro.setError("Insira um e-mail");
            edt_senha_cadastro.setError("Insira uma senha");
            return;
        }else if(TextUtils.isEmpty(email)){
            edt_email_cadastro.setError("Insira um e-mail");
            return;
        }else if(TextUtils.isEmpty(senha)){
            edt_senha_cadastro.setError("Insira uma senha");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //firebaseAuth.getCurrentUser().getUid() != null
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();

                    userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put(KEY_EMAIL, email);
                    user.put(KEY_AUTENTICATED, false);
                    user.put(KEY_PROFESSIONAL, true);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: usuário salvo no firestore: " + userID);
                        }
                    });

                    Intent CompletarPerfilIntent = new Intent(CadastroActivity.this, MainActivity.class);
                    startActivity(CompletarPerfilIntent);
                    finish();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(CadastroActivity.this, "Falha no cadastro", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void InicializarComponentes(){
        edt_email_cadastro = (TextInputLayout) findViewById(R.id.edt_email_cadastro);
        edt_senha_cadastro = (TextInputLayout) findViewById(R.id.edt_senha_cadastro);
        btn_cadastrar = (Button) findViewById(R.id.btn_cadastrar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}