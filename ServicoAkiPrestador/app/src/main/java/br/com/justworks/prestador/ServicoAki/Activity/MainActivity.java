package br.com.justworks.prestador.ServicoAki.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import br.com.justworks.prestador.ServicoAki.Enum.userEnum;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference servicesReference = db.collection("scheduleItems");

    private String name, imageUrl, phoneNumber, email, comprovanteResidencia;
    private Boolean isAuthenticated, isProfessional;
    public static User user = new User();

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponentes();
        inicializarUsuario();
    }

    private void validarUsuario() {
        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    comprovanteResidencia = documentSnapshot.getString(userEnum.USER_PROF_ADDRESS_IMAGE.getDisplayName());
                    if(TextUtils.isEmpty(comprovanteResidencia)){
                        Intent complearCadastro = new Intent(MainActivity.this, CompletarCadastroActivity.class);
                        startActivity(complearCadastro);
                    }
                }
            }
        });
    }

    public void inicializarUsuario() {
//        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//
//                    comprovanteResidencia = documentSnapshot.getString(userEnum.USER_PROF_ADDRESS_IMAGE.getDisplayName());
//
//                    if(TextUtils.isEmpty(comprovanteResidencia)){
//                        Intent complearCadastro = new Intent(MainActivity.this, CompletarCadastroActivity.class);
//                        startActivity(complearCadastro);
//                    }
//
//                    name = documentSnapshot.getString(userEnum.USER_NAME.getDisplayName());
//                    imageUrl = documentSnapshot.getString(userEnum.USER_IMAGE_URL.getDisplayName());
//                    email = documentSnapshot.getString(userEnum.USER_EMAIL.getDisplayName());
//                    phoneNumber = documentSnapshot.getString(userEnum.USER_PHONE.getDisplayName());
//                    isAuthenticated = documentSnapshot.getBoolean(userEnum.USER_IS_AUTHENTICATED.getDisplayName());
//                    isProfessional = documentSnapshot.getBoolean(userEnum.USER_IS_PROFESSIONAL.getDisplayName());
//
//                    user.setName(name);
//                    user.setImageUrl(imageUrl);
//                    user.setEmail(email);
//                    user.setPhoneNumber(phoneNumber);
//                    user.setProfessional(isProfessional);
//                    user.setAuthenticated(isAuthenticated);
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        validarUsuario();
    }

    private void inicializarComponentes() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}