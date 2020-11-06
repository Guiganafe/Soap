package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.R;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";
    private static final String KEY_EMAIL = "email";
    private EditText edt_email, edt_senha;
    private Button btn_cadastrar;
    private ProgressBar progressBar;

    private String userID;

    /**
     * Instancia do firebase
     */
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseService.getFirebaseAuth();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarVariaveis();

        onClick();

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void onClick() {
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario() {
        final String email, senha;

        email = edt_email.getText().toString();
        senha = edt_senha.getText().toString();

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(senha)) {
            edt_email.setError("Insira um e-mail");
            edt_senha.setError("Insira uma senha");
            return;
        }else if(TextUtils.isEmpty(email)){
            edt_email.setError("Insira um e-mail");
            return;
        }else if(TextUtils.isEmpty(senha)){
            edt_senha.setError("Insira uma senha");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    final FirebaseUser userEmailAndPassword = firebaseAuth.getCurrentUser();

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();

                    userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put(KEY_EMAIL, email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: usuário salvo no firestore: " + userID);
                        }
                    });

                    updateUI(userEmailAndPassword);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(CadastroActivity.this, "Falha no cadastro", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Toast.makeText(CadastroActivity.this, "Login realizado com sucesso", Toast.LENGTH_LONG).show();
            Intent completarCadastroIntent = new Intent(CadastroActivity.this, CompletarCadastroActivity.class);
            startActivity(completarCadastroIntent);
            finish();
        }
    }

    private void inicializarVariaveis() {
        edt_email = (EditText) findViewById(R.id.edt_email_criar_conta);
        edt_senha = (EditText) findViewById(R.id.edt_senha_criar_conta);
        btn_cadastrar = (Button) findViewById(R.id.btn_continuar_criar_conta);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}