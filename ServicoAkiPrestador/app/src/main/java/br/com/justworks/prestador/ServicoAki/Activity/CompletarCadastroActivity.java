package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.R;
import br.com.justworks.prestador.ServicoAki.Enum.userEnum;

public class CompletarCadastroActivity extends AppCompatActivity {

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private StorageReference mStorageRef;
//    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro);
        Toast.makeText(CompletarCadastroActivity.this, "Complete seu cadastro", Toast.LENGTH_SHORT).show();
//        mStorageRef = FirebaseStorage.getInstance().getReference();
//        userID = FirebaseService.getFirebaseAuth().getCurrentUser().getUid();
//
//        db.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    String USER_PROF_ADDRESS_IMAGE = documentSnapshot.getString(userEnum.USER_PROF_ADDRESS_IMAGE.getDisplayName());
//
//                    if(TextUtils.isEmpty(USER_PROF_ADDRESS_IMAGE)) {
//                        Toast.makeText(CompletarCadastroActivity.this, "Complete seu cadastro", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Intent mainIntent = new Intent(CompletarCadastroActivity.this, MainActivity.class);
//                        startActivity(mainIntent);
//                        finish();
//                    }
//                }
//            }
//        });
    }
}